package gg.mooncraft.minecraft.bedwars.game.match;

import lombok.Getter;

import me.neznamy.tab.api.scoreboard.Scoreboard;

import net.kyori.adventure.text.Component;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.GameState;
import gg.mooncraft.minecraft.bedwars.data.GameTeam;
import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.data.map.point.PointTypes;
import gg.mooncraft.minecraft.bedwars.data.map.point.TeamMapPoint;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.events.EventsAPI;
import gg.mooncraft.minecraft.bedwars.game.events.MatchUpdateStateEvent;
import gg.mooncraft.minecraft.bedwars.game.match.systems.AnnouncerSystem;
import gg.mooncraft.minecraft.bedwars.game.match.systems.BlocksSystem;
import gg.mooncraft.minecraft.bedwars.game.match.systems.DamageSystem;
import gg.mooncraft.minecraft.bedwars.game.match.systems.EventSystem;
import gg.mooncraft.minecraft.bedwars.game.match.systems.FurnaceSystem;
import gg.mooncraft.minecraft.bedwars.game.match.systems.GeneratorSystem;
import gg.mooncraft.minecraft.bedwars.game.match.systems.VillagersSystem;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.DragonTask;
import gg.mooncraft.minecraft.bedwars.game.slime.SlimeBukkitPair;
import gg.mooncraft.minecraft.bedwars.game.utilities.PointAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Getter
public final class GameMatch {

    /*
    Fields
     */
    private final UUID uniqueId;
    private final @NotNull String identifier;
    private final @NotNull GameMode gameMode;
    private final @NotNull SlimeBukkitPair dimension;

    private final @NotNull GameTicker gameTicker;
    private final @NotNull Scoreboard scoreboard;

    private final @NotNull List<GameMatchTeam> teamList = new LinkedList<>();
    private final @NotNull List<DragonTask> dragonTaskList = new ArrayList<>();

    private GameState gameState;
    private EventSystem eventSystem;
    private BlocksSystem blocksSystem;
    private DamageSystem damageSystem;
    private FurnaceSystem furnaceSystem;
    private GeneratorSystem generatorSystem;
    private VillagersSystem villagersSystem;
    private AnnouncerSystem announcerSystem;

    /*
    Constructor
     */
    public GameMatch(UUID uniqueId, @NotNull String identifier, @NotNull GameMode gameMode, @NotNull SlimeBukkitPair dimension) {
        this.uniqueId = uniqueId;
        this.identifier = identifier;
        this.gameMode = gameMode;
        this.dimension = dimension;
        this.gameTicker = new GameTicker(this);
        this.scoreboard = BedWarsPlugin.getInstance().getBoardManager().createScoreboard(this);

        getBedWarsMap().ifPresent(bedWarsMap -> {
            bedWarsMap.getPointsContainer().getTeamPointList()
                    .stream()
                    .map(TeamMapPoint::getGameTeam)
                    .sorted((o1, o2) -> o1.getDisplay().compareToIgnoreCase(o2.getDisplay()))
                    .distinct()
                    .forEach(gameTeam -> {
                        GameMatchTeam gameMatchTeam = new GameMatchTeam(this, this.teamList.size(), gameTeam);
                        this.teamList.add(gameMatchTeam);
                    });
            this.teamList.forEach(gameMatchTeam -> {
                gameMatchTeam.initScoreboard(this);
                gameMatchTeam.getMapPointList()
                        .stream()
                        .filter(teamMapPoint -> teamMapPoint.getType() == PointTypes.TEAM.TEAM_BED)
                        .map(teamMapPoint -> PointAdapter.adapt(this, teamMapPoint))
                        .findFirst()
                        .ifPresent(location -> this.dragonTaskList.add(new DragonTask(this, gameMatchTeam.getGameTeam(), location)));
            });
        });

        updateState(GameState.WAITING);
        this.eventSystem = new EventSystem(this);
        this.blocksSystem = new BlocksSystem(this);
        this.damageSystem = new DamageSystem(this);
        this.furnaceSystem = new FurnaceSystem(this);
        this.generatorSystem = new GeneratorSystem(this);
        this.villagersSystem = new VillagersSystem(this);
        this.announcerSystem = new AnnouncerSystem(this);
    }

    /*
    Methods
     */
    public void terminate() {
        this.gameTicker.getSchedulerTask().cancel();
        getPlayerList().forEach(player -> player.kick(Component.empty()));
        BedWarsPlugin.getInstance().getSlimeManager().unloadPairAsync(this.dimension);
        BedWarsPlugin.getInstance().getMatchManager().destroyMatch(this);
        BedWarsPlugin.getInstance().getLogger().info("Game Match " + this.uniqueId.toString() + " has been terminated!");
    }

    public boolean findTeamFor(@NotNull List<UUID> playerList) {
        if (gameState != GameState.WAITING && gameState != GameState.STARTING) return false;
        List<UUID> finalPlayerList = new ArrayList<>(playerList);
        finalPlayerList.removeIf(this::hasPlayer);

        GameMatchTeam freeMatchTeam = null;
        for (GameMatchTeam gameMatchTeam : this.teamList) {
            int teamSize = gameMatchTeam.getMatchPlayerList().size();
            int freeSlots = gameMode.getPlayersPerTeam() - teamSize;
            if (freeSlots < finalPlayerList.size()) continue;
            freeMatchTeam = gameMatchTeam;
            break;
        }
        if (freeMatchTeam == null) return false;

        finalPlayerList.forEach(freeMatchTeam::addPlayer);
        return true;
    }

    public void updateState(@NotNull GameState gameState) {
        this.gameState = gameState;
        EventsAPI.callEventSync(new MatchUpdateStateEvent(this));
    }

    public @NotNull Optional<GameMatchTeam> getTeam(@NotNull GameTeam gameTeam) {
        return this.teamList.stream().filter(gameMatchTeam -> gameMatchTeam.getGameTeam() == gameTeam).findFirst();
    }

    public @NotNull Optional<GameMatchTeam> getTeamOf(@NotNull Player player) {
        return this.teamList.stream().filter(gameMatchTeam -> gameMatchTeam.hasPlayer(player.getUniqueId())).findFirst();
    }

    public @NotNull Optional<GameMatchPlayer> getDataOf(@NotNull Player player) {
        return getTeamOf(player).flatMap(gameMatchTeam -> gameMatchTeam.getPlayer(player.getUniqueId()));
    }

    public boolean hasPlayer(@NotNull UUID uniqueId) {
        return this.teamList.stream().anyMatch(gameMatchTeam -> gameMatchTeam.hasPlayer(uniqueId));
    }

    public @NotNull Optional<BedWarsMap> getBedWarsMap() {
        return BedWarsPlugin.getInstance().getMapManager().getBedWarsMap(this.identifier);
    }

    @UnmodifiableView
    public @NotNull List<Player> getPlayerList() {
        return dimension.world().getPlayers();
    }

    @UnmodifiableView
    public @NotNull List<GameMatchPlayer> getMatchPlayerList() {
        return this.teamList.stream().map(GameMatchTeam::getMatchPlayerList).flatMap(List::stream).toList();
    }

    public int getPlayersCount() {
        int amount = 0;
        for (GameMatchTeam gameMatchTeam : this.teamList) {
            amount += gameMatchTeam.getMatchPlayerList().stream().mapToInt(gameMatchPlayer -> gameMatchPlayer.getPlayer().isPresent() ? 1 : 0).sum();
        }
        return amount;
    }

    public int getPlayersCapacity() {
        return gameMode.getWeight();
    }

    /*
    Override Methods
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameMatch gameMatch = (GameMatch) o;
        return getUniqueId() == gameMatch.getUniqueId() && getIdentifier().equals(gameMatch.getIdentifier()) && getGameMode() == gameMatch.getGameMode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUniqueId(), getIdentifier(), getGameMode());
    }
}
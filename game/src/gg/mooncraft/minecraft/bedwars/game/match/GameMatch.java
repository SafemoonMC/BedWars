package gg.mooncraft.minecraft.bedwars.game.match;

import lombok.Getter;

import me.neznamy.tab.api.scoreboard.Scoreboard;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.GameState;
import gg.mooncraft.minecraft.bedwars.data.GameTeam;
import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.data.map.point.TeamMapPoint;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.events.EventsAPI;
import gg.mooncraft.minecraft.bedwars.game.events.MatchUpdateStateEvent;
import gg.mooncraft.minecraft.bedwars.game.slime.SlimeBukkitPair;

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
    private final int id;
    private final @NotNull String identifier;
    private final @NotNull GameMode gameMode;
    private final @NotNull SlimeBukkitPair slimeBukkitPair;
    private final @NotNull Scoreboard scoreboard;

    private final @NotNull List<GameMatchTeam> teamList = new LinkedList<>();

    private GameState gameState;

    /*
    Constructor
     */
    public GameMatch(int id, @NotNull String identifier, @NotNull GameMode gameMode, @NotNull SlimeBukkitPair slimeBukkitPair) {
        this.id = id;
        this.identifier = identifier;
        this.gameMode = gameMode;
        this.slimeBukkitPair = slimeBukkitPair;
        this.scoreboard = BedWarsPlugin.getInstance().getBoardManager().createScoreboard(this);

        getBedWarsMap().ifPresent(bedWarsMap -> {
            bedWarsMap.getPointsContainer().getTeamPointList()
                    .stream()
                    .map(TeamMapPoint::getGameTeam)
                    .sorted((o1, o2) -> o1.getDisplay().compareToIgnoreCase(o2.getDisplay()))
                    .distinct()
                    .forEach(gameTeam -> this.teamList.add(new GameMatchTeam(this.teamList.size(), gameTeam)));
            this.teamList.forEach(gameMatchTeam -> gameMatchTeam.initScoreboard(this));
        });

        updateState(GameState.WAITING);
    }

    /*
    Methods
     */
    public boolean findTeamFor(@NotNull List<UUID> playerList) {
        if (gameState != GameState.WAITING) return false;

        GameMatchTeam freeMatchTeam = null;
        for (GameMatchTeam gameMatchTeam : teamList) {
            int teamSize = gameMatchTeam.getMatchPlayerList().size();
            int freeSlots = gameMode.getPlayersPerTeam() - teamSize;
            if (freeSlots < playerList.size()) continue;
            freeMatchTeam = gameMatchTeam;
            break;
        }
        if (freeMatchTeam == null) return false;

        playerList.forEach(freeMatchTeam::addPlayer);
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

    public @NotNull Optional<BedWarsMap> getBedWarsMap() {
        return BedWarsPlugin.getInstance().getMapManager().getBedWarsMap(this.identifier);
    }

    @UnmodifiableView
    public @NotNull List<Player> getPlayerList() {
        return slimeBukkitPair.world().getPlayers();
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
        return getId() == gameMatch.getId() && getIdentifier().equals(gameMatch.getIdentifier()) && getGameMode() == gameMatch.getGameMode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getIdentifier(), getGameMode());
    }
}
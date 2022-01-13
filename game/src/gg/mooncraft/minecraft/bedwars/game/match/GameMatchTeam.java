package gg.mooncraft.minecraft.bedwars.game.match;

import lombok.Getter;

import me.neznamy.tab.api.scoreboard.Scoreboard;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import gg.mooncraft.minecraft.bedwars.data.GameTeam;
import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.data.map.MapPointsContainer;
import gg.mooncraft.minecraft.bedwars.data.map.point.TeamMapPoint;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
public final class GameMatchTeam {

    /*
    Fields
     */
    private final @NotNull GameMatch parent;
    private final int id;
    private final @NotNull GameTeam gameTeam;
    private final @NotNull List<GameMatchPlayer> matchPlayerList;
    private final @NotNull Map<String, Integer> upgradesMap;
    private Scoreboard scoreboard;
    private TeamStatus teamStatus;

    /*
    Constructor
     */
    public GameMatchTeam(@NotNull GameMatch parent, int id, @NotNull GameTeam gameTeam) {
        this.parent = parent;
        this.id = id;
        this.gameTeam = gameTeam;
        this.matchPlayerList = new LinkedList<>();
        this.upgradesMap = new HashMap<>();
        this.teamStatus = TeamStatus.ALIVE;
    }

    /*
    Methods
     */
    void initScoreboard(@NotNull GameMatch gameMatch) {
        this.scoreboard = BedWarsPlugin.getInstance().getBoardManager().createScoreboard(gameMatch, this);
    }

    public void broadcastAction(Consumer<GameMatchPlayer> consumer) {
        this.getMatchPlayerList().forEach(consumer);
    }

    public void addPlayer(@NotNull UUID uniqueId) {
        this.matchPlayerList.add(new GameMatchPlayer(this, uniqueId));
    }

    public void delPlayer(@NotNull UUID uniqueId) {
        this.matchPlayerList.removeIf(gameMatchPlayer -> gameMatchPlayer.getUniqueId().equals(uniqueId));
    }

    public boolean hasPlayer(@NotNull UUID uniqueId) {
        return this.matchPlayerList.stream().anyMatch(gameMatchPlayer -> gameMatchPlayer.getUniqueId().equals(uniqueId));
    }

    public @NotNull Optional<GameMatchPlayer> getPlayer(@NotNull UUID uniqueId) {
        return this.matchPlayerList.stream().filter(gameMatchPlayer -> gameMatchPlayer.getUniqueId().equals(uniqueId)).findFirst();
    }

    public void incrementUpgrade(@NotNull String identifier) {
        this.upgradesMap.put(identifier, this.upgradesMap.getOrDefault(identifier, 0) + 1);
    }

    public int getUpgradeTier(@NotNull String identifier) {
        return this.upgradesMap.getOrDefault(identifier, 0);
    }

    public void setStatus(@NotNull TeamStatus teamStatus) {
        this.teamStatus = teamStatus;
    }

    @UnmodifiableView
    public @NotNull List<GameMatchPlayer> getMatchPlayerList() {
        return Collections.unmodifiableList(this.matchPlayerList);
    }

    @UnmodifiableView
    public @NotNull List<TeamMapPoint> getMapPointList() {
        return this.parent.getBedWarsMap().map(BedWarsMap::getPointsContainer).map(MapPointsContainer::getTeamPointList).stream().flatMap(List::stream).filter(teamMapPoint -> teamMapPoint.getGameMode() == this.parent.getGameMode() && teamMapPoint.getGameTeam() == this.gameTeam).toList();
    }

    /*
    Override Methods
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameMatchTeam that = (GameMatchTeam) o;
        return getId() == that.getId() && getGameTeam() == that.getGameTeam();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getGameTeam());
    }
}
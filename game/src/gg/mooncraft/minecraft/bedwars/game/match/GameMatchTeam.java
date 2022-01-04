package gg.mooncraft.minecraft.bedwars.game.match;

import lombok.Getter;

import me.neznamy.tab.api.scoreboard.Scoreboard;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import gg.mooncraft.minecraft.bedwars.data.GameTeam;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
public final class GameMatchTeam {

    /*
    Fields
     */
    private final int id;
    private final @NotNull GameTeam gameTeam;
    private final @NotNull List<GameMatchPlayer> matchPlayerList;
    private Scoreboard scoreboard;
    private TeamStatus teamStatus;

    /*
    Constructor
     */
    public GameMatchTeam(int id, @NotNull GameTeam gameTeam) {
        this.id = id;
        this.gameTeam = gameTeam;
        this.matchPlayerList = new LinkedList<>();

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
        this.matchPlayerList.add(new GameMatchPlayer(uniqueId));
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

    public void setStatus(@NotNull TeamStatus teamStatus) {
        this.teamStatus = teamStatus;
    }

    @UnmodifiableView
    public @NotNull List<GameMatchPlayer> getMatchPlayerList() {
        return Collections.unmodifiableList(matchPlayerList);
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
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
import java.util.Optional;
import java.util.UUID;

@Getter
public final class GameMatchTeam {

    /*
    Fields
     */
    private final int id;
    private final @NotNull GameTeam gameTeam;
    private final @NotNull List<GameMatchPlayer> matchPlayerList;
    private final @NotNull Scoreboard scoreboard;
    private TeamStatus teamStatus;

    /*
    Constructor
     */
    public GameMatchTeam(int id, @NotNull GameTeam gameTeam, @NotNull GameMatch gameMatch) {
        this.id = id;
        this.gameTeam = gameTeam;
        this.matchPlayerList = new LinkedList<>();
        this.scoreboard = BedWarsPlugin.getInstance().getBoardManager().createScoreboard(gameMatch, this);

        this.teamStatus = TeamStatus.ALIVE;
    }

    /*
    Methods
     */
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
}
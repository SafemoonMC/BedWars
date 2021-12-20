package gg.mooncraft.minecraft.bedwars.game.managers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class MatchManager {

    /*
    Fields
     */
    private final @NotNull List<GameMatch> matchList = new ArrayList<>();

    /*
    Methods
     */

    @UnmodifiableView
    public @NotNull List<GameMatch> getMatchList() {
        return Collections.unmodifiableList(this.matchList);
    }

    @UnmodifiableView
    public @NotNull List<GameMatch> getMatchList(@NotNull GameMode gameMode) {
        return getMatchList().stream().filter(gameMatch -> gameMatch.getGameMode() == gameMode).collect(Collectors.toList());
    }
}
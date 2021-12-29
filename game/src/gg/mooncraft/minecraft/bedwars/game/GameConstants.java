package gg.mooncraft.minecraft.bedwars.game;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class GameConstants {

    /*
    Constants
     */
    public static final @NotNull String DEFAULT_SCOREBOARD_TITLE = "&4~ BedWars ~";
    public static final @NotNull List<String> DEFAULT_SCOREBOARD_LINES = Arrays.asList(
            "&7%date% &8%server-name%",
            "",
            "&3&l| &r%game-status%",
            "",
            "{teams}",
            "",
            "&b  play.mooncraft.gg"
    );

    public static final @NotNull String DEFAULT_WORLD_NAME = "VoidworldStart";
}
package gg.mooncraft.minecraft.bedwars.game;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.utilities.DisplayUtilities;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class GameConstants {

    /*
    Constants
     */
    public static final @NotNull String DEFAULT_WORLD_NAME = "VoidworldStart";

    public static final @NotNull String NAMETAG_FORMAT = "%color%%team% &8| %color%";
    public static final @NotNull String SCOREBOARD_TITLE = "&b⁂ &6&lBedWars &b⁂";
    public static final @NotNull List<String> SCOREBOARD_LINES_LOBBY = Arrays.asList(
            "&7%date% &8%server-name%",
            "&r",
            "&3&l| &fMap: &a%game-map%",
            "&3&l| &fPlayers: &a%game-players-count%/%game-players-capacity%",
            "&r",
            "&3&l| &r%game-status%",
            "&r",
            "&3&l| &fMode: &a%game-mode%",
            "&3&l| &fVersion: &7v%plugin-version%",
            "&r",
            "&b  play.mooncraft.gg"
    );
    public static final @NotNull List<String> SCOREBOARD_LINES_PLAY = Arrays.asList(
            "&7%date%  &8%server-name%",
            "&r",
            "&3&l| &r%game-event%",
            "&r",
            "{teams}",
            "&r",
            "&b  play.mooncraft.gg"
    );
    public static final @NotNull List<String> GENERATOR_HOLOGRAM_LINES = Arrays.asList(
            DisplayUtilities.getColored("&eTier &c%tier%"),
            DisplayUtilities.getColored(""),
            DisplayUtilities.getColored("%generator-type%"),
            DisplayUtilities.getColored(""),
            DisplayUtilities.getColored("&eSpawning in &c%time-left% &e%time-unit%")
    );

    public static final @NotNull String SHOP_ITEMS_TITLE = DisplayUtilities.getColored("&6Items Shop");
    public static final @NotNull String SHOP_UPGRADES_TITLE = DisplayUtilities.getColored("&6Upgrades Shop");

    public static final @NotNull String MESSAGE_PLAYER_DIES = DisplayUtilities.getColored("&7&o%player% died!");
    public static final @NotNull String MESSAGE_PLAYER_KILL = DisplayUtilities.getColored("&7&o%killer% killed %killed% with %weapon%!");
    public static final @NotNull String MESSAGE_PLAYER_JOIN = "%vault_rankprefix%%vault_suffix%%player_name% &ehas joined (&b%game-players-count%&e/&b%game-players-capacity%&e)!";
    public static final @NotNull String MESSAGE_PLAYER_QUIT = "%vault_rankprefix%%vault_suffix%%player_name% &ehas quit!";
    public static final @NotNull String MESSAGE_GLOBAL_STARTING = DisplayUtilities.getColored("&eThe game starts in %time-color%%time-left% %time-unit%!");
    public static final @NotNull String MESSAGE_ACTIONBAR_WAITING = DisplayUtilities.getColored("&6The game will start once necessary players join");
    public static final @NotNull List<String> MESSAGE_STARTING_TIP = Arrays.asList(
            DisplayUtilities.getColored("&b&m                                                                             &r"),
            DisplayUtilities.getColored("                                &3&lBED WARS"),
            DisplayUtilities.getColored(""),
            DisplayUtilities.getColored("         &f&lProtect your bed and destroy enemy beds."),
            DisplayUtilities.getColored("     &f&lUpgrade yourself and your team by collecting"),
            DisplayUtilities.getColored("                  &f&lminerals from Generators."),
            DisplayUtilities.getColored(""),
            DisplayUtilities.getColored("&b&m                                                                             &r")
    );
}
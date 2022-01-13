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

    public static final int ANNOUNCER_TIP_INTERVAL = 190;
    public static final @NotNull List<String> ANNOUNCER_MESSAGES = Arrays.asList(
            DisplayUtilities.getColored("&cCross-teaming is not allowed! Report rule breakers with /report <player>."),
            DisplayUtilities.getColored("&cReport rule breakers with /report <player>.")
    );

    public static final @NotNull String BOSSBAR_TITLE = DisplayUtilities.getColored("&f&lPlaying &3&lBED WARS &f&lon &3&lPLAY.MOONCRAFT.GG");
    public static final @NotNull String NAMETAG_FORMAT = "%color%%team% &7| %color%";
    public static final @NotNull String SCOREBOARD_TITLE = "&b⁂ &6&lBED WARS &b⁂";
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

    public static final @NotNull String SHOP_ITEMS_TITLE = DisplayUtilities.getColored("&8Items Shop");
    public static final @NotNull String SHOP_UPGRADES_TITLE = DisplayUtilities.getColored("&8Upgrades Shop");

    public static final @NotNull String MESSAGE_SHOP_CANNOT_AFFORD = DisplayUtilities.getColored("&cYou don't have enough materials!");
    public static final @NotNull String MESSAGE_SHOP_BUY = DisplayUtilities.getColored("&bYou purchased &3%shop-item%");

    public static final @NotNull String MESSAGE_SPECTATOR_TITLE = DisplayUtilities.getColored("&cYOU DIED!");
    public static final @NotNull String MESSAGE_SPECTATOR_SUBTITLE = DisplayUtilities.getColored("&eYou will respawn in %time% %time-unit%!");
    public static final @NotNull String MESSAGE_SPECTATOR_RESPAWN_CHAT = DisplayUtilities.getColored("&eYou have respawned!");
    public static final @NotNull String MESSAGE_SPECTATOR_RESPAWN_TITLE = DisplayUtilities.getColored("&aRESPAWNED!");
    public static final @NotNull String MESSAGE_BED_YOUR = DisplayUtilities.getColored("&cYou cannot break your bed!");
    public static final @NotNull String MESSAGE_BED_DESTRUCTION_TITLE = DisplayUtilities.getColored("&cBED DESTROYED!");
    public static final @NotNull String MESSAGE_BED_DESTRUCTION_SUBTITLE = DisplayUtilities.getColored("&fYou will no longer respawn!");
    public static final @NotNull List<String> MESSAGE_BED_DESTRUCTION_YOUR = Arrays.asList(
            "",
            DisplayUtilities.getColored("&f&lBED DESTRUCTION > &r&7Your Bed was destroyed by %destroyer-team-color%%destroyer-player-name%&7!"),
            ""
    );
    public static final @NotNull List<String> MESSAGE_BED_DESTRUCTION_OTHERS = Arrays.asList(
            "",
            DisplayUtilities.getColored("&f&lBED DESTRUCTION > &r%team-color%%team-name% &7Bed was destroyed by %destroyer-team-color%%destroyer-player-name%&7!"),
            ""
    );

    public static final @NotNull String MESSAGE_BRIDGEEGG_WRONG_ANGLE = DisplayUtilities.getColored("&cYou can't throw the Bridge Egg at that angle (too high or too low)!");
    public static final @NotNull String MESSAGE_BLOCK_UNBREAKABLE = DisplayUtilities.getColored("&cYou can only break blocks placed by other players!");
    public static final @NotNull String MESSAGE_BLOCK_HEIGHT_LIMIT = DisplayUtilities.getColored("&cBuild height limit reached!");
    public static final @NotNull String MESSAGE_PLAYER_INVISIBILITY = DisplayUtilities.getColored("&aYou are completely invisible including your armor for the next 30 seconds or until you get hit!");
    public static final @NotNull String MESSAGE_PLAYER_INVISIBILITY_END = DisplayUtilities.getColored("&cYou are no longer invisible!");
    public static final @NotNull String MESSAGE_PLAYER_INVISIBILITY_ARMOR = DisplayUtilities.getColored("&cYour armor is no longer invisible since you got hit!");
    public static final @NotNull String MESSAGE_PLAYER_DIES = DisplayUtilities.getColored("&7&o%player% died!");
    public static final @NotNull String MESSAGE_PLAYER_KILL = DisplayUtilities.getColored("&7&o%killer% killed %killed% with %weapon%!");
    public static final @NotNull String MESSAGE_PLAYER_JOIN = "%vault_rankprefix%%vault_suffix%%player_name% &ehas joined (&b%game-players-count%&e/&b%game-players-capacity%&e)!";
    public static final @NotNull String MESSAGE_PLAYER_QUIT = "%vault_rankprefix%%vault_suffix%%player_name% &ehas quit!";
    public static final @NotNull String MESSAGE_GLOBAL_STARTING = DisplayUtilities.getColored("&eThe game starts in %time-color%%time-left% %time-unit%!");
    public static final @NotNull String MESSAGE_ACTIONBAR_WAITING = DisplayUtilities.getColored("&6The game will start once the necessary players join");
    public static final @NotNull List<String> MESSAGE_PLAYER_MOVE = Arrays.asList(DisplayUtilities.getColored("&8Sending you to %server-name%..."), "", "");
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
package gg.mooncraft.minecraft.bedwars.lobby.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.Prestige;
import gg.mooncraft.minecraft.bedwars.data.user.BedWarsUser;
import gg.mooncraft.minecraft.bedwars.data.user.stats.StatisticTypes;
import gg.mooncraft.minecraft.bedwars.lobby.BedWarsPlugin;

import java.math.BigInteger;
import java.text.DecimalFormat;

/**
 * A PlaceholderAPI expansion that implements the following placeholders:
 * - %bw_coins% -> returns coins
 * - %bw_level% -> returns current level
 * - %bw_level-next% -> returns next level
 * - %bw_experience% -> returns current experience
 * - %bw_experience-current% -> returns current experience
 * - %bw_experience-required% -> returns required experience
 * - %bw_experience-percentage% -> returns percentage experience
 * - %bw_experience-progress-bar% -> returns progress-bar experience
 * - %bw_prestige% -> returns prestige
 * - %bw_prestige-next% -> returns next prestige
 * - %bw_stat-game-[type]% -> returns a sum of all [type] amount from all games
 * - %bw_stat-game-[mode]-[type]% -> returns the statistic [type] for that [mode]
 * - %bw_stat-overall-[type]% -> returns an overall statistic
 * - %bw_counter-[game mode]% -> returns the amount of players playing that game mode
 */
public class BedWarsExpansion extends PlaceholderExpansion {

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return "INVALID_PLAYER";
        if (params.equalsIgnoreCase("coins")) {
            return BedWarsPlugin.getInstance().getUserFactory().getUser(player)
                    .map(BedWarsUser::getCoins)
                    .map(BigInteger::intValue)
                    .map(String::valueOf)
                    .orElse("0");
        } else if (params.equalsIgnoreCase("level")) {
            return BedWarsPlugin.getInstance().getUserFactory().getUser(player)
                    .map(BedWarsUser::getLevel)
                    .map(String::valueOf)
                    .orElse("0");
        } else if (params.equalsIgnoreCase("level-next")) {
            return BedWarsPlugin.getInstance().getUserFactory().getUser(player)
                    .map(BedWarsUser::getNextLevel)
                    .map(String::valueOf)
                    .orElse("0");
        } else if (params.equalsIgnoreCase("level-prestige")) {
            return BedWarsPlugin.getInstance().getUserFactory().getUser(player)
                    .map(bedWarsUser -> bedWarsUser.getPrestige().applyColors(bedWarsUser.getLevel()))
                    .orElse("0");
        } else if (params.equalsIgnoreCase("experience")) {
            return BedWarsPlugin.getInstance().getUserFactory().getUser(player)
                    .map(BedWarsUser::getExperience)
                    .map(String::valueOf)
                    .orElse("0");
        } else if (params.equalsIgnoreCase("experience-current")) {
            return BedWarsPlugin.getInstance().getUserFactory().getUser(player)
                    .map(BedWarsUser::getExperienceCurrent)
                    .map(String::valueOf)
                    .orElse("0");
        } else if (params.equalsIgnoreCase("experience-required")) {
            return BedWarsPlugin.getInstance().getUserFactory().getUser(player)
                    .map(BedWarsUser::getExperienceRequired)
                    .map(String::valueOf)
                    .orElse("0");
        } else if (params.equalsIgnoreCase("experience-percentage")) {
            return BedWarsPlugin.getInstance().getUserFactory().getUser(player)
                    .map(BedWarsUser::getExperiencePercentage)
                    .map(f -> new DecimalFormat("#.##").format(f))
                    .orElse("0");
        } else if (params.equalsIgnoreCase("experience-progress-bar")) {
            return BedWarsPlugin.getInstance().getUserFactory().getUser(player)
                    .map(BedWarsUser::getExperienceProgressBar)
                    .orElse("0");
        } else if (params.equalsIgnoreCase("prestige")) {
            return BedWarsPlugin.getInstance().getUserFactory().getUser(player)
                    .map(BedWarsUser::getPrestige)
                    .map(Prestige::getDisplay)
                    .orElse("-");
        } else if (params.equalsIgnoreCase("prestige-next")) {
            return BedWarsPlugin.getInstance().getUserFactory().getUser(player)
                    .map(BedWarsUser::getNextPrestige)
                    .map(Prestige::getDisplay)
                    .orElse("-");
        }

        if (params.contains("-")) {
            String[] args = params.split("-");
            if (args.length == 2) {
                String name = args[0];
                if (name.equalsIgnoreCase("counter")) {
                    GameMode gameMode = parseGameMode(args[1]);
                    if (gameMode == null) return "INVALID_GAMEMODE";
                    return String.valueOf(BedWarsPlugin.getInstance().getGameServerManager().getOnlinePlayers(gameMode));
                }
            }
            if (args.length == 3) {
                String name = args[0];
                String type = args[1];
                if (name.equalsIgnoreCase("stat")) {
                    if (type.equalsIgnoreCase("game")) {
                        StatisticTypes.GAME stat = StatisticTypes.GAME.valueOf(args[2].toUpperCase());
                        return BedWarsPlugin.getInstance().getUserFactory().getUser(player)
                                .map(BedWarsUser::getStatisticContainer)
                                .map(userStatisticContainer -> userStatisticContainer.getGameStatisticTotal(stat))
                                .map(BigInteger::intValue)
                                .map(String::valueOf)
                                .orElse("0");
                    } else if (type.equalsIgnoreCase("overall")) {
                        StatisticTypes.OVERALL stat = StatisticTypes.OVERALL.valueOf(args[2].toUpperCase());
                        return BedWarsPlugin.getInstance().getUserFactory().getUser(player)
                                .map(BedWarsUser::getStatisticContainer)
                                .map(userStatisticContainer -> userStatisticContainer.getOverallStatistic(stat))
                                .map(BigInteger::intValue)
                                .map(String::valueOf)
                                .orElse("0");
                    }
                }
            }
            if (args.length == 4) {
                String name = args[0];
                String type = args[1];
                if (name.equalsIgnoreCase("stat")) {
                    if (type.equalsIgnoreCase("game")) {
                        GameMode mode = GameMode.valueOf(args[2].toUpperCase());
                        StatisticTypes.GAME stat = StatisticTypes.GAME.valueOf(args[3].toUpperCase());
                        return BedWarsPlugin.getInstance().getUserFactory().getUser(player)
                                .map(BedWarsUser::getStatisticContainer)
                                .map(userStatisticContainer -> userStatisticContainer.getGameStatistic(mode, stat))
                                .map(BigInteger::intValue)
                                .map(String::valueOf)
                                .orElse("0");
                    }
                }
            }
        }

        return "INVALID_PARAMS";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "bw";
    }

    @Override
    public @NotNull String getAuthor() {
        return BedWarsPlugin.getInstance().getDescription().getAuthors().stream().findFirst().orElse("");
    }

    @Override
    public @NotNull String getVersion() {
        return BedWarsPlugin.getInstance().getDescription().getVersion();
    }

    /*
    Methods
     */
    private @Nullable GameMode parseGameMode(@NotNull String arg) {
        try {
            return GameMode.valueOf(arg.toUpperCase());
        } catch (Exception ignored) {
            return null;
        }
    }
}
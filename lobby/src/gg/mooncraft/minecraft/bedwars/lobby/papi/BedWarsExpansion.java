package gg.mooncraft.minecraft.bedwars.lobby.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.user.BedWarsUser;
import gg.mooncraft.minecraft.bedwars.data.user.stats.StatisticTypes;
import gg.mooncraft.minecraft.bedwars.lobby.BedWarsPlugin;

import java.math.BigInteger;

/**
 * A PlaceholderAPI expansion that implements the following placeholders:
 * - %bw_coins% -> returns coins
 * - %bw_level% -> returns level
 * - %bw_experience% -> returns experience
 * - %bw_stat-game-[type]% -> returns a sum of all [type] amount from all games
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
                    .map(BigInteger::intValue)
                    .map(String::valueOf)
                    .orElse("0");
        } else if (params.equalsIgnoreCase("experience")) {
            return BedWarsPlugin.getInstance().getUserFactory().getUser(player)
                    .map(BedWarsUser::getExperience)
                    .map(BigInteger::intValue)
                    .map(String::valueOf)
                    .orElse("0");
        }

        if (params.contains("-")) {
            String[] args = params.split("-");
            if (args.length == 2) {
                String name = args[0];
                if (name.equalsIgnoreCase("counter")) {
                    GameMode gameMode = parseGameMode(args[1]);
                    return "NOT YET";
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
            return GameMode.valueOf(arg);
        } catch (Exception ignored) {
            return null;
        }
    }
}
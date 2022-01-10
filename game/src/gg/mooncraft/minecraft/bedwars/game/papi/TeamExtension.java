package gg.mooncraft.minecraft.bedwars.game.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gg.mooncraft.minecraft.bedwars.data.GameTeam;
import gg.mooncraft.minecraft.bedwars.data.user.BedWarsUser;
import gg.mooncraft.minecraft.bedwars.data.user.stats.StatisticTypes;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchTeam;

import java.math.BigInteger;

public class TeamExtension extends PlaceholderExpansion {

    /*
    Override Methods
     */
    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return "INVALID_PLAYER";
        if (params.equalsIgnoreCase("level")) {
            return BedWarsPlugin.getInstance().getUserFactory().getUser(player)
                    .map(BedWarsUser::getLevel)
                    .map(BigInteger::intValue)
                    .map(String::valueOf)
                    .orElse("0");
        }

        if (params.contains("-")) {
            String[] args = params.split("-");
            if (args.length == 2) {
                String name = args[0];
                String type = args[1];
                if (name.equalsIgnoreCase("team")) {
                    if (type.equalsIgnoreCase("color")) {
                        return BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player).flatMap(gameMatch -> gameMatch.getTeamOf(player)).map(GameMatchTeam::getGameTeam).map(GameTeam::getChatColor).map(ChatColor::toString).orElse("&r");
                    } else if (type.equalsIgnoreCase("symbol")) {
                        return BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player).flatMap(gameMatch -> gameMatch.getTeamOf(player)).map(GameMatchTeam::getGameTeam).map(GameTeam::getLetter).map(String::valueOf).orElse("-");
                    } else if (type.equalsIgnoreCase("name")) {
                        return BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player).flatMap(gameMatch -> gameMatch.getTeamOf(player)).map(GameMatchTeam::getGameTeam).map(GameTeam::getDisplay).map(String::valueOf).orElse("-");
                    }
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
        return "INVALID_REQUEST";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "bw";
    }

    @Override
    public @NotNull String getAuthor() {
        return BedWarsPlugin.getInstance().getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }
}

package gg.mooncraft.minecraft.bedwars.game.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gg.mooncraft.minecraft.bedwars.data.GameTeam;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchTeam;

public class TeamExtension extends PlaceholderExpansion {

    /*
    Override Methods
     */
    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equalsIgnoreCase("team-color")) {
            return BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player).flatMap(gameMatch -> gameMatch.getTeamOf(player)).map(GameMatchTeam::getGameTeam).map(GameTeam::getChatColor).map(ChatColor::toString).orElse("&r");
        }
        if (params.equalsIgnoreCase("team-symbol")) {
            return BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player).flatMap(gameMatch -> gameMatch.getTeamOf(player)).map(GameMatchTeam::getGameTeam).map(GameTeam::getLetter).map(String::valueOf).orElse("-");
        }
        return super.onPlaceholderRequest(player, params);
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

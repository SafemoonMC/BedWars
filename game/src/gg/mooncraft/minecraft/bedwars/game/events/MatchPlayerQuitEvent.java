package gg.mooncraft.minecraft.bedwars.game.events;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;

public class MatchPlayerQuitEvent extends MatchPlayerEvent {

    /*
    Constructor
     */
    public MatchPlayerQuitEvent(@NotNull Player player, @NotNull GameMatchPlayer matchPlayer) {
        super(player, matchPlayer);
    }
}
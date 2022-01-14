package gg.mooncraft.minecraft.bedwars.game.events;

import lombok.Getter;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;

@Getter
public class MatchPlayerDamageEvent extends MatchPlayerEvent {

    /*
    Fields
     */
    private final double damage;

    /*
    Constructor
     */
    public MatchPlayerDamageEvent(@NotNull Player player, @NotNull GameMatchPlayer matchPlayer, double damage) {
        super(player, matchPlayer);
        this.damage = damage;
    }
}
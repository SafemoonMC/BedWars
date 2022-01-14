package gg.mooncraft.minecraft.bedwars.game.events;

import lombok.Getter;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;

@Getter
public class MatchPlayerPickupItemEvent extends MatchPlayerEvent {

    /*
    Constants
     */
    private static final @NotNull HandlerList HANDLERS = new HandlerList();

    /*
    Fields
     */
    private final @NotNull Item item;

    /*
    Constructor
     */
    public MatchPlayerPickupItemEvent(@NotNull Player player, @NotNull GameMatchPlayer matchPlayer, @NotNull Item item) {
        super(player, matchPlayer);
        this.item = item;
    }
}
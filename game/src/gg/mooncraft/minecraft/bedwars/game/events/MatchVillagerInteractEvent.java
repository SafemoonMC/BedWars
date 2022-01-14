package gg.mooncraft.minecraft.bedwars.game.events;

import lombok.Getter;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.MatchVillager;

@Getter
public class MatchVillagerInteractEvent extends MatchPlayerEvent {

    /*
    Constants
     */
    private static final @NotNull HandlerList HANDLERS = new HandlerList();

    /*
    Fields
     */
    private final @NotNull MatchVillager matchVillager;

    /*
    Constructor
     */
    public MatchVillagerInteractEvent(@NotNull Player player, @NotNull GameMatchPlayer matchPlayer, @NotNull MatchVillager matchVillager) {
        super(player, matchPlayer);
        this.matchVillager = matchVillager;
    }
}
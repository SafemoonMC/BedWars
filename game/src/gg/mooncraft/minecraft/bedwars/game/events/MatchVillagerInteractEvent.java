package gg.mooncraft.minecraft.bedwars.game.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchTeam;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.MatchVillager;

@Getter
@AllArgsConstructor
public class MatchVillagerInteractEvent extends Event {

    /*
    Constants
     */
    private static final @NotNull HandlerList HANDLERS = new HandlerList();

    /*
    Fields
     */
    private final @NotNull Player player;
    private final @NotNull GameMatchPlayer gameMatchPlayer;
    private final @NotNull MatchVillager matchVillager;

    /*
    Methods
     */
    public @NotNull GameMatch getGameMatch() {
        return this.matchVillager.getGameMatch();
    }

    public @NotNull GameMatchTeam getGameMatchTeam() {
        return this.gameMatchPlayer.getParent();
    }

    /*
    Static Methods
     */
    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

    /*
    Override Methods
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}

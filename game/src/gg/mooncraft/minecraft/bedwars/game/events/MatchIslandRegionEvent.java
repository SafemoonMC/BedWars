package gg.mooncraft.minecraft.bedwars.game.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameTeam;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchTeam;

@Getter
@AllArgsConstructor
public class MatchIslandRegionEvent extends Event {

    /*
    Inner
     */
    public enum Action {
        ENTER, LEAVE
    }

    /*
    Constants
     */
    private static final @NotNull HandlerList HANDLERS = new HandlerList();

    /*
    Fields
     */
    private final @NotNull Player player;
    private final @NotNull GameMatchPlayer matchPlayer;
    private final @NotNull GameTeam team;
    private final @NotNull Action action;

    /*
    Methods
     */
    public @NotNull GameMatch getGameMatch() {
        return this.matchPlayer.getParent().getParent();
    }

    public @NotNull GameMatchTeam getGameMatchTeam() {
        return this.matchPlayer.getParent();
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
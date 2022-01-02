package gg.mooncraft.minecraft.bedwars.game.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.GameMatchEvent;

@Getter
@AllArgsConstructor
public class MatchUpdateGameEvent extends Event {

    /*
    Constants
     */
    private static final @NotNull HandlerList HANDLERS = new HandlerList();

    /*
    Fields
     */
    private final @NotNull GameMatch gameMatch;
    private final @NotNull GameMatchEvent gameMatchEvent;

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

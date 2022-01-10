package gg.mooncraft.minecraft.bedwars.game.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchTeam;

@Getter
@AllArgsConstructor
public class MatchBlockPlaceEvent extends Event implements Cancellable {

    /*
    Constants
     */
    private static final @NotNull HandlerList HANDLERS = new HandlerList();

    /*
    Fields
     */
    private final @NotNull Player player;
    private final @NotNull Location location;
    private final @NotNull GameMatch gameMatch;
    private final @NotNull GameMatchPlayer gameMatchPlayer;
    @Setter
    private boolean cancelled = false;

    /*
    Constructor
     */
    public MatchBlockPlaceEvent(@NotNull Player player, @NotNull Location location, @NotNull GameMatch gameMatch, @NotNull GameMatchPlayer gameMatchPlayer) {
        this.player = player;
        this.location = location;
        this.gameMatch = gameMatch;
        this.gameMatchPlayer = gameMatchPlayer;
    }

    /*
    Methods
     */
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

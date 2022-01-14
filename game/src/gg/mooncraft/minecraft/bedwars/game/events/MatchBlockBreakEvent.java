package gg.mooncraft.minecraft.bedwars.game.events;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;

@Getter
public class MatchBlockBreakEvent extends MatchPlayerEvent implements Cancellable {

    /*
    Fields
     */
    private final @NotNull Location location;
    @Setter
    private boolean cancelled = false;

    /*
    Constructor
     */
    public MatchBlockBreakEvent(@NotNull Player player, @NotNull GameMatchPlayer matchPlayer, @NotNull Location location) {
        super(player, matchPlayer);
        this.location = location;
    }
}
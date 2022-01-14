package gg.mooncraft.minecraft.bedwars.game.events;

import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;

@Getter
public class MatchPlayerMoveEvent extends MatchPlayerEvent {

    /*
    Fields
     */
    private final @NotNull Location to;
    private final @NotNull Location from;

    /*
    Constructor
     */
    public MatchPlayerMoveEvent(@NotNull Player player, @NotNull GameMatchPlayer matchPlayer, @NotNull Location to, @NotNull Location from) {
        super(player, matchPlayer);
        this.to = to;
        this.from = from;
    }
}
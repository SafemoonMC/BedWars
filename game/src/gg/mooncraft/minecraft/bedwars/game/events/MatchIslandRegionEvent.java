package gg.mooncraft.minecraft.bedwars.game.events;

import lombok.Getter;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameTeam;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;

@Getter
public class MatchIslandRegionEvent extends MatchPlayerEvent {

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
    private final @NotNull GameTeam team;
    private final @NotNull Action action;

    /*
    Constructor
     */
    public MatchIslandRegionEvent(@NotNull Player player, @NotNull GameMatchPlayer matchPlayer, @NotNull GameTeam gameTeam, @NotNull Action action) {
        super(player, matchPlayer);
        this.team = gameTeam;
        this.action = action;
    }
}
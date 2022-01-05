package gg.mooncraft.minecraft.bedwars.game.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchTeam;
import gg.mooncraft.minecraft.bedwars.game.match.damage.PlayerDamage;

@Getter
@AllArgsConstructor
public class MatchPlayerDeathEvent extends Event {

    /*
    Inner
     */
    public enum Reason {
        PLAYER, UNKNOWN
    }

    /*
    Constants
     */
    private static final @NotNull HandlerList HANDLERS = new HandlerList();

    /*
    Fields
     */
    private final @NotNull Player player;
    private final @NotNull GameMatch gameMatch;
    private final @NotNull GameMatchTeam playerMatchTeam;
    private final @NotNull GameMatchPlayer playerMatchPlayer;
    private final @NotNull Reason reason;
    private final @Nullable PlayerDamage lastPlayerDamage;

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

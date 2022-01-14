package gg.mooncraft.minecraft.bedwars.game.events;

import lombok.Getter;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.damage.PlayerDamage;

@Getter
public class MatchPlayerDeathEvent extends MatchPlayerEvent {

    /*
    Inner
     */
    public enum DeathReason {
        PLAYER, UNKNOWN
    }

    /*
    Fields
     */
    private final @NotNull DeathReason deathReason;
    private final @Nullable PlayerDamage lastPlayerDamage;

    /*
    Constructor
     */
    public MatchPlayerDeathEvent(@NotNull Player player, @NotNull GameMatchPlayer matchPlayer, @NotNull DeathReason deathReason) {
        super(player, matchPlayer);
        this.deathReason = deathReason;
        this.lastPlayerDamage = null;
    }

    public MatchPlayerDeathEvent(@NotNull Player player, @NotNull GameMatchPlayer matchPlayer, @NotNull DeathReason deathReason, @Nullable PlayerDamage lastPlayerDamage) {
        super(player, matchPlayer);
        this.deathReason = deathReason;
        this.lastPlayerDamage = lastPlayerDamage;
    }
}
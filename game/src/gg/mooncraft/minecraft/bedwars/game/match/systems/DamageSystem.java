package gg.mooncraft.minecraft.bedwars.game.match.systems;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.damage.DamageTracker;
import gg.mooncraft.minecraft.bedwars.game.match.damage.PlayerDamage;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public final class DamageSystem {

    /*
    Fields
     */
    private final @NotNull GameMatch gameMatch;
    private final @NotNull List<DamageTracker> trackerList = new LinkedList<>();

    /*
    Constructor
     */
    public DamageSystem(@NotNull GameMatch gameMatch) {
        this.gameMatch = gameMatch;
    }

    /*
    Methods
     */
    public void trackPlayer(@NotNull Player player, @NotNull Player damager, double damage) {
        getTracker(player).ifPresentOrElse(damageTracker -> {
            damageTracker.trackDamage(damager, damager.getInventory().getItemInMainHand(), damage);
        }, () -> {
            DamageTracker damageTracker = new DamageTracker(player);
            damageTracker.trackDamage(damager, damager.getInventory().getItemInMainHand(), damage);
            this.trackerList.add(damageTracker);
        });
    }

    public @NotNull Optional<PlayerDamage> getHighestTracker(@NotNull Player player) {
        Optional<PlayerDamage> optionalPlayerDamage = getTracker(player).flatMap(DamageTracker::getHighestDamage);
        optionalPlayerDamage.ifPresent(playerDamage -> this.trackerList.removeIf(damageTracker -> damageTracker.getPlayer().equals(player)));
        return optionalPlayerDamage;
    }

    public @NotNull Optional<DamageTracker> getTracker(@NotNull Player player) {
        return this.trackerList.stream().filter(damageTracker -> damageTracker.getPlayer().equals(player)).findFirst();
    }

    @UnmodifiableView
    public @NotNull List<DamageTracker> getTrackerList() {
        return Collections.unmodifiableList(this.trackerList);
    }
}
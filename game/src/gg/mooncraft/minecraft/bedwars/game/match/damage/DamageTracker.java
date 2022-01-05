package gg.mooncraft.minecraft.bedwars.game.match.damage;

import lombok.Getter;

import com.google.common.util.concurrent.AtomicDouble;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
public final class DamageTracker {

    /*
    Fields
     */
    private final @NotNull Player player;
    private final @NotNull List<PlayerDamage> damagerList;

    /*
    Constructor
     */
    public DamageTracker(@NotNull Player player) {
        this.player = player;
        this.damagerList = new ArrayList<>();
    }

    /*
    Methods
     */
    public void trackDamage(@NotNull Player player, @NotNull ItemStack itemStack, double damage) {
        Optional<PlayerDamage> optionalPlayerDamage = getPlayerDamage(player);
        optionalPlayerDamage.ifPresentOrElse(playerDamage -> {
            playerDamage.setWeapon(itemStack);
            playerDamage.setInstant(Instant.now());
            playerDamage.getDamage().addAndGet(damage);
        }, () -> {
            PlayerDamage playerDamage = new PlayerDamage(player, new AtomicDouble(damage), Instant.now(), itemStack);
            this.damagerList.add(playerDamage);
        });
    }

    public @NotNull Optional<PlayerDamage> getHighestDamage() {
        return this.damagerList.stream().max(Comparator.comparingDouble(o -> o.getDamage().get()));
    }

    public @NotNull Optional<PlayerDamage> getPlayerDamage(@NotNull Player player) {
        return this.damagerList.stream().filter(playerDamage -> playerDamage.getPlayer().equals(player)).findFirst();
    }

    /*
    Override Methods
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DamageTracker that = (DamageTracker) o;
        return getPlayer().equals(that.getPlayer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayer());
    }
}
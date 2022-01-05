package gg.mooncraft.minecraft.bedwars.game.match.damage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.google.common.util.concurrent.AtomicDouble;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public final class PlayerDamage {

    /*
    Fields
     */
    private final @NotNull Player player;
    private final @NotNull AtomicDouble damage;
    private @NotNull Instant instant;
    private @NotNull ItemStack weapon;
}
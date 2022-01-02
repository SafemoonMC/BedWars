package gg.mooncraft.minecraft.bedwars.game.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class EntityUtilities {

    public static void spawnItemStack(@NotNull Location location, ItemStack itemStack) {
        BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
            Item item = location.getWorld().dropItem(location, itemStack);
            item.setVelocity(new Vector(0, 0.1, 0));
        });
    }

    public static @NotNull ArmorStand createGeneratorStand(@NotNull Location location, @NotNull Material headMaterial) {
        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setItem(EquipmentSlot.HEAD, new ItemStack(headMaterial));
        armorStand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING_OR_CHANGING);

        armorStand.setSmall(true);
        armorStand.setArms(false);
        armorStand.setBasePlate(false);
        armorStand.setGravity(false);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        return armorStand;
    }
}
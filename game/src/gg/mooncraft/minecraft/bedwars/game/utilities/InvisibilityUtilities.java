package gg.mooncraft.minecraft.bedwars.game.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.mojang.datafixers.util.Pair;

import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EnumItemSlot;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This utility class is made for 1.17.1 specifically.
 * The known variables are:
 * EnumItemSlot.c = feet
 * EnumItemSlot.d = legs
 * EnumItemSlot.e = chest
 * EnumItemSlot.f = head
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class InvisibilityUtilities {

    /**
     * Hides the armor of the player
     *
     * @param player the player whose armor will be hidden
     */
    public static void hideArmor(@NotNull Player player) {
        // Prepare the fake equipment list
        List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> equipmentList = new ArrayList<>();
        equipmentList.add(Pair.of(EnumItemSlot.c, CraftItemStack.asNMSCopy(new ItemStack(Material.AIR))));
        equipmentList.add(Pair.of(EnumItemSlot.d, CraftItemStack.asNMSCopy(new ItemStack(Material.AIR))));
        equipmentList.add(Pair.of(EnumItemSlot.e, CraftItemStack.asNMSCopy(new ItemStack(Material.AIR))));
        equipmentList.add(Pair.of(EnumItemSlot.f, CraftItemStack.asNMSCopy(new ItemStack(Material.AIR))));

        // Building the packet
        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(player.getEntityId(), equipmentList);

        // Sending the packet to observers
        for (Player otherPlayer : player.getWorld().getPlayers()) {
            PlayerConnection playerConnection = ((CraftPlayer) otherPlayer).getHandle().b;
            playerConnection.sendPacket(packet);
        }
    }

    /**
     * Shows the armor of the player
     *
     * @param player the player whose armor will be shown
     */
    public static void showArmor(@NotNull Player player) {
        List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> equipmentList = new ArrayList<>();
        equipmentList.add(Pair.of(EnumItemSlot.c, CraftItemStack.asNMSCopy(player.getInventory().getBoots())));
        equipmentList.add(Pair.of(EnumItemSlot.d, CraftItemStack.asNMSCopy(player.getInventory().getLeggings())));
        equipmentList.add(Pair.of(EnumItemSlot.e, CraftItemStack.asNMSCopy(player.getInventory().getChestplate())));
        equipmentList.add(Pair.of(EnumItemSlot.f, CraftItemStack.asNMSCopy(player.getInventory().getHelmet())));

        // Building the packet
        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(player.getEntityId(), equipmentList);

        // Sending the packet to observers
        for (Player otherPlayer : player.getWorld().getPlayers()) {
            PlayerConnection playerConnection = ((CraftPlayer) otherPlayer).getHandle().b;
            playerConnection.sendPacket(packet);
        }
    }
}
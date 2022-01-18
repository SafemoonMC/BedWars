package gg.mooncraft.minecraft.bedwars.game.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameTeam;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class ItemsUtilities {

    public static @NotNull Map<Material, AtomicInteger> getResourceItems(@NotNull Player player) {
        Map<Material, AtomicInteger> map = new HashMap<>();
        for (ItemStack itemStack : player.getInventory()) {
            if (itemStack == null) continue;
            if (itemStack.getType() == Material.DIAMOND || itemStack.getType() == Material.EMERALD || itemStack.getType() == Material.IRON_INGOT) {
                map.putIfAbsent(itemStack.getType(), new AtomicInteger());
                map.get(itemStack.getType()).addAndGet(itemStack.getAmount());
            }
        }
        return map;
    }

    public static @NotNull ItemStack createPureItem(@NotNull Material material) {
        ItemStack itemStack = new ItemStack(material);
        itemStack.addItemFlags(ItemFlag.values());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setUnbreakable(true);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static @NotNull ItemStack createWoolitem(@NotNull GameTeam gameTeam) {
        switch (gameTeam) {
            case BLUE -> {
                return createPureItem(Material.BLUE_WOOL);
            }
            case YELLOW -> {
                return createPureItem(Material.YELLOW_WOOL);
            }
            case RED -> {
                return createPureItem(Material.RED_WOOL);
            }
            case AQUA -> {
                return createPureItem(Material.CYAN_WOOL);
            }
            case GRAY -> {
                return createPureItem(Material.GRAY_WOOL);
            }
            case GREEN -> {
                return createPureItem(Material.GREEN_WOOL);
            }
            case PINK -> {
                return createPureItem(Material.PURPLE_WOOL);
            }
            default -> {
                return createPureItem(Material.WHITE_WOOL);
            }
        }
    }

    public static @NotNull ItemStack createGlassItem(@NotNull GameTeam gameTeam) {
        switch (gameTeam) {
            case BLUE -> {
                return createPureItem(Material.BLUE_STAINED_GLASS);
            }
            case YELLOW -> {
                return createPureItem(Material.YELLOW_STAINED_GLASS);
            }
            case RED -> {
                return createPureItem(Material.RED_STAINED_GLASS);
            }
            case AQUA -> {
                return createPureItem(Material.CYAN_STAINED_GLASS);
            }
            case GRAY -> {
                return createPureItem(Material.GRAY_STAINED_GLASS);
            }
            case GREEN -> {
                return createPureItem(Material.GREEN_STAINED_GLASS);
            }
            case PINK -> {
                return createPureItem(Material.PURPLE_STAINED_GLASS);
            }
            default -> {
                return createPureItem(Material.WHITE_STAINED_GLASS);
            }
        }
    }

    public static @NotNull ItemStack createArmorItem(@NotNull GameTeam gameTeam, @NotNull Material material) {
        ItemStack itemStack = createPureItem(material);
        itemStack.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        switch (gameTeam) {
            case BLUE -> {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
                leatherArmorMeta.setColor(Color.BLUE);
                itemStack.setItemMeta(leatherArmorMeta);
                return itemStack;
            }
            case YELLOW -> {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
                leatherArmorMeta.setColor(Color.YELLOW);
                itemStack.setItemMeta(leatherArmorMeta);
                return itemStack;
            }
            case RED -> {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
                leatherArmorMeta.setColor(Color.RED);
                itemStack.setItemMeta(leatherArmorMeta);
                return itemStack;
            }
            case AQUA -> {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
                leatherArmorMeta.setColor(Color.AQUA);
                itemStack.setItemMeta(leatherArmorMeta);
                return itemStack;
            }
            case GRAY -> {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
                leatherArmorMeta.setColor(Color.GRAY);
                itemStack.setItemMeta(leatherArmorMeta);
                return itemStack;
            }
            case GREEN -> {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
                leatherArmorMeta.setColor(Color.GREEN);
                itemStack.setItemMeta(leatherArmorMeta);
                return itemStack;
            }
            case PINK -> {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
                leatherArmorMeta.setColor(Color.FUCHSIA);
                itemStack.setItemMeta(leatherArmorMeta);
                return itemStack;
            }
            default -> {
                return itemStack;
            }
        }
    }
}
package gg.mooncraft.minecraft.bedwars.game.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameTeam;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class ItemsUtilities {

    public static @NotNull ItemStack createPureItem(@NotNull Material material) {
        ItemStack itemStack = new ItemStack(material);
        itemStack.addItemFlags(ItemFlag.values());
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
            case DARK_RED -> {
                return createPureItem(Material.RED_WOOL);
            }
            case DARK_AQUA -> {
                return createPureItem(Material.CYAN_WOOL);
            }
            case DARK_GRAY -> {
                return createPureItem(Material.GRAY_WOOL);
            }
            case DARK_GREEN -> {
                return createPureItem(Material.GREEN_WOOL);
            }
            case LIGHT_PURPLE -> {
                return createPureItem(Material.PURPLE_WOOL);
            }
            default -> {
                return createPureItem(Material.WHITE_WOOL);
            }
        }
    }

    public static boolean hasEnoughItems(@NotNull Player player, @NotNull Material material, int amount) {
        return player.getInventory().contains(material, amount);
    }
}
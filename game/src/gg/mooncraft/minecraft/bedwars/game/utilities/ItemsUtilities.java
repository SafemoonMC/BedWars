package gg.mooncraft.minecraft.bedwars.game.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.bukkit.Material;
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
}
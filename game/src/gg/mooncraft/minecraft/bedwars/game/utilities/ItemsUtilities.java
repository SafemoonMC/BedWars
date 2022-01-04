package gg.mooncraft.minecraft.bedwars.game.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class ItemsUtilities {

    public static @NotNull ItemStack createPureItem(@NotNull Material material) {
        ItemStack itemStack = new ItemStack(material);
        itemStack.addItemFlags(ItemFlag.values());
        return itemStack;
    }
}
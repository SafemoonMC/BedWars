package gg.mooncraft.minecraft.bedwars.game.items.persistence;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class PersistentItems {

    public static <T> void set(@NotNull ItemStack itemStack, @NotNull PersistentDataItem<T> persistentDataItem, T value) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer customItemTagContainer = itemMeta.getPersistentDataContainer();
        customItemTagContainer.set(persistentDataItem.getNamespacedKey(), persistentDataItem.getPersistentDataType(), value);
        itemStack.setItemMeta(itemMeta);
    }

    public static boolean has(@NotNull ItemStack itemStack, @NotNull PersistentDataItem<?> persistentDataItem) {
        if (!itemStack.hasItemMeta()) return false;
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer customItemTagContainer = itemMeta.getPersistentDataContainer();
        return customItemTagContainer.has(persistentDataItem.getNamespacedKey(), persistentDataItem.getPersistentDataType());
    }

    public static <T> @NotNull Optional<T> get(@NotNull ItemStack itemStack, @NotNull PersistentDataItem<T> persistentDataItem) {
        if (!itemStack.hasItemMeta()) return Optional.empty();
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer customItemTagContainer = itemMeta.getPersistentDataContainer();
        return Optional.ofNullable(customItemTagContainer.get(persistentDataItem.getNamespacedKey(), persistentDataItem.getPersistentDataType()));
    }
}
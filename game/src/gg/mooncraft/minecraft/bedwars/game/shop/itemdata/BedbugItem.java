package gg.mooncraft.minecraft.bedwars.game.shop.itemdata;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameTeam;
import gg.mooncraft.minecraft.bedwars.game.items.persistence.PersistentItems;

import java.util.Optional;
import java.util.UUID;

@Getter
@AllArgsConstructor
public final class BedbugItem {

    /*
    Fields
     */
    private final @NotNull UUID uniqueId;
    private final @NotNull GameTeam gameTeam;

    /*
    Methods
     */
    public @NotNull ItemStack update(@NotNull ItemStack itemStack) {
        PersistentItems.set(itemStack, BedbugItemData.INSTANCE, this);
        return itemStack;
    }

    /*
    Static Methods
     */
    public static boolean isValid(ItemStack itemStack) {
        return PersistentItems.has(itemStack, BedbugItemData.INSTANCE);
    }

    public static @NotNull Optional<BedbugItem> getFrom(@NotNull ItemStack itemStack) {
        return PersistentItems.get(itemStack, BedbugItemData.INSTANCE);
    }
}
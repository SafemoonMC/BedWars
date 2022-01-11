package gg.mooncraft.minecraft.bedwars.game.shop;

import lombok.Getter;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.options.OptionEntry;

@Getter
public final class ShopElementItem extends ShopElement {

    /*
    Fields
     */
    private final @NotNull ItemStack itemStack;

    /*
    Constructor
     */
    public ShopElementItem(@NotNull String display, @NotNull String description, @NotNull OptionEntry<Material, Integer> cost, @NotNull Material displayIcon, @NotNull ItemStack itemStack) {
        super(display, description, cost, displayIcon);
        this.itemStack = itemStack;
    }

    /*
    Override Methods
     */
    @Override
    public @NotNull ItemStack getIconItem(@NotNull Player player, @NotNull GameMatchPlayer gameMatchPlayer) {
        ItemStack itemStack = super.getIconItem(player, gameMatchPlayer);
        if (this.itemStack.getItemMeta().hasEnchants()) {
            itemStack.addUnsafeEnchantments(this.itemStack.getEnchantments());
            itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        itemStack.setAmount(this.itemStack.getAmount());
        return itemStack;
    }
}
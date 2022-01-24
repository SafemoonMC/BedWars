package gg.mooncraft.minecraft.bedwars.game.shop;

import lombok.Getter;

import net.kyori.adventure.text.Component;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.options.OptionEntry;

import java.util.List;
import java.util.function.Function;

@Getter
public final class ShopElementItemDynamic extends ShopElement {

    /*
    Fields
     */
    private final @NotNull Function<GameMatchPlayer, ItemStack> itemStackFunction;

    /*
    Constructor
     */
    public ShopElementItemDynamic(@NotNull String display, @NotNull String description, @NotNull OptionEntry<Material, Integer> cost, @NotNull Material displayIcon, @NotNull Function<GameMatchPlayer, ItemStack> itemStackFunction) {
        super(display, description, cost, displayIcon);
        this.itemStackFunction = itemStackFunction;
    }

    /*
    Override Methods
     */
    @Override
    public @NotNull ItemStack getIconItem(@NotNull Player player, @NotNull GameMatchPlayer gameMatchPlayer) {
        ItemStack originalItemStack = this.itemStackFunction.apply(gameMatchPlayer);
        ItemStack itemStack = super.getIconItem(player, gameMatchPlayer);
        if (originalItemStack.getItemMeta().hasEnchants()) {
            itemStack.addUnsafeEnchantments(originalItemStack.getEnchantments());
            itemStack.removeItemFlags(ItemFlag.HIDE_ENCHANTS);

            ItemMeta itemMeta = itemStack.getItemMeta();
            List<Component> lore = itemMeta.lore();
            lore.add(0, Component.empty());
            itemMeta.lore(lore);
            itemStack.setItemMeta(itemMeta);
        }
        itemStack.setAmount(originalItemStack.getAmount());
        return itemStack;
    }

}
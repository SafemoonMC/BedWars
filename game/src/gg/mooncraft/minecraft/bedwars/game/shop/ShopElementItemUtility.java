package gg.mooncraft.minecraft.bedwars.game.shop;

import lombok.Getter;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.items.ItemStackCreator;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.options.OptionEntry;
import gg.mooncraft.minecraft.bedwars.game.shop.itemdata.BedbugItem;

import java.util.List;
import java.util.function.Function;

@Getter
public final class ShopElementItemUtility extends ShopElement {

    /*
    Fields
     */
    private final @NotNull Function<GameMatchPlayer, ItemStack> itemStackFunction;

    /*
    Constructor
     */
    public ShopElementItemUtility(@NotNull String display, @NotNull String description, @NotNull OptionEntry<Material, Integer> cost, @NotNull Material displayIcon, @NotNull Function<GameMatchPlayer, ItemStack> itemStackFunction) {
        super(display, description, cost, displayIcon);
        this.itemStackFunction = itemStackFunction;
    }

    /*
    Methods
     */
    public @NotNull ItemStack getItemStack(@NotNull GameMatchPlayer gameMatchPlayer) {
        ItemStack clone = this.itemStackFunction.apply(gameMatchPlayer).clone();
        return new BedbugItem(gameMatchPlayer.getUniqueId(), gameMatchPlayer.getParent().getGameTeam()).update(ItemStackCreator.using(clone).meta().display(ChatColor.WHITE + getDisplay()).lore(List.of(getDescription())).stack().create());
    }
}
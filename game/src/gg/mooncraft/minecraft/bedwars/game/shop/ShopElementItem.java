package gg.mooncraft.minecraft.bedwars.game.shop;

import lombok.Getter;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.items.ItemStackCreator;
import gg.mooncraft.minecraft.bedwars.game.match.options.OptionEntry;

import java.util.List;

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
}
package gg.mooncraft.minecraft.bedwars.game.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.items.ItemStackCreator;
import gg.mooncraft.minecraft.bedwars.game.match.options.OptionEntry;

import java.util.List;

@Getter
@AllArgsConstructor
public class ShopElement {

    /*
    Fields
     */
    private final @NotNull String display;
    private final @NotNull String description;
    private final @NotNull OptionEntry<Material, Integer> cost;
    private final @NotNull Material displayIcon;

    /*
    Methods
     */
    public @NotNull ItemStack getIconItem() {
        return ItemStackCreator.using(getDisplayIcon()).meta().display(getDisplay()).lore(List.of(getDescription())).stack().create();
    }
}
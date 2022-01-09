package gg.mooncraft.minecraft.bedwars.game.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.items.ItemStackCreator;
import gg.mooncraft.minecraft.bedwars.game.match.options.OptionEntry;
import gg.mooncraft.minecraft.bedwars.game.utilities.ItemsUtilities;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public class ShopElement {

    /*
    Fields
     */
    private final @NotNull String display;
    private final @NotNull String description;
    private final @NotNull OptionEntry<Material, Integer> costEntry;
    private final @NotNull Material displayIcon;

    /*
    Methods
     */
    public @NotNull ItemStack getIconItem(@NotNull Player player) {
        String color = ItemsUtilities.hasEnoughItems(player, this.costEntry.getKey(), this.costEntry.getValue()) ? "&a" : "&c";
        String lastLine = ItemsUtilities.hasEnoughItems(player, this.costEntry.getKey(), this.costEntry.getValue()) ? "&aClick to purchase!" : "&cYou can't afford this.";
        return ItemStackCreator.using(getDisplayIcon())
                .meta()
                .display(color + getDisplay())
                .lore(Arrays.asList(
                        "&7Cost: " + getCost(),
                        "",
                        getDescription(),
                        "",
                        lastLine
                ))
                .stack().create();
    }

    private @NotNull String getCost() {
        String color = this.costEntry.getKey() == Material.IRON_INGOT ? "&f" : this.costEntry.getKey() == Material.GOLD_INGOT ? "&6" : this.costEntry.getKey() == Material.DIAMOND ? "&b" : "&2";
        return color + this.costEntry.getValue() + " " + WordUtils.capitalizeFully(this.costEntry.getKey().name().replaceAll("_", " "));
    }
}
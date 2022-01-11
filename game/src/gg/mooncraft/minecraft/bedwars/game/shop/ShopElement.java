package gg.mooncraft.minecraft.bedwars.game.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.items.ItemStackCreator;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.options.OptionEntry;
import gg.mooncraft.minecraft.bedwars.game.utilities.ItemsUtilities;
import gg.mooncraft.minecraft.bedwars.game.utilities.StringUtilities;

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
    public @NotNull ItemStack getIconItem(@NotNull Player player, @NotNull GameMatchPlayer gameMatchPlayer) {
        String color = ItemsUtilities.hasEnoughItems(player, this.costEntry.getKey(), this.costEntry.getValue()) ? "&a" : "&c";
        String lastLine = ItemsUtilities.hasEnoughItems(player, this.costEntry.getKey(), this.costEntry.getValue()) ? "&eClick to purchase!" : "&cYou can't afford this.";
        return ItemStackCreator.using(ItemsUtilities.createPureItem(getDisplayIcon()))
                .meta()
                .display(color + getDisplay())
                .lore(StringUtilities.isBlankOrEmpty(getDescription()) ? Arrays.asList("&7Cost: " + getCost(), "", lastLine) : Arrays.asList("&7Cost: " + getCost(), "", getDescription(), "", lastLine))
                .stack().create();
    }

    private @NotNull String getCost() {
        String material = this.costEntry.getKey() == Material.IRON_INGOT ? "Iron" : this.costEntry.getKey() == Material.GOLD_INGOT ? "Gold" : this.costEntry.getKey() == Material.DIAMOND ? "Diamond" : "Emerald";
        String color = this.costEntry.getKey() == Material.IRON_INGOT ? "&f" : this.costEntry.getKey() == Material.GOLD_INGOT ? "&6" : this.costEntry.getKey() == Material.DIAMOND ? "&b" : "&2";
        return color + this.costEntry.getValue() + " " + material;
    }
}
package gg.mooncraft.minecraft.bedwars.game.shop;

import lombok.Getter;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.items.ItemStackCreator;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.options.OptionEntry;
import gg.mooncraft.minecraft.bedwars.game.utilities.ItemsUtilities;
import gg.mooncraft.minecraft.bedwars.game.utilities.StringUtilities;

import java.util.Arrays;

@Getter
public final class ShopElementItemPermanent extends ShopElement {

    /*
    Fields
     */
    private final PermanentElement permanentElement;

    /*
    Constructor
     */
    public ShopElementItemPermanent(@NotNull String display, @NotNull String description, @NotNull OptionEntry<Material, Integer> cost, @NotNull Material displayIcon, @NotNull PermanentElement permanentElement) {
        super(display, description, cost, displayIcon);
        this.permanentElement = permanentElement;
    }

    /*
    Override Methods
     */
    @Override
    public @NotNull ItemStack getIconItem(@NotNull Player player, @NotNull GameMatchPlayer gameMatchPlayer) {
        String color = player.getInventory().contains(getCostEntry().getKey(), getCostEntry().getValue()) ? "&a" : "&c";
        String lastLine = gameMatchPlayer.getPermanentElementList().contains(this.permanentElement) ? "&aAlready bought!" : player.getInventory().contains(getCostEntry().getKey(), getCostEntry().getValue()) ? "&eClick to purchase!" : "&cYou can't afford this.";
        ItemStack itemStack = ItemStackCreator.using(ItemsUtilities.createPureItem(getDisplayIcon()))
                .meta()
                .display(color + getDisplay())
                .lore(StringUtilities.isBlankOrEmpty(getDescription()) ? Arrays.asList("&7Cost: " + getCost(), "", lastLine) : Arrays.asList("&7Cost: " + getCost(), "", getDescription(), "", lastLine))
                .stack().create();
        if (gameMatchPlayer.getParent().getUpgradeTier("armor") != 0) {
            itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 0);
            itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return itemStack;
    }

}
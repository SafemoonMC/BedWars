package gg.mooncraft.minecraft.bedwars.game.upgrades;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.ChatColor;
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
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public final class UpgradeElement {

    private final @NotNull UpgradeCategory group;
    private final int tier;
    private final @NotNull String description;
    private final @NotNull OptionEntry<Material, Integer> costEntry;
    private final @NotNull Material iconMaterial;

    /*
    Methods
     */
    public @NotNull ItemStack getIconItem(@NotNull Player player, @NotNull GameMatchPlayer gameMatchPlayer) {
        String color = player.getInventory().contains(this.costEntry.getKey(), this.costEntry.getValue()) ? "&a" : "&c";
        String lastLine = player.getInventory().contains(this.costEntry.getKey(), this.costEntry.getValue()) ? "&eClick to purchase!" : "&cYou can't afford this.";
        return ItemStackCreator.using(ItemsUtilities.createPureItem(getIconMaterial()))
                .meta()
                .display(color + group.getDisplay())
                .lore(StringUtilities.isBlankOrEmpty(getDescription()) ?
                        Arrays.asList(group.getDescription(), "", "&7Cost: " + getCost(), "", gameMatchPlayer.getParent().getUpgradeTier(group.getIdentifier()) == group.getUpgradeElementList().size() ? group.isSingleTier() ? "&aAlready Unlocked!" : "&aMAX TIER!" : lastLine) :
                        Arrays.asList(group.getDescription(), "", group.getUpgradeElementList().stream().map(upgradeElement -> String.format("%sTier %d: %s. %s", getTierColor(upgradeElement, gameMatchPlayer), upgradeElement.getTier(), upgradeElement.getDescription(), upgradeElement.getCost())).collect(Collectors.joining("\n")), "", gameMatchPlayer.getParent().getUpgradeTier(group.getIdentifier()) == group.getUpgradeElementList().size() ? group.isSingleTier() ? "&aAlready Unlocked!" : "&aMAX TIER!" : lastLine))
                .stack().create();
    }

    private @NotNull String getCost() {
        String material = this.costEntry.getKey() == Material.IRON_INGOT ? "Iron" : this.costEntry.getKey() == Material.GOLD_INGOT ? "Gold" : this.costEntry.getKey() == Material.DIAMOND ? "Diamond" : "Emerald";
        String color = this.costEntry.getKey() == Material.IRON_INGOT ? "&f" : this.costEntry.getKey() == Material.GOLD_INGOT ? "&6" : this.costEntry.getKey() == Material.DIAMOND ? "&b" : "&2";
        return color + this.costEntry.getValue() + " " + material;
    }

    private @NotNull String getTierColor(@NotNull UpgradeElement upgradeElement, @NotNull GameMatchPlayer gameMatchPlayer) {
        String color = gameMatchPlayer.getParent().getUpgradeTier(this.group.getIdentifier()) < upgradeElement.tier ? ChatColor.GRAY.toString() : ChatColor.GREEN.toString();
        return color;
    }
}
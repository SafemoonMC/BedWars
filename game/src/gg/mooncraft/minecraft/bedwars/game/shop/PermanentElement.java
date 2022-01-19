package gg.mooncraft.minecraft.bedwars.game.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.items.ItemStackCreator;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.utilities.ItemsUtilities;

@Getter
@AllArgsConstructor
public enum PermanentElement {

    ST_ARMOR("armor") {
        @Override
        public void apply(@NotNull GameMatchPlayer gameMatchPlayer) {
            gameMatchPlayer.getArmor()[0] = ItemsUtilities.makeUnbreakable(ItemStackCreator.using(Material.CHAINMAIL_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, gameMatchPlayer.getParent().getUpgradeTier("armor")).amount(1).create());
            gameMatchPlayer.getArmor()[1] = ItemsUtilities.makeUnbreakable(ItemStackCreator.using(Material.CHAINMAIL_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, gameMatchPlayer.getParent().getUpgradeTier("armor")).amount(1).create());
            gameMatchPlayer.getPlayer().ifPresent(player -> player.getInventory().setArmorContents(gameMatchPlayer.getArmor()));
        }
    },
    ND_ARMOR("armor") {
        @Override
        public void apply(@NotNull GameMatchPlayer gameMatchPlayer) {
            gameMatchPlayer.getArmor()[0] = ItemsUtilities.makeUnbreakable(ItemStackCreator.using(Material.IRON_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, gameMatchPlayer.getParent().getUpgradeTier("armor")).amount(1).create());
            gameMatchPlayer.getArmor()[1] = ItemsUtilities.makeUnbreakable(ItemStackCreator.using(Material.IRON_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, gameMatchPlayer.getParent().getUpgradeTier("armor")).amount(1).create());
            gameMatchPlayer.getPlayer().ifPresent(player -> player.getInventory().setArmorContents(gameMatchPlayer.getArmor()));
        }
    },
    RD_ARMOR("armor") {
        @Override
        public void apply(@NotNull GameMatchPlayer gameMatchPlayer) {
            gameMatchPlayer.getArmor()[0] = ItemsUtilities.makeUnbreakable(ItemStackCreator.using(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, gameMatchPlayer.getParent().getUpgradeTier("armor")).amount(1).create());
            gameMatchPlayer.getArmor()[1] = ItemsUtilities.makeUnbreakable(ItemStackCreator.using(Material.DIAMOND_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, gameMatchPlayer.getParent().getUpgradeTier("armor")).amount(1).create());
            gameMatchPlayer.getPlayer().ifPresent(player -> player.getInventory().setArmorContents(gameMatchPlayer.getArmor()));
        }
    },
    SHEARS("shears") {
        @Override
        public void apply(@NotNull GameMatchPlayer gameMatchPlayer) {
            gameMatchPlayer.getUtility().setType(Material.SHEARS);
            ItemMeta itemMeta = gameMatchPlayer.getUtility().getItemMeta();
            itemMeta.setUnbreakable(true);
            gameMatchPlayer.getUtility().setItemMeta(itemMeta);
            gameMatchPlayer.getPlayer().ifPresent(player -> player.getInventory().addItem(gameMatchPlayer.getUtility()));
        }
    };

    /*
    Fields
     */
    private final @NotNull String id;

    /*
    Methods
     */
    public abstract void apply(@NotNull GameMatchPlayer gameMatchPlayer);
}
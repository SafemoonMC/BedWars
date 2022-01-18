package gg.mooncraft.minecraft.bedwars.game.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.items.ItemStackCreator;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;

@Getter
@AllArgsConstructor
public enum PermanentElement {

    ST_ARMOR("armor") {
        @Override
        public void apply(@NotNull GameMatchPlayer gameMatchPlayer) {
            gameMatchPlayer.getArmor()[0] = ItemStackCreator.using(Material.CHAINMAIL_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, gameMatchPlayer.getParent().getUpgradeTier("armor")).amount(1).create();
            gameMatchPlayer.getArmor()[1] = ItemStackCreator.using(Material.CHAINMAIL_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, gameMatchPlayer.getParent().getUpgradeTier("armor")).amount(1).create();
        }
    },
    ND_ARMOR("armor") {
        @Override
        public void apply(@NotNull GameMatchPlayer gameMatchPlayer) {
            gameMatchPlayer.getArmor()[0] = ItemStackCreator.using(Material.IRON_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, gameMatchPlayer.getParent().getUpgradeTier("armor")).amount(1).create();
            gameMatchPlayer.getArmor()[1] = ItemStackCreator.using(Material.IRON_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, gameMatchPlayer.getParent().getUpgradeTier("armor")).amount(1).create();
        }
    },
    RD_ARMOR("armor") {
        @Override
        public void apply(@NotNull GameMatchPlayer gameMatchPlayer) {
            gameMatchPlayer.getArmor()[0] = ItemStackCreator.using(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, gameMatchPlayer.getParent().getUpgradeTier("armor")).amount(1).create();
            gameMatchPlayer.getArmor()[1] = ItemStackCreator.using(Material.DIAMOND_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, gameMatchPlayer.getParent().getUpgradeTier("armor")).amount(1).create();
        }
    },
    SHEARS("shears") {
        @Override
        public void apply(@NotNull GameMatchPlayer gameMatchPlayer) {
            gameMatchPlayer.getUtility().setType(Material.SHEARS);
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
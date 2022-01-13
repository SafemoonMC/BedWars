package gg.mooncraft.minecraft.bedwars.game.menu;

import lombok.Getter;

import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.GameConstants;
import gg.mooncraft.minecraft.bedwars.game.items.ItemStackCreator;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchTeam;
import gg.mooncraft.minecraft.bedwars.game.shop.Shop;
import gg.mooncraft.minecraft.bedwars.game.shop.ShopCategory;
import gg.mooncraft.minecraft.bedwars.game.shop.ShopElement;
import gg.mooncraft.minecraft.bedwars.game.shop.ShopElementItem;
import gg.mooncraft.minecraft.bedwars.game.shop.ShopElementItemDynamic;
import gg.mooncraft.minecraft.bedwars.game.shop.ShopElementItemUtility;
import gg.mooncraft.minecraft.bedwars.game.shop.Shops;
import gg.mooncraft.minecraft.bedwars.game.utilities.DisplayUtilities;
import gg.mooncraft.minecraft.bedwars.game.utilities.ItemsUtilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class ShopMenu implements ShopInterface {

    /*
    Constants
     */
    private static final int[] CATEGORIES = {
            1, 2, 3, 4, 5, 6, 7
    };
    private static final int[] ELEMENTS = {
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43,
    };
    private static final int[] DESIGN = {
            0, 8,
            9, 10, 11, 12, 13, 14, 15, 16, 17,
            18, 26,
            27, 35,
            36, 44,
            45, 46, 47, 48, 49, 50, 51, 52, 53
    };

    /*
    Fields
     */
    private final @NotNull Player player;
    private final @NotNull GameMatch gameMatch;
    private final @NotNull GameMatchPlayer gameMatchPlayer;
    private final @NotNull Inventory inventory;
    private final @NotNull Map<Integer, ShopCategory> categoryMap;
    private final @NotNull Map<Integer, ShopElement> elementMap;

    private int selectedCategorySlot = -1;

    /*
    Constructor
     */
    public ShopMenu(@NotNull Player player, @NotNull GameMatch gameMatch, @NotNull GameMatchPlayer gameMatchPlayer) {
        this.player = player;
        this.gameMatch = gameMatch;
        this.gameMatchPlayer = gameMatchPlayer;
        this.inventory = Bukkit.createInventory(this, 54, Component.text(GameConstants.SHOP_ITEMS_TITLE));
        this.categoryMap = new HashMap<>();
        this.elementMap = new HashMap<>();
        // Load and select default category
        load();
        select(1);
        // Place design items which won't be updated anymore
        Arrays.stream(DESIGN).forEach(slot -> this.inventory.setItem(slot, ItemStackCreator.using(Material.BLACK_STAINED_GLASS_PANE).meta().display("&4").stack().create()));
    }

    /*
    Methods
     */
    private void load() {
        // Clear old references
        this.categoryMap.clear();
        this.elementMap.clear();
        Arrays.stream(ELEMENTS).forEach(slot -> this.inventory.setItem(slot, null));

        // Place categories item
        int index = 0;
        Shop shop = Shops.getShop(gameMatch.getGameMode());
        for (ShopCategory shopCategory : shop.getCategoryList()) {
            int slot = CATEGORIES[index];
            ItemStack itemStack = shopCategory.getIconItem().clone();
            itemStack.addItemFlags(ItemFlag.values());
            this.inventory.setItem(slot, itemStack);
            this.categoryMap.put(slot, shopCategory);

            index++;
        }
    }

    private void select(int categorySlot) {
        if (categorySlot < 0 || categorySlot > 7) return;
        if (selectedCategorySlot != -1) {
            load();
        }

        // Glow the selected category
        ItemStack itemStack = inventory.getItem(categorySlot);
        itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

        // Place the elements
        int index = 0;
        ShopCategory shopCategory = categoryMap.get(categorySlot);
        for (ShopElement shopElement : shopCategory.getElementList()) {
            int slot = ELEMENTS[index];
            this.inventory.setItem(slot, shopElement.getIconItem(player, gameMatchPlayer));
            this.elementMap.put(slot, shopElement);
            index++;
        }

        this.selectedCategorySlot = categorySlot;
    }

    /*
    Override Methods
     */
    @Override
    public void onClick(int slot, @NotNull Player player, @NotNull GameMatchPlayer gameMatchPlayer, @NotNull GameMatchTeam gameMatchTeam) {
        if (this.categoryMap.containsKey(slot)) {
            select(slot);
            return;
        }

        ShopCategory shopCategory = this.categoryMap.get(selectedCategorySlot);
        if (this.elementMap.containsKey(slot)) {
            ShopElement shopElement = this.elementMap.get(slot);

            if (!player.getInventory().contains(shopElement.getCostEntry().getKey(), shopElement.getCostEntry().getValue())) {
                player.sendMessage(GameConstants.MESSAGE_SHOP_CANNOT_AFFORD);
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }

            ItemStack costItem = ItemsUtilities.createPureItem(shopElement.getCostEntry().getKey());
            costItem.setAmount(shopElement.getCostEntry().getValue());
            if (shopElement instanceof ShopElementItem shopElementItem) {
                player.getInventory().removeItemAnySlot(costItem);
                player.getInventory().addItem(shopElementItem.getItemStack());
                player.sendMessage(GameConstants.MESSAGE_SHOP_BUY.replaceAll("%shop-item%", DisplayUtilities.getDisplay(shopElementItem.getItemStack())));
            }
            if (shopElement instanceof ShopElementItemDynamic shopElementItemDynamic) {
                player.getInventory().removeItemAnySlot(costItem);
                player.getInventory().addItem(shopElementItemDynamic.getItemStackFunction().apply(gameMatchPlayer));
                player.sendMessage(GameConstants.MESSAGE_SHOP_BUY.replaceAll("%shop-item%", DisplayUtilities.getDisplay(shopElementItemDynamic.getItemStackFunction().apply(gameMatchPlayer))));
            }
            if (shopElement instanceof ShopElementItemUtility shopElementItemUtility) {
                player.getInventory().removeItemAnySlot(costItem);
                player.getInventory().addItem(shopElementItemUtility.getItemStack(gameMatchPlayer));
                player.sendMessage(GameConstants.MESSAGE_SHOP_BUY.replaceAll("%shop-item%", DisplayUtilities.getDisplay(shopElementItemUtility.getItemStackFunction().apply(gameMatchPlayer))));
            }

            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1, 1);
            select(selectedCategorySlot);
        }
    }
}
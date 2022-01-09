package gg.mooncraft.minecraft.bedwars.game.menu;

import lombok.Getter;

import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.GameConstants;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchTeam;
import gg.mooncraft.minecraft.bedwars.game.shop.Shop;
import gg.mooncraft.minecraft.bedwars.game.shop.ShopCategory;
import gg.mooncraft.minecraft.bedwars.game.shop.ShopElement;
import gg.mooncraft.minecraft.bedwars.game.shop.Shops;

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
    private final @NotNull GameMatch gameMatch;
    private final @NotNull Inventory inventory;
    private final @NotNull Map<Integer, ShopCategory> categoryMap;

    private int selectedCategorySlot = -1;

    /*
    Constructor
     */
    public ShopMenu(@NotNull GameMatch gameMatch) {
        this.gameMatch = gameMatch;
        this.inventory = Bukkit.createInventory(this, 54, Component.text(GameConstants.SHOP_ITEMS_TITLE));
        this.categoryMap = new HashMap<>();
        load();
        select(1);
    }

    /*
    Methods
     */
    private void load() {
        this.categoryMap.clear();

        Shop shop = Shops.getShop(gameMatch.getGameMode());
        int index = 0;
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
        if (categorySlot < 0 || categorySlot > 6) return;
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
            this.inventory.setItem(slot, shopElement.getIconItem());
            index++;
        }

        this.selectedCategorySlot = categorySlot;
    }

    /*
    Override Methods
     */
    @Override
    public void onClick(int slot, @NotNull Player player, @NotNull GameMatchPlayer gameMatchPlayer, @NotNull GameMatchTeam gameMatchTeam) {
    }
}
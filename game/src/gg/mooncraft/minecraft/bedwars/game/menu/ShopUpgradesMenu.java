package gg.mooncraft.minecraft.bedwars.game.menu;

import lombok.Getter;

import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.GameConstants;
import gg.mooncraft.minecraft.bedwars.game.items.ItemStackCreator;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchTeam;
import gg.mooncraft.minecraft.bedwars.game.upgrades.TeamUpgrades;
import gg.mooncraft.minecraft.bedwars.game.upgrades.UpgradeCategory;
import gg.mooncraft.minecraft.bedwars.game.upgrades.UpgradeElement;
import gg.mooncraft.minecraft.bedwars.game.upgrades.UpgradeSet;
import gg.mooncraft.minecraft.bedwars.game.utilities.ItemsUtilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class ShopUpgradesMenu implements ShopInterface {

    /*
    Constants
     */
    private static final int[] UPGRADES = {
            10, 11, 12,
            19, 20, 21
    };
    private static final int[] TRAPS = {
            14, 15, 16,
            23
    };
    private static final int[] QUEUE = {
            39, 40, 41
    };

    /*
    Fields
     */
    private final @NotNull Player player;
    private final @NotNull GameMatch gameMatch;
    private final @NotNull GameMatchPlayer gameMatchPlayer;
    private final @NotNull Inventory inventory;
    private final @NotNull Map<Integer, UpgradeElement> upgradeMap;

    /*
    Constructor
     */
    public ShopUpgradesMenu(@NotNull Player player, @NotNull GameMatch gameMatch, @NotNull GameMatchPlayer gameMatchPlayer) {
        this.player = player;
        this.gameMatch = gameMatch;
        this.gameMatchPlayer = gameMatchPlayer;
        this.inventory = Bukkit.createInventory(this, 54, Component.text(GameConstants.SHOP_UPGRADES_TITLE));
        this.upgradeMap = new HashMap<>();

        // Load and select default category
        load();

        // Place design items which won't be updated anymore
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack != null) continue;
            this.inventory.setItem(i, ItemStackCreator.using(Material.BLACK_STAINED_GLASS_PANE).meta().display("&4").stack().create());
        }
    }

    /*
    Methods
     */
    private void load() {
        this.upgradeMap.clear();

        // Clear old references
        Arrays.stream(UPGRADES).forEach(slot -> this.inventory.setItem(slot, null));
        Arrays.stream(TRAPS).forEach(slot -> this.inventory.setItem(slot, null));
        Arrays.stream(QUEUE).forEach(slot -> this.inventory.setItem(slot, null));

        // Place upgrades group
        int index = 0;
        UpgradeSet upgradeSet = TeamUpgrades.UPGRADE_SET;
        for (UpgradeCategory upgradeCategory : upgradeSet.getGroupList()) {
            int slot = UPGRADES[index];
            upgradeCategory.getUpgrade(this.gameMatchPlayer.getParent().getUpgradeTier(upgradeCategory.getIdentifier()) + 1).ifPresentOrElse(upgradeElement -> {
                ItemStack itemStack = upgradeElement.getIconItem(player, gameMatchPlayer).clone();
                this.inventory.setItem(slot, itemStack);
                this.upgradeMap.put(slot, upgradeElement);
            }, () -> {
                ItemStack itemStack = upgradeCategory.getUpgrade(this.gameMatchPlayer.getParent().getUpgradeTier(upgradeCategory.getIdentifier())).map(upgradeElement -> upgradeElement.getIconItem(player, gameMatchPlayer)).orElse(new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
                this.inventory.setItem(slot, itemStack);
            });
            index++;
        }
    }

    /*
    Override Methods
     */
    @Override
    public void onClick(int slot, @NotNull Player player, @NotNull GameMatchPlayer gameMatchPlayer, @NotNull GameMatchTeam gameMatchTeam) {
        if (this.upgradeMap.containsKey(slot)) {
            UpgradeElement upgradeElement = this.upgradeMap.get(slot);
            if (!player.getInventory().contains(upgradeElement.getCostEntry().getKey(), upgradeElement.getCostEntry().getValue())) {
                player.sendMessage(GameConstants.MESSAGE_SHOP_CANNOT_AFFORD);
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }

            ItemStack costItem = ItemsUtilities.createPureItem(upgradeElement.getCostEntry().getKey());
            costItem.setAmount(upgradeElement.getCostEntry().getValue());

            player.getInventory().removeItemAnySlot(costItem);
            gameMatchPlayer.getParent().incrementUpgrade(upgradeElement.getGroup().getIdentifier());
            player.sendMessage(GameConstants.MESSAGE_SHOP_BUY.replaceAll("%shop-item%", upgradeElement.getGroup().getDisplay()));

            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1, 1);
            load();
        }
    }
}
package gg.mooncraft.minecraft.bedwars.game.handlers.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.menu.ShopInterface;

public class MenuListeners implements Listener {

    /*
    Constructor
     */
    public MenuListeners() {
        Bukkit.getPluginManager().registerEvents(this, BedWarsPlugin.getInstance());
    }

    /*
    Handlers
     */
    @EventHandler
    public void on(@NotNull InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null) return;
        Inventory inventory = e.getInventory();

        if (!(inventory.getHolder() instanceof ShopInterface shopInterface)) return;
        e.setCancelled(true);
        if (!e.getInventory().equals(e.getClickedInventory())) return;

        BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player)
                .flatMap(gameMatch -> gameMatch.getDataOf(player))
                .ifPresent(gameMatchPlayer -> shopInterface.onClick(e.getSlot(), player, gameMatchPlayer, gameMatchPlayer.getParent()));
    }
}
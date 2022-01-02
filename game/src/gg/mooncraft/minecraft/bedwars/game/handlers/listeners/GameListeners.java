package gg.mooncraft.minecraft.bedwars.game.handlers.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;

public class GameListeners implements Listener {

    /*
    Constructor
     */
    public GameListeners() {
        Bukkit.getPluginManager().registerEvents(this, BedWarsPlugin.getInstance());
    }

    /*
    Handlers
     */
    @EventHandler
    public void on(@NotNull PlayerArmorStandManipulateEvent e) {
        Player player = e.getPlayer();
        BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player).ifPresent(gameMatch -> {
            e.setCancelled(true);
        });
    }

    @EventHandler
    public void on(@NotNull CraftItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void on(@NotNull FoodLevelChangeEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        e.setCancelled(true);
    }
}
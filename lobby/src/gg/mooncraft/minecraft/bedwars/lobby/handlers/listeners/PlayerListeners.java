package gg.mooncraft.minecraft.bedwars.lobby.handlers.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.lobby.BedWarsPlugin;

public class PlayerListeners implements Listener {

    /*
    Constructor
     */
    public PlayerListeners() {
        Bukkit.getPluginManager().registerEvents(this, BedWarsPlugin.getInstance());
    }

    /*
    Handlers
     */
    @EventHandler
    public void on(@NotNull PlayerJoinEvent e) {
        Player player = e.getPlayer();
        BedWarsPlugin.getInstance().getUserFactory().loadUser(player);
    }

    @EventHandler
    public void on(@NotNull PlayerQuitEvent e) {
        Player player = e.getPlayer();
        BedWarsPlugin.getInstance().getUserFactory().unloadUser(player);
    }

    @EventHandler
    public void on(@NotNull PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
            return;
        }
        if (e.getTo().getBlockY() <= 0) {
            player.teleport(player.getWorld().getSpawnLocation());
        }
    }
}
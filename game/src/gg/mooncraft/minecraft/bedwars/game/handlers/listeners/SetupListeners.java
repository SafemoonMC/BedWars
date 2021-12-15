package gg.mooncraft.minecraft.bedwars.game.handlers.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;

public final class SetupListeners implements Listener {

    /*
    Constructor
     */
    public SetupListeners() {
        Bukkit.getPluginManager().registerEvents(this, BedWarsPlugin.getInstance());
    }

    /*
    Handlers
     */
    @EventHandler
    public void on(@NotNull PlayerQuitEvent e) {
        Player player = e.getPlayer();
        BedWarsPlugin.getInstance().getSetupManager().getMapBuilder(player).ifPresent(mapBuilder -> {
            mapBuilder.cancel();
            BedWarsPlugin.getInstance().getSetupManager().stopSetup(mapBuilder);
        });
    }
}
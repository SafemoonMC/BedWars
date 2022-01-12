package gg.mooncraft.minecraft.bedwars.game.handlers.listeners.items;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;

public class UtilityListeners implements Listener {

    /*
    Constructor
     */
    public UtilityListeners() {
        Bukkit.getPluginManager().registerEvents(this, BedWarsPlugin.getInstance());
    }

    
}
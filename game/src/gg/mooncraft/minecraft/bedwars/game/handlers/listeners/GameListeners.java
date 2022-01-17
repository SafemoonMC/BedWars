package gg.mooncraft.minecraft.bedwars.game.handlers.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
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
    public void on(@NotNull PlayerItemConsumeEvent e) {
        if (e.getItem().getType() == Material.MILK_BUCKET) {
            e.setReplacement(new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void on(@NotNull PlayerArmorStandManipulateEvent e) {
        Player player = e.getPlayer();
        BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player).ifPresent(gameMatch -> e.setCancelled(true));
    }

    @EventHandler
    public void on(@NotNull EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Villager) {
            e.setCancelled(true);
        }
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

    @EventHandler
    public void on(@NotNull PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType().name().contains("BED")) {
            e.setCancelled(true);
        }
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getItem() != null && e.getItem().getType() == Material.WATER_BUCKET) {
            e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getItem().getAmount() - 1);
        }
    }
}
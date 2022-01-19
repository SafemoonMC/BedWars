package gg.mooncraft.minecraft.bedwars.game.handlers.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.PlayerStatus;

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

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(@NotNull AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player)
                .flatMap(gameMatch -> gameMatch.getDataOf(player))
                .ifPresent(gameMatchPlayer -> {
                    if (gameMatchPlayer.getPlayerStatus() == PlayerStatus.RESPAWNING || gameMatchPlayer.getPlayerStatus() == PlayerStatus.SPECTATING) {
                        e.setCancelled(true);
                    }
                });
    }

    @EventHandler
    public void on(@NotNull PlayerDropItemEvent e) {
        ItemStack itemStack = e.getItemDrop().getItemStack();
        if (itemStack.getType().name().contains("BOOTS") || itemStack.getType().name().contains("LEGGINGS") || itemStack.getType().name().contains("CHESTPLATE") || itemStack.getType().name().contains("HELMET") || itemStack.getType().name().contains("AXE") || itemStack.getType().name().contains("PICKAXE") || itemStack.getType().name().contains("SHEARS")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void on(@NotNull InventoryClickEvent e) {
        if (e.getSlotType() == InventoryType.SlotType.ARMOR) {
            e.setCancelled(true);
        }
    }
}
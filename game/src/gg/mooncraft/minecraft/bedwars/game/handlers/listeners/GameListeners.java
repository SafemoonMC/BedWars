package gg.mooncraft.minecraft.bedwars.game.handlers.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.GameConstants;
import gg.mooncraft.minecraft.bedwars.game.utilities.InvisibilityUtilities;

import java.util.concurrent.TimeUnit;

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
    /*
    Potions feature:
        - the bottle should disappear;
        - the invisibility potion should hide armor aswel;
     */
    @EventHandler
    public void on(@NotNull PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        if (e.getItem().getType() == Material.POTION) {
            e.setReplacement(new ItemStack(Material.AIR));

            PotionMeta potionMeta = (PotionMeta) e.getItem().getItemMeta();
            if (potionMeta.hasCustomEffect(PotionEffectType.INVISIBILITY)) {
                InvisibilityUtilities.hideArmor(player);
                BedWarsPlugin.getInstance().getScheduler().asyncLater(() -> {
                    InvisibilityUtilities.showArmor(player);
                    player.sendMessage(GameConstants.MESSAGE_PLAYER_INVISIBILITY_END);
                }, potionMeta.getCustomEffects().get(0).getDuration() / 20, TimeUnit.SECONDS);
                player.sendMessage(GameConstants.MESSAGE_PLAYER_INVISIBILITY);
            }
        }
    }

    public void on(@NotNull PlayerArmorStandManipulateEvent e) {
        Player player = e.getPlayer();
        BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player).ifPresent(gameMatch -> e.setCancelled(true));
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
    }
}
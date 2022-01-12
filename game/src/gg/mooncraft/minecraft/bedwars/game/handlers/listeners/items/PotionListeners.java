package gg.mooncraft.minecraft.bedwars.game.handlers.listeners.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.GameConstants;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerDamageEvent;
import gg.mooncraft.minecraft.bedwars.game.utilities.InvisibilityUtilities;

import java.util.concurrent.TimeUnit;

/**
 * Potions feature:
 * - the bottle should disappear;
 * - the invisibility potion should hide armor aswel;
 */
public class PotionListeners implements Listener {

    /*
    Constructor
     */
    public PotionListeners() {
        Bukkit.getPluginManager().registerEvents(this, BedWarsPlugin.getInstance());
    }

    /*
    Handlers
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

    @EventHandler
    public void on(@NotNull MatchPlayerDamageEvent e) {
        Player player = e.getPlayer();
        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            InvisibilityUtilities.showArmor(player);
            player.sendMessage(GameConstants.MESSAGE_PLAYER_INVISIBILITY_ARMOR);
        }
    }
}
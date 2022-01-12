package gg.mooncraft.minecraft.bedwars.game.handlers.listeners.items;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameTeam;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.BedbugTask;
import gg.mooncraft.minecraft.bedwars.game.shop.itemdata.BedbugItem;

public class UtilityListeners implements Listener {

    /*
    Constructor
     */
    public UtilityListeners() {
        Bukkit.getPluginManager().registerEvents(this, BedWarsPlugin.getInstance());
    }

    /*
    Handlers
     */
    @EventHandler
    public void on(@NotNull PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction().isRightClick() && e.hasItem()) {
            ItemStack itemStack = e.getItem();
            BedbugItem.getFrom(itemStack).ifPresent(bedbugItem -> {
                e.setCancelled(true);
                Snowball snowball = player.launchProjectile(Snowball.class);
                snowball.setGlowing(true);
                BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player).flatMap(gameMatch -> gameMatch.getDataOf(player)).ifPresent(gameMatchPlayer -> {
                    new BedbugTask(gameMatchPlayer, snowball);
                });
            });
        }
    }

    @EventHandler
    public void on(@NotNull EntityTargetLivingEntityEvent e) {
        if (e.getEntity() instanceof Silverfish silverfish && e.getTarget() instanceof Player player) {
            if (silverfish.hasMetadata("owner") && silverfish.hasMetadata("owner-team")) {
                GameTeam gameTeam = GameTeam.valueOf(silverfish.getMetadata("owner-team").get(0).asString());

                BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player).flatMap(gameMatch -> gameMatch.getTeam(gameTeam)).flatMap(gameMatchTeam -> silverfish.getWorld().getNearbyEntitiesByType(Player.class, silverfish.getLocation(), 3, 3, 3)
                        .stream()
                        .filter(possibleTarget -> !gameMatchTeam.hasPlayer(possibleTarget.getUniqueId()))
                        .findAny()).ifPresentOrElse(silverfish::setTarget, () -> e.setCancelled(true));
            }
        }
    }
}
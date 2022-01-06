package gg.mooncraft.minecraft.bedwars.game.handlers.listeners;

import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.data.map.point.PointTypes;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.events.EventsAPI;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerDeathEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerJoinEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerQuitEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchVillagerInteractEvent;
import gg.mooncraft.minecraft.bedwars.game.match.damage.PlayerDamage;
import gg.mooncraft.minecraft.bedwars.game.utilities.PointAdapter;

import java.util.Optional;

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
        BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player)
                .ifPresent(gameMatch -> gameMatch.getDataOf(player)
                        .ifPresent(gameMatchPlayer -> EventsAPI.callEventSync(new MatchPlayerJoinEvent(player, gameMatch, gameMatchPlayer)))
                );
        e.joinMessage(null);
    }

    @EventHandler
    public void on(@NotNull PlayerQuitEvent e) {
        Player player = e.getPlayer();
        BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player)
                .ifPresent(gameMatch -> gameMatch.getDataOf(player)
                        .ifPresent(gameMatchPlayer -> EventsAPI.callEventSync(new MatchPlayerQuitEvent(player, gameMatch, gameMatchPlayer)))
                );
        e.quitMessage(null);
    }

    @EventHandler
    public void on(@NotNull PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        if (e.getRightClicked() instanceof Villager villager) {
            BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player)
                    .ifPresent(gameMatch -> gameMatch.getVillagersSystem().getMatchVillager(villager)
                            .ifPresent(matchVillager -> gameMatch.getDataOf(player)
                                    .ifPresent(gameMatchPlayer -> {
                                        e.setCancelled(true);
                                        EventsAPI.callEventSync(new MatchVillagerInteractEvent(player, gameMatchPlayer, matchVillager));
                                    })
                            )
                    );
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(@NotNull EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        Player damager = lookupPlayer(e.getDamager());
        if (damager != null) {
            BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player).ifPresent(gameMatch -> gameMatch.getDamageSystem().trackPlayer(player, damager, e.getFinalDamage()));
        }
    }

    @EventHandler
    public void on(@NotNull PlayerDeathEvent e) {
        Player player = e.getPlayer();
        BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player).ifPresent(gameMatch -> {
            gameMatch.getDataOf(player).ifPresent(gameMatchPlayer -> {
                BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
                    player.spigot().respawn();

                    Optional<PlayerDamage> optionalPlayerDamage = gameMatch.getDamageSystem().getHighestTracker(player);
                    optionalPlayerDamage.ifPresentOrElse(playerDamage -> {
                        new MatchPlayerDeathEvent(player, gameMatch, gameMatchPlayer.getParent(), gameMatchPlayer, MatchPlayerDeathEvent.Reason.PLAYER, playerDamage).callEvent();
                    }, () -> {
                        new MatchPlayerDeathEvent(player, gameMatch, gameMatchPlayer.getParent(), gameMatchPlayer, MatchPlayerDeathEvent.Reason.UNKNOWN, null).callEvent();
                    });
                });
                e.setCancelled(true);
                e.setKeepLevel(false);
                e.setKeepInventory(false);
                Bukkit.broadcastMessage("PlayerDeath");
            });
        });
        e.deathMessage(Component.empty());
    }

    @EventHandler
    public void on(@NotNull PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (e.getTo().getBlockY() <= 0) {
            player.setHealth(0D);
        }
    }

    @EventHandler
    public void on(@NotNull PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player).ifPresent(gameMatch -> {
            Optional<Location> optionalLocation = gameMatch.getBedWarsMap()
                    .map(BedWarsMap::getPointsContainer)
                    .flatMap(container -> container.getGameMapPoint(gameMatch.getGameMode(), PointTypes.MAP.MAP_CENTER)
                            .stream()
                            .findFirst())
                    .map(gameMapPoint -> PointAdapter.adapt(gameMatch, gameMapPoint));
            optionalLocation.ifPresent(e::setRespawnLocation);
        });
    }

    /*
    Methods
     */
    private @Nullable Player lookupPlayer(@NotNull Entity entity) {
        if (!(entity instanceof Player)) {
            if (entity instanceof Projectile projectile && projectile.getShooter() instanceof Player projectileOwner) {
                return projectileOwner;
            }
        } else {
            return (Player) entity;
        }
        return null;
    }
}
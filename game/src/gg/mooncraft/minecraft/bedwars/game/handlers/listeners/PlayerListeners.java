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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gg.mooncraft.minecraft.bedwars.data.GameState;
import gg.mooncraft.minecraft.bedwars.data.GameTeam;
import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.data.map.point.PointTypes;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.events.EventsAPI;
import gg.mooncraft.minecraft.bedwars.game.events.MatchBlockBreakEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchBlockPlaceEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchIslandRegionEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerDamageEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerDeathEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerJoinEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerMoveEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerPickupItemEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerQuitEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchVillagerInteractEvent;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchTeam;
import gg.mooncraft.minecraft.bedwars.game.match.damage.PlayerDamage;
import gg.mooncraft.minecraft.bedwars.game.utilities.PointAdapter;
import gg.mooncraft.minecraft.bedwars.game.utilities.WorldUtilities;

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
    public void on(@NotNull BlockPlaceEvent e) {
        Player player = e.getPlayer();
        BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player).ifPresent(gameMatch -> {
            gameMatch.getDataOf(player).ifPresent(gameMatchPlayer -> {
                boolean cancelled = !new MatchBlockPlaceEvent(player, e.getBlock().getLocation(), gameMatch, gameMatchPlayer).callEvent();
                if (cancelled) {
                    e.setBuild(false);
                    e.setCancelled(true);
                }
            });
        });
    }

    @EventHandler
    public void on(@NotNull BlockBreakEvent e) {
        Player player = e.getPlayer();
        BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player).ifPresent(gameMatch -> {
            e.setExpToDrop(0);
            e.setDropItems(false);

            gameMatch.getDataOf(player).ifPresent(gameMatchPlayer -> {
                boolean cancelled = !new MatchBlockBreakEvent(player, e.getBlock().getLocation(), gameMatch, gameMatchPlayer).callEvent();
                if (cancelled) {
                    e.setCancelled(true);
                }
            });
        });
    }

    @EventHandler
    public void on(@NotNull EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player).ifPresent(gameMatch -> {
            gameMatch.getDataOf(player).ifPresent(gameMatchPlayer -> new MatchPlayerPickupItemEvent(player, gameMatch, gameMatchPlayer, e.getItem()).callEvent());
        });
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
            BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player).ifPresent(gameMatch -> {
                gameMatch.getDataOf(player).ifPresent(gameMatchPlayer -> {
                    if (gameMatch.getGameState() != GameState.PLAYING || gameMatchPlayer.getParent().hasPlayer(damager.getUniqueId())) {
                        e.setDamage(0);
                        e.setCancelled(true);
                        return;
                    }
                    gameMatch.getDamageSystem().trackPlayer(player, damager, e.getFinalDamage());
                    EventsAPI.callEventSync(new MatchPlayerDamageEvent(player, gameMatch, gameMatchPlayer.getParent(), gameMatchPlayer, e.getFinalDamage()));
                });
            });
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
            });
        });
        e.deathMessage(Component.empty());
    }

    @EventHandler
    public void on(@NotNull PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (!WorldUtilities.isSameXYZ(e.getFrom(), e.getTo())) {
            BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player).ifPresent(gameMatch -> {
                gameMatch.getDataOf(player).ifPresent(gameMatchPlayer -> {
                    new MatchPlayerMoveEvent(player, gameMatch, gameMatchPlayer.getParent(), gameMatchPlayer, e.getTo(), e.getFrom()).callEvent();

                    Optional<GameTeam> toTeam = lookupTeamArea(gameMatch, e.getTo());
                    Optional<GameTeam> fromTeam = lookupTeamArea(gameMatch, e.getFrom());

                    if (toTeam.isEmpty() && fromTeam.isPresent()) {
                        new MatchIslandRegionEvent(player, gameMatchPlayer, fromTeam.get(), MatchIslandRegionEvent.Action.LEAVE).callEvent();
                    } else if (toTeam.isPresent() && fromTeam.isEmpty()) {
                        new MatchIslandRegionEvent(player, gameMatchPlayer, toTeam.get(), MatchIslandRegionEvent.Action.ENTER).callEvent();
                    }
                });
            });
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
    private @NotNull Optional<GameTeam> lookupTeamArea(@NotNull GameMatch gameMatch, @NotNull Location location) {
        return gameMatch.getTeamList().stream().filter(gameMatchTeam -> gameMatchTeam.isBedArea(location)).map(GameMatchTeam::getGameTeam).findFirst();
    }

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
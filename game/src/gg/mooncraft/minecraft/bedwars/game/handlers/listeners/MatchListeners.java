package gg.mooncraft.minecraft.bedwars.game.handlers.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.neznamy.tab.api.TabAPI;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameState;
import gg.mooncraft.minecraft.bedwars.data.GameTeam;
import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.data.map.MapPointsContainer;
import gg.mooncraft.minecraft.bedwars.data.map.point.PointTypes;
import gg.mooncraft.minecraft.bedwars.data.map.point.TeamMapPoint;
import gg.mooncraft.minecraft.bedwars.data.user.BedWarsUser;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.GameConstants;
import gg.mooncraft.minecraft.bedwars.game.events.EventsAPI;
import gg.mooncraft.minecraft.bedwars.game.events.MatchBedBreakEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchBlockBreakEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchBlockPlaceEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchIslandRegionEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerDeathEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerJoinEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerMoveEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerQuitEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchTeamWinEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchUpdateGameEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchUpdatePlayerEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchUpdateStateEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchVillagerInteractEvent;
import gg.mooncraft.minecraft.bedwars.game.match.GameEvent;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchTeam;
import gg.mooncraft.minecraft.bedwars.game.match.PlayerStatus;
import gg.mooncraft.minecraft.bedwars.game.match.TeamStatus;
import gg.mooncraft.minecraft.bedwars.game.match.VillagerType;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.GeneratorTask;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.SpectatorTask;
import gg.mooncraft.minecraft.bedwars.game.menu.ShopMenu;
import gg.mooncraft.minecraft.bedwars.game.menu.ShopUpgradesMenu;
import gg.mooncraft.minecraft.bedwars.game.utilities.DisplayUtilities;
import gg.mooncraft.minecraft.bedwars.game.utilities.ItemsUtilities;
import gg.mooncraft.minecraft.bedwars.game.utilities.PointAdapter;
import gg.mooncraft.minecraft.bedwars.game.utilities.WorldUtilities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class MatchListeners implements Listener {

    /*
    Constructor
     */
    public MatchListeners() {
        Bukkit.getPluginManager().registerEvents(this, BedWarsPlugin.getInstance());
    }

    /*
    Handlers
     */
    @EventHandler
    public void on(@NotNull MatchUpdateStateEvent e) {
        GameMatch gameMatch = e.getMatch();

        switch (gameMatch.getGameState()) {
            case PLAYING -> {
                gameMatch.getEventSystem().play();
                gameMatch.getFurnaceSystem().play();
                gameMatch.getGeneratorSystem().play();
                gameMatch.getScoreboard().unregister();

                // Send visual elements and teleport players
                List<TeamMapPoint> teamMapPointList = gameMatch.getBedWarsMap()
                        .map(BedWarsMap::getPointsContainer)
                        .map(mapPointsContainer -> mapPointsContainer.getTeamMapPoint(gameMatch.getGameMode(), PointTypes.TEAM.TEAM_SPAWNPOINT))
                        .orElse(new ArrayList<>());
                for (GameMatchTeam gameMatchTeam : gameMatch.getTeamList()) {
                    Location location = teamMapPointList
                            .stream()
                            .filter(point -> point.getGameTeam() == gameMatchTeam.getGameTeam())
                            .findFirst()
                            .map(point -> PointAdapter.adapt(gameMatch, point))
                            .orElse(null);
                    if (location == null) {
                        continue;
                    }
                    if (!gameMatchTeam.isAnyAlive()) {
                        gameMatchTeam.updateStatus(TeamStatus.NOT_ALIVE);
                        gameMatchTeam.getMapPointList()
                                .stream()
                                .filter(point -> point.getType() == PointTypes.TEAM.TEAM_BED)
                                .findFirst()
                                .map(point -> PointAdapter.adapt(gameMatch, point)).ifPresent(bedLocation -> {
                                    bedLocation.getBlock().setType(Material.AIR);
                                });
                        continue;
                    }

                    gameMatchTeam.broadcastAction(gameMatchPlayer -> {
                        gameMatchPlayer.getPlayer().ifPresent(player -> {
                            player.teleportAsync(location);
                            player.setGameMode(GameMode.SURVIVAL);
                            player.hideBossBar(BedWarsPlugin.getInstance().getBossBar());
                            GameConstants.MESSAGE_STARTING_TIP.forEach(player::sendMessage);

                            player.getInventory().clear();
                            player.getInventory().setItem(0, gameMatchPlayer.getWeapon());
                            player.getInventory().setArmorContents(gameMatchPlayer.getArmor());
                        });
                        gameMatchPlayer.getTabPlayer().ifPresent(tabPlayer -> {
                            TabAPI.getInstance().getScoreboardManager().showScoreboard(tabPlayer, gameMatchTeam.getScoreboard());
                            TabAPI.getInstance().getTeamManager().setPrefix(tabPlayer, GameConstants.NAMETAG_FORMAT
                                    .replaceAll("%color%", gameMatchTeam.getGameTeam().getChatColor().toString())
                                    .replaceAll("%team%", String.valueOf(gameMatchTeam.getGameTeam().getLetter()))
                            );
                        });
                    });
                }
            }
            case UNLOADING -> {
                Optional<GameMatchTeam> winnerTeamOptional = gameMatch.getTeamList().stream().filter(GameMatchTeam::isAnyAlive).findFirst();
                winnerTeamOptional.ifPresent(winnerTeam -> {
                    EventsAPI.callEventSync(new MatchTeamWinEvent(winnerTeam));
                    winnerTeam.broadcastAction(gameMatchPlayer -> {
                        gameMatchPlayer.getPlayer().ifPresent(player -> {
                            player.showTitle(Title.title(Component.text(GameConstants.MESSAGE_WINNER_TITLE), Component.empty()));
                        });
                    });

                    gameMatch.getPlayerList().forEach(player -> {
                        Optional<BedWarsUser> bedWarsUserOptional = BedWarsPlugin.getInstance().getUserFactory().getUser(player);

                        player.showBossBar(BedWarsPlugin.getInstance().getBossBar());
                        GameConstants.MESSAGE_GLOBAL_WINNER.forEach(line -> {
                            player.sendMessage(line
                                    .replaceAll("%team-color%", winnerTeam.getGameTeam().getChatColor().toString())
                                    .replaceAll("%team-name%", winnerTeam.getGameTeam().name())
                                    .replaceAll("%level-current%", bedWarsUserOptional.map(BedWarsUser::getLevel).map(String::valueOf).orElse("-"))
                                    .replaceAll("%level-next%", bedWarsUserOptional.map(BedWarsUser::getNextLevel).map(String::valueOf).orElse("-"))
                                    .replaceAll("%experience-reward%", gameMatch.getDataOf(player).map(GameMatchPlayer::getExperienceReward).map(AtomicInteger::get).map(String::valueOf).orElse("-"))
                                    .replaceAll("%experience-current%", bedWarsUserOptional.map(BedWarsUser::getExperienceCurrent).map(String::valueOf).orElse("-"))
                                    .replaceAll("%experience-required%", bedWarsUserOptional.map(BedWarsUser::getExperienceRequired).map(String::valueOf).orElse("-"))
                                    .replaceAll("%experience-percentage%", bedWarsUserOptional.map(BedWarsUser::getExperiencePercentage).map(d -> new DecimalFormat("#.##").format(d)).orElse("-"))
                                    .replaceAll("%experience-progress-bar%", bedWarsUserOptional.map(BedWarsUser::getExperienceProgressBar).orElse("-"))
                            );
                        });
                    });
                });
            }
            case ENDING -> {
                for (GameMatchTeam gameMatchTeam : gameMatch.getTeamList()) {
                    gameMatchTeam.getScoreboard().unregister();
                }
                gameMatch.terminate();
            }
        }
        BedWarsPlugin.getInstance().getGameServerManager().sendGameServerMessage();
    }

    @EventHandler
    public void on(@NotNull MatchUpdateGameEvent e) {
        GameMatch gameMatch = e.getMatch();
        gameMatch.getGeneratorSystem().getTaskList().forEach(GeneratorTask::forceUpdateHologram);

        if (e.getPreviousEvent() == null) return;
        switch (e.getPreviousEvent().getGameEvent()) {
            case DIAMOND -> {
                gameMatch.getPlayerList().forEach(player -> {
                    player.sendMessage(GameConstants.MESSAGE_GENERATOR_UPDATE
                            .replaceAll("%type%", ChatColor.AQUA + GameEvent.DIAMOND.getDisplay())
                            .replaceAll("%tier%", DisplayUtilities.getLiteral(gameMatch.getGeneratorSystem().getDiamondTier()))
                    );
                });
            }
            case EMERALD -> {
                gameMatch.getPlayerList().forEach(player -> {
                    player.sendMessage(GameConstants.MESSAGE_GENERATOR_UPDATE
                            .replaceAll("%type%", ChatColor.DARK_GREEN + GameEvent.EMERALD.getDisplay())
                            .replaceAll("%tier%", DisplayUtilities.getLiteral(gameMatch.getGeneratorSystem().getEmeraldTier()))
                    );
                });
            }
        }
    }

    @EventHandler
    public void on(@NotNull MatchUpdatePlayerEvent e) {
        GameMatch gameMatch = e.getMatch();
        if (gameMatch.getGameState() != GameState.PLAYING) {
            return;
        }
        GameMatchPlayer gameMatchPlayer = e.getMatchPlayer();

        switch (gameMatchPlayer.getPlayerStatus()) {
            case ALIVE -> {
                e.getMatchPlayer().getPlayer().ifPresent(player -> {
                    e.getMatchPlayer().getParent().getMapPointList().stream().filter(teamMapPoint -> teamMapPoint.getType() == PointTypes.TEAM.TEAM_SPAWNPOINT).findFirst().ifPresent(teamMapPoint -> {
                        Location location = PointAdapter.adapt(gameMatch, teamMapPoint);
                        player.teleportAsync(location);
                    });

                    gameMatchPlayer.updateEffect();
                    player.getInventory().setItem(0, gameMatchPlayer.getWeapon());
                    player.getInventory().setArmorContents(gameMatchPlayer.getArmor());

                    player.setGameMode(GameMode.SURVIVAL);
                });
            }
            case RESPAWNING -> {
                e.getMatchPlayer().getPlayer().ifPresent(player -> {
                    player.getInventory().clear();
                    player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
                    player.setGameMode(GameMode.SPECTATOR);
                });
                new SpectatorTask(gameMatch, gameMatchPlayer).play();
            }
            case SPECTATING -> {
                e.getMatchPlayer().getPlayer().ifPresent(player -> {
                    player.getInventory().clear();
                    player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
                    player.setGameMode(GameMode.SPECTATOR);

                    BedWarsPlugin.getInstance().getUserFactory().getUser(player).ifPresent(bedWarsUser -> {
                        e.getMatchPlayer().getGameStatistics().forEach((k, v) -> {
                            bedWarsUser.getStatisticContainer().updateGameStatistic(gameMatch.getGameMode(), k, v.get());
                        });
                        e.getMatchPlayer().getOverallStatistics().forEach((k, v) -> {
                            bedWarsUser.getStatisticContainer().updateOverallStatistic(k, v.get());
                        });
                    });
                });

                long teamsAlive = e.getMatch().getTeamList()
                        .stream()
                        .filter(GameMatchTeam::isAnyAlive)
                        .count();
                if (teamsAlive == 1) {
                    gameMatch.getGameTicker().getGameEndTask().play();
                }
            }
        }
    }

    @EventHandler
    public void on(@NotNull MatchPlayerJoinEvent e) {
        Player player = e.getPlayer();
        GameMatch gameMatch = e.getMatch();
        GameMatchPlayer gameMatchPlayer = e.getMatchPlayer();
        GameMatchTeam gameMatchTeam = e.getMatchTeam();

        // Clear chat and inventory
        for (int i = 0; i < 256; i++) {
            player.sendMessage(ChatColor.RESET + "");
        }
        player.getInventory().clear();
        GameConstants.MESSAGE_PLAYER_MOVE.forEach(line -> {
            String[] args = BedWarsPlugin.getInstance().getServerName().split("-");
            player.sendMessage(line.replaceAll("%server-name%", args[0].substring(0, 2) + "-" + args[1].substring(0, 2) + "-" + args[2]));
        });

        // Teleport to spawnpoint
        gameMatch.getBedWarsMap()
                .map(BedWarsMap::getPointsContainer)
                .flatMap(pointsContainer -> pointsContainer.getGameMapPoint(gameMatch.getGameMode(), PointTypes.MAP.MAP_SPAWNPOINT).stream().findFirst())
                .map(point -> PointAdapter.adapt(gameMatch, point))
                .ifPresent(player::teleportAsync);

        // Show scoreboard and nametags
        BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
            gameMatchPlayer.getTabPlayer().ifPresent(tabPlayer -> {
                if (gameMatch.getGameState() == GameState.WAITING) {
                    TabAPI.getInstance().getScoreboardManager().showScoreboard(tabPlayer, gameMatch.getScoreboard());
                } else {
                    TabAPI.getInstance().getScoreboardManager().showScoreboard(tabPlayer, gameMatchTeam.getScoreboard());
                }
            });
            gameMatch.getMatchPlayerList()
                    .stream()
                    .map(GameMatchPlayer::getTabPlayer)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(streamTabPlayer -> BedWarsPlugin.getInstance().getBoardManager().updateScoreboard(streamTabPlayer));
        });

        // Send join message
        String joinMessage = PlaceholderAPI.setPlaceholders(player, GameConstants.MESSAGE_PLAYER_JOIN)
                .replaceAll("%game-players-count%", String.valueOf(gameMatch.getPlayersCount()))
                .replaceAll("%game-players-capacity%", String.valueOf(gameMatch.getPlayersCapacity()));
        player.sendMessage(joinMessage);
        gameMatch.getPlayerList().forEach(streamPlayer -> streamPlayer.sendMessage(joinMessage));

        // Try to update GameStarTask if necessary
        if (gameMatch.getGameState() == GameState.WAITING) {
            player.setGameMode(GameMode.ADVENTURE);
            player.showBossBar(BedWarsPlugin.getInstance().getBossBar());
            if (gameMatch.getPlayersCount() == gameMatch.getPlayersCapacity()) {
                gameMatch.getGameTicker().getGameStartTask().play();
            }
        }

        // Send update message to lobby
        BedWarsPlugin.getInstance().getGameServerManager().sendGameServerMessage();
    }

    @EventHandler
    public void on(@NotNull MatchPlayerQuitEvent e) {
        Player player = e.getPlayer();
        GameMatch gameMatch = e.getMatch();

        // If the game is still waiting for players, a quit has to free team slot
        // Else the player must be set as a spectator
        if (gameMatch.getGameState() != GameState.WAITING) {
            GameMatchPlayer gameMatchPlayer = e.getMatchPlayer();
            gameMatchPlayer.updateStatus(PlayerStatus.SPECTATING);
        } else {
            e.getMatchTeam().delPlayer(player.getUniqueId());
        }

        // Send quit message
        String quitMessage = PlaceholderAPI.setPlaceholders(player, GameConstants.MESSAGE_PLAYER_QUIT);
        gameMatch.getPlayerList().forEach(streamPlayer -> streamPlayer.sendMessage(quitMessage));

        // Force update all the scoreboards
        BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
            gameMatch.getMatchPlayerList()
                    .stream()
                    .map(GameMatchPlayer::getTabPlayer)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(streamTabPlayer -> BedWarsPlugin.getInstance().getBoardManager().updateScoreboard(streamTabPlayer));
        });

        // Try to update GameStarTask if necessary
        if (gameMatch.getGameState() == GameState.WAITING) {
            gameMatch.getGameTicker().getGameStartTask().stop();
        }

        // Send update message to lobby
        BedWarsPlugin.getInstance().getGameServerManager().sendGameServerMessage();
    }

    @EventHandler
    public void on(@NotNull MatchBlockPlaceEvent e) {
        Player player = e.getPlayer();
        Location location = e.getLocation();
        GameMatch gameMatch = e.getMatch();
        if (location.getBlockY() > gameMatch.getBedWarsMap().map(BedWarsMap::getPointsContainer).map(MapPointsContainer::getMaximumBlockHeight).orElse(0) || location.getBlockY() < gameMatch.getBedWarsMap().map(BedWarsMap::getPointsContainer).map(MapPointsContainer::getMinimumBlockHeight).orElse(0)) {
            player.sendMessage(GameConstants.MESSAGE_BLOCK_HEIGHT_LIMIT);
            e.setCancelled(true);
            return;
        }
        if (!gameMatch.getBlocksSystem().canPlace(location)) {
            player.sendMessage(GameConstants.MESSAGE_BLOCK_NO_BUILD);
            e.setCancelled(true);
            return;
        }
        gameMatch.getBlocksSystem().placeBlock(location);
    }

    @EventHandler
    public void on(@NotNull MatchBlockBreakEvent e) {
        Player player = e.getPlayer();
        Location location = e.getLocation();
        GameMatch gameMatch = e.getMatch();

        // Check if it's a bed
        if (location.getBlock().getType().name().contains("BED")) {
            Location[] parts = WorldUtilities.getBedParts(location.getBlock());
            boolean cancelled = !new MatchBedBreakEvent(player, e.getMatchPlayer(), parts).callEvent();
            e.setCancelled(cancelled);
            return;
        }

        // Check if it's a breakable block
        if (!gameMatch.getBlocksSystem().canBreak(location)) {
            player.sendMessage(GameConstants.MESSAGE_BLOCK_UNBREAKABLE);
            e.setCancelled(true);
            return;
        }
        location.getBlock().getDrops(player.getInventory().getItemInMainHand()).forEach(itemStack -> player.getInventory().addItem(ItemsUtilities.createPureItem(itemStack.getType())));
        gameMatch.getBlocksSystem().breakBlock(location);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(@NotNull MatchBedBreakEvent e) {
        Player player = e.getPlayer();

        // Check bed's team ownership
        if (e.isOwnBed()) {
            player.sendMessage(GameConstants.MESSAGE_BED_YOUR);
            e.setCancelled(true);
            return;
        }
        Optional<GameMatchTeam> optionalBedTeamOwner = e.getMatchTeamOwner();
        optionalBedTeamOwner.ifPresent(gameMatchTeam -> {
            // Update team status
            gameMatchTeam.updateStatus(TeamStatus.NOT_ALIVE);

            // Create explosion effect and play sound
            Arrays.stream(e.getBedParts()).forEach(location -> location.getWorld().createExplosion(location, 1, false, false));
            gameMatchTeam.broadcastAction(gameMatchPlayer -> gameMatchPlayer.getPlayer().ifPresent(streamPlayer -> streamPlayer.playSound(streamPlayer.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1)));

            // Broadcast messages
            e.getMatch().getPlayerList().forEach(streamPlayer -> {
                if (gameMatchTeam.hasPlayer(streamPlayer.getUniqueId())) {
                    streamPlayer.showTitle(Title.title(Component.text(GameConstants.MESSAGE_BED_DESTRUCTION_TITLE), Component.text(GameConstants.MESSAGE_BED_DESTRUCTION_SUBTITLE)));
                    GameConstants.MESSAGE_BED_DESTRUCTION_YOUR.forEach(line -> streamPlayer.sendMessage(line
                            .replaceAll("%destroyer-team-color%", e.getMatchTeam().getGameTeam().getChatColor().toString())
                            .replaceAll("%destroyer-player-name%", player.getName())
                    ));
                } else {
                    GameConstants.MESSAGE_BED_DESTRUCTION_OTHERS.forEach(line -> streamPlayer.sendMessage(line
                            .replaceAll("%team-color%", gameMatchTeam.getGameTeam().getChatColor().toString())
                            .replaceAll("%team-name%", gameMatchTeam.getGameTeam().getDisplay())
                            .replaceAll("%destroyer-team-color%", e.getMatchTeam().getGameTeam().getChatColor().toString())
                            .replaceAll("%destroyer-team-name%", e.getMatchTeam().getGameTeam().getDisplay())
                            .replaceAll("%destroyer-player-name%", player.getName())
                    ));
                }
            });
        });
    }

    @EventHandler
    public void on(@NotNull MatchPlayerDeathEvent e) {
        Player player = e.getPlayer();
        GameMatch gameMatch = e.getMatch();
        GameMatchTeam gameMatchTeam = e.getMatchTeam();
        GameMatchPlayer gameMatchPlayer = e.getMatchPlayer();
        if (gameMatchTeam.getTeamStatus() == TeamStatus.ALIVE) {
            gameMatchPlayer.updateStatus(PlayerStatus.RESPAWNING);
        } else {
            gameMatchPlayer.updateStatus(PlayerStatus.SPECTATING);
        }

        if (e.getDeathReason() == MatchPlayerDeathEvent.DeathReason.PLAYER) {
            e.getLastPlayerDamage().getPlayer().sendMessage(ChatColor.RESET.toString());
            e.getItems().forEach((k, v) -> {
                ItemStack itemStack = ItemsUtilities.createPureItem(k);
                itemStack.setAmount(v.get());

                e.getLastPlayerDamage().getPlayer().getInventory().addItem(itemStack);
                ChatColor color = itemStack.getType() == Material.DIAMOND ? ChatColor.AQUA : itemStack.getType() == Material.EMERALD ? ChatColor.DARK_GREEN : itemStack.getType() == Material.GOLD_INGOT ? ChatColor.GOLD : ChatColor.WHITE;
                e.getLastPlayerDamage().getPlayer().sendMessage(color + "+" + v.get() + " " + DisplayUtilities.getDisplay(itemStack));
            });
            e.getLastPlayerDamage().getPlayer().sendMessage(ChatColor.RESET.toString());
        }

        gameMatch.getPlayerList().forEach(streamPlayer -> {
            if (e.getDeathReason() == MatchPlayerDeathEvent.DeathReason.PLAYER) {
                streamPlayer.sendMessage(GameConstants.MESSAGE_PLAYER_KILL
                        .replaceAll("%killer-team-color%", gameMatch.getTeamOf(e.getLastPlayerDamage().getPlayer()).map(GameMatchTeam::getGameTeam).map(GameTeam::getChatColor).map(net.md_5.bungee.api.ChatColor::toString).orElse(ChatColor.RESET.toString()))
                        .replaceAll("%killed-team-color%", gameMatchTeam.getGameTeam().getChatColor().toString())
                        .replaceAll("%killer%", e.getLastPlayerDamage().getPlayer().getName())
                        .replaceAll("%killed%", player.getName())
                );
            } else {
                streamPlayer.sendMessage(GameConstants.MESSAGE_PLAYER_DIES
                        .replaceAll("%player%", player.getName())
                        .replaceAll("%team-color%", gameMatchTeam.getGameTeam().getChatColor().toString())
                );
            }
        });

        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 2, 2);
    }

    @EventHandler
    public void on(@NotNull MatchPlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (e.getTo().getBlockY() < -5) {
            player.setHealth(0D);
        }
    }

    @EventHandler
    public void on(@NotNull MatchIslandRegionEvent e) {
        Player player = e.getPlayer();

        // Check team upgrades
        if (e.getMatchTeam().getGameTeam() == e.getTeam()) {
            if (e.getMatchTeam().getUpgradeTier("healpool") == 1) {
                switch (e.getAction()) {
                    case ENTER -> {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, true, true, true));
                    }
                    case LEAVE -> {
                        player.removePotionEffect(PotionEffectType.REGENERATION);
                    }
                }
            }
        }

    }

    @EventHandler
    public void on(@NotNull MatchVillagerInteractEvent e) {
        if (e.getMatchVillager().getVillagerType() == VillagerType.ITEM_SHOP) {
            ShopMenu shopMenu = new ShopMenu(e.getPlayer(), e.getMatch(), e.getMatchPlayer());
            e.getPlayer().openInventory(shopMenu.getInventory());
        } else if (e.getMatchVillager().getVillagerType() == VillagerType.UPGRADE_SHOP) {
            ShopUpgradesMenu shopUpgradesMenu = new ShopUpgradesMenu(e.getPlayer(), e.getMatch(), e.getMatchPlayer());
            e.getPlayer().openInventory(shopUpgradesMenu.getInventory());
        }
    }
}
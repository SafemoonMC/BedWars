package gg.mooncraft.minecraft.bedwars.game.handlers.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.neznamy.tab.api.TabAPI;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameState;
import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.data.map.MapPointsContainer;
import gg.mooncraft.minecraft.bedwars.data.map.point.PointTypes;
import gg.mooncraft.minecraft.bedwars.data.map.point.TeamMapPoint;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.GameConstants;
import gg.mooncraft.minecraft.bedwars.game.events.MatchBedBreakEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchBlockBreakEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchBlockPlaceEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerDeathEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerJoinEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerMoveEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerQuitEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchUpdateGameEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchUpdatePlayerEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchUpdateStateEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchVillagerInteractEvent;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchTeam;
import gg.mooncraft.minecraft.bedwars.game.match.PlayerStatus;
import gg.mooncraft.minecraft.bedwars.game.match.VillagerType;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.GameMatchEvent;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.GeneratorTask;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.SpectatorTask;
import gg.mooncraft.minecraft.bedwars.game.menu.ShopMenu;
import gg.mooncraft.minecraft.bedwars.game.utilities.DisplayUtilities;
import gg.mooncraft.minecraft.bedwars.game.utilities.PointAdapter;
import gg.mooncraft.minecraft.bedwars.game.utilities.WorldUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        GameMatch gameMatch = e.getGameMatch();

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
                        return;
                    }

                    gameMatchTeam.broadcastAction(gameMatchPlayer -> {
                        gameMatchPlayer.getPlayer().ifPresent(player -> {
                            player.teleportAsync(location);
                            player.setGameMode(GameMode.SURVIVAL);
                            player.hideBossBar(BedWarsPlugin.getInstance().getBossBar());
                            GameConstants.MESSAGE_STARTING_TIP.forEach(player::sendMessage);
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
            case ENDING -> {
                for (GameMatchTeam gameMatchTeam : gameMatch.getTeamList()) {
                    gameMatchTeam.getScoreboard().unregister();
                }
            }
        }
        BedWarsPlugin.getInstance().getGameServerManager().sendGameServerMessage();

        Bukkit.broadcastMessage(gameMatch.getDimension().getName() + " match status: " + gameMatch.getGameState().name());
    }

    @EventHandler
    public void on(@NotNull MatchUpdateGameEvent e) {
        GameMatch gameMatch = e.getGameMatch();
        GameMatchEvent gameMatchEvent = e.getGameMatchEvent();
        gameMatch.getGeneratorSystem().getTaskList().forEach(GeneratorTask::forceUpdateHologram);
        Bukkit.broadcastMessage(gameMatch.getDimension().getName() + " match event: " + gameMatchEvent.getGameEvent().name());
    }


    @EventHandler
    public void on(@NotNull MatchUpdatePlayerEvent e) {
        GameMatch gameMatch = e.getGameMatch();
        GameMatchPlayer gameMatchPlayer = e.getGameMatchPlayer();

        switch (gameMatchPlayer.getPlayerStatus()) {
            case ALIVE -> {
                e.getGameMatchPlayer().getPlayer().ifPresent(player -> {
                    e.getGameMatchPlayer().getParent().getMapPointList().stream().filter(teamMapPoint -> teamMapPoint.getType() == PointTypes.TEAM.TEAM_SPAWNPOINT).findFirst().ifPresent(teamMapPoint -> {
                        Location location = PointAdapter.adapt(gameMatch, teamMapPoint);
                        player.teleportAsync(location);
                        player.setGameMode(GameMode.SURVIVAL);
                    });
                });
            }
            case RESPAWNING -> {
                e.getGameMatchPlayer().getPlayer().ifPresent(player -> player.setGameMode(GameMode.SPECTATOR));
                new SpectatorTask(gameMatch, gameMatchPlayer).play();
            }
        }
    }

    @EventHandler
    public void on(@NotNull MatchPlayerJoinEvent e) {
        Player player = e.getPlayer();
        GameMatch gameMatch = e.getGameMatch();
        GameMatchPlayer gameMatchPlayer = e.getGameMatchPlayer();
        GameMatchTeam gameMatchTeam = e.getGameMatchTeam();

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
        GameMatch gameMatch = e.getGameMatch();

        // If the game is still waiting for players, a quit has to free team slot
        // Else the player must be set as a spectator
        if (gameMatch.getGameState() != GameState.WAITING) {
            GameMatchPlayer gameMatchPlayer = e.getGameMatchPlayer();
            gameMatchPlayer.updateStatus(PlayerStatus.SPECTATING);
        } else {
            e.getGameMatchTeam().delPlayer(player.getUniqueId());
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
        GameMatch gameMatch = e.getGameMatch();
        if (location.getBlockY() > gameMatch.getBedWarsMap().map(BedWarsMap::getPointsContainer).map(MapPointsContainer::getMaximumBlockHeight).orElse(0) || location.getBlockY() < gameMatch.getBedWarsMap().map(BedWarsMap::getPointsContainer).map(MapPointsContainer::getMinimumBlockHeight).orElse(0)) {
            player.sendMessage(GameConstants.MESSAGE_BLOCK_HEIGHT_LIMIT);
            e.setCancelled(true);
            return;
        }
        if (!gameMatch.getBlocksSystem().canPlace(location)) {
            e.setCancelled(true);
            return;
        }
        gameMatch.getBlocksSystem().placeBlock(location);
    }

    @EventHandler
    public void on(@NotNull MatchBlockBreakEvent e) {
        Player player = e.getPlayer();
        Location location = e.getLocation();
        GameMatch gameMatch = e.getGameMatch();

        // Check if it's a bed
        if (location.getBlock().getType().name().contains("BED")) {
            Location[] parts = WorldUtilities.getBedParts(location.getBlock());
            boolean cancelled = !new MatchBedBreakEvent(player, parts, e.getGameMatch(), e.getGameMatchPlayer()).callEvent();
            e.setCancelled(cancelled);
            return;
        }

        // Check if it's a breakable block
        if (!gameMatch.getBlocksSystem().canBreak(location)) {
            player.sendMessage(GameConstants.MESSAGE_BLOCK_UNBREAKABLE);
            e.setCancelled(true);
            return;
        }
        location.getBlock().getDrops(player.getInventory().getItemInMainHand()).forEach(itemStack -> player.getInventory().addItem(itemStack));
        gameMatch.getBlocksSystem().breakBlock(location);
    }

    @EventHandler
    public void on(@NotNull MatchBedBreakEvent e) {
        Player player = e.getPlayer();

        // Check bed's team ownership
        if (e.isOwnBed()) {
            player.sendMessage(GameConstants.MESSAGE_BED_YOUR);
            e.setCancelled(true);
            return;
        }
        Optional<GameMatchTeam> optionalBedTeamOwner = e.getGameMatchTeamOwner();
        optionalBedTeamOwner.ifPresent(gameMatchTeam -> {
            // Create explosion effect and play sound
            Arrays.stream(e.getBedParts()).forEach(location -> location.getWorld().createExplosion(location, 1, false, false));
            gameMatchTeam.broadcastAction(gameMatchPlayer -> gameMatchPlayer.getPlayer().ifPresent(streamPlayer -> streamPlayer.playSound(streamPlayer.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1)));

            // Broadcast messages
            e.getGameMatch().getPlayerList().forEach(streamPlayer -> {
                if (gameMatchTeam.hasPlayer(streamPlayer.getUniqueId())) {
                    streamPlayer.showTitle(Title.title(Component.text(GameConstants.MESSAGE_BED_DESTRUCTION_TITLE), Component.text(GameConstants.MESSAGE_BED_DESTRUCTION_SUBTITLE)));
                    GameConstants.MESSAGE_BED_DESTRUCTION_YOUR.forEach(line -> streamPlayer.sendMessage(line
                            .replaceAll("%destroyer-team-color%", e.getGameMatchTeam().getGameTeam().getChatColor().toString())
                            .replaceAll("%destroyer-player-name%", player.getName())
                    ));
                } else {
                    GameConstants.MESSAGE_BED_DESTRUCTION_OTHERS.forEach(line -> streamPlayer.sendMessage(line
                            .replaceAll("%team-color%", gameMatchTeam.getGameTeam().getChatColor().toString())
                            .replaceAll("%team-name%", gameMatchTeam.getGameTeam().getDisplay())
                            .replaceAll("%destroyer-team-color%", e.getGameMatchTeam().getGameTeam().getChatColor().toString())
                            .replaceAll("%destroyer-team-name%", e.getGameMatchTeam().getGameTeam().getDisplay())
                            .replaceAll("%destroyer-player-name%", player.getName())
                    ));
                }
            });
        });
    }

    @EventHandler
    public void on(@NotNull MatchPlayerDeathEvent e) {
        Player player = e.getPlayer();
        GameMatch gameMatch = e.getGameMatch();
        GameMatchPlayer gameMatchPlayer = e.getPlayerMatchPlayer();
        gameMatchPlayer.updateStatus(PlayerStatus.RESPAWNING);

        gameMatch.getPlayerList().forEach(streamPlayer -> {
            if (e.getReason() == MatchPlayerDeathEvent.Reason.PLAYER) {
                streamPlayer.sendMessage(GameConstants.MESSAGE_PLAYER_KILL
                        .replaceAll("%killer%", e.getLastPlayerDamage().getPlayer().getName())
                        .replaceAll("%killed%", player.getName())
                        .replaceAll("%weapon%", DisplayUtilities.getDisplay(e.getLastPlayerDamage().getWeapon())
                        ));
            } else {
                streamPlayer.sendMessage(GameConstants.MESSAGE_PLAYER_DIES
                        .replaceAll("%player%", player.getName())
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
    public void on(@NotNull MatchVillagerInteractEvent e) {
        if (e.getMatchVillager().getVillagerType() == VillagerType.ITEM_SHOP) {
            ShopMenu shopMenu = new ShopMenu(e.getPlayer(), e.getGameMatch(), e.getGameMatchPlayer());
            e.getPlayer().openInventory(shopMenu.getInventory());
        }
    }
}
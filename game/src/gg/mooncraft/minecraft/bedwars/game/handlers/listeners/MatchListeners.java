package gg.mooncraft.minecraft.bedwars.game.handlers.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameState;
import gg.mooncraft.minecraft.bedwars.data.map.point.PointTypes;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.GameConstants;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerJoinEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerQuitEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchUpdateGameEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchUpdateStateEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchVillagerInteractEvent;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchTeam;
import gg.mooncraft.minecraft.bedwars.game.match.PlayerStatus;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.GameMatchEvent;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.GeneratorTask;

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
                gameMatch.getScoreboard().unregister();

                for (GameMatchTeam gameMatchTeam : gameMatch.getTeamList()) {
                    for (GameMatchPlayer gameMatchPlayer : gameMatchTeam.getMatchPlayerList()) {
                        gameMatchPlayer.getPlayer().ifPresent(player -> {
                            TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(player.getUniqueId());
                            TabAPI.getInstance().getScoreboardManager().showScoreboard(tabPlayer, gameMatchTeam.getScoreboard());
                            TabAPI.getInstance().getTeamManager().setPrefix(tabPlayer, GameConstants.NAMETAG_FORMAT
                                    .replaceAll("%color%", gameMatchTeam.getGameTeam().getChatColor().toString())
                                    .replaceAll("%team%", String.valueOf(gameMatchTeam.getGameTeam().getLetter()))
                                    .replaceAll("%player%", player.getName())
                            );
                        });
                    }
                }
            }
            case ENDING -> {
                for (GameMatchTeam gameMatchTeam : gameMatch.getTeamList()) {
                    gameMatchTeam.getScoreboard().unregister();
                }
            }
        }

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
    public void on(@NotNull MatchPlayerJoinEvent e) {
        Player player = e.getPlayer();
        GameMatch gameMatch = e.getGameMatch();
        GameMatchTeam gameMatchTeam = e.getGameMatchTeam();

        // Clear chat
        for (int i = 0; i < 256; i++) {
            player.sendMessage(ChatColor.RESET + "");
        }

        // Teleport to spawnpoint
        gameMatch.getBedWarsMap()
                .flatMap(bedWarsMap -> bedWarsMap.getPointsContainer().getGameMapPoint(PointTypes.MAP.MAP_SPAWNPOINT))
                .ifPresent(gameMapPoint -> {
                    Location location = gameMatch.getDimension().getLocation(gameMapPoint.getX(), gameMapPoint.getY(), gameMapPoint.getZ(), gameMapPoint.getYaw(), gameMapPoint.getPitch());
                    BedWarsPlugin.getInstance().getScheduler().executeSync(() -> player.teleport(location));
                });

        // Show scoreboard and nametags
        BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
            TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(player.getUniqueId());
            if (gameMatch.getGameState() == GameState.WAITING) {
                TabAPI.getInstance().getScoreboardManager().showScoreboard(tabPlayer, gameMatch.getScoreboard());
            } else {
                TabAPI.getInstance().getScoreboardManager().showScoreboard(tabPlayer, gameMatchTeam.getScoreboard());
            }
            // Force update all the scoreboards
            gameMatch.getPlayerList().stream()
                    .map(streamPlayer -> TabAPI.getInstance().getPlayer(streamPlayer.getUniqueId()))
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
            if (gameMatch.getPlayersCount() == gameMatch.getPlayersCapacity()) {
                gameMatch.getGameTicker().getGameStartTask().play();
            }
        }
    }

    @EventHandler
    public void on(@NotNull MatchPlayerQuitEvent e) {
        Player player = e.getPlayer();
        GameMatch gameMatch = e.getGameMatch();

        // If the game is still waiting for players, a quit has to free team slot
        // Else the player must be set as a spectator
        if (gameMatch.getGameState() != GameState.WAITING) {
            GameMatchPlayer gameMatchPlayer = e.getGameMatchPlayer();
            gameMatchPlayer.setStatus(PlayerStatus.SPECTATING);
        } else {
            e.getGameMatchTeam().delPlayer(player.getUniqueId());
        }

        // Send quit message
        String quitMessage = PlaceholderAPI.setPlaceholders(player, GameConstants.MESSAGE_PLAYER_QUIT);
        player.sendMessage(quitMessage);
        gameMatch.getPlayerList().forEach(streamPlayer -> streamPlayer.sendMessage(quitMessage));
        // Force update all the scoreboards
        BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
            gameMatch.getPlayerList().stream()
                    .map(streamPlayer -> TabAPI.getInstance().getPlayer(streamPlayer.getUniqueId()))
                    .forEach(streamTabPlayer -> BedWarsPlugin.getInstance().getBoardManager().updateScoreboard(streamTabPlayer));
        });
        // Try to update GameStarTask if necessary
        if (gameMatch.getGameState() == GameState.WAITING) {
            gameMatch.getGameTicker().getGameStartTask().stop();
        }
    }

    @EventHandler
    public void on(@NotNull MatchVillagerInteractEvent e) {
        Inventory inventory = Bukkit.createInventory(null, 54, e.getMatchVillager().getVillagerType().getDisplay());
        e.getPlayer().openInventory(inventory);
    }
}
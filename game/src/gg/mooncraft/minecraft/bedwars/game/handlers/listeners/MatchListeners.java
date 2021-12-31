package gg.mooncraft.minecraft.bedwars.game.handlers.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.neznamy.tab.api.TabAPI;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameState;
import gg.mooncraft.minecraft.bedwars.data.map.point.PointTypes;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.GameConstants;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerJoinEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerQuitEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchUpdateStateEvent;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchTeam;
import gg.mooncraft.minecraft.bedwars.game.match.PlayerStatus;

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
                            TabAPI.getInstance().getScoreboardManager().showScoreboard(TabAPI.getInstance().getPlayer(player.getUniqueId()), gameMatchTeam.getScoreboard());
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
    public void on(@NotNull MatchPlayerJoinEvent e) {
        Player player = e.getPlayer();
        GameMatch gameMatch = e.getGameMatch();
        GameMatchTeam gameMatchTeam = e.getGameMatchTeam();

        // Teleport to spawnpoint
        gameMatch.getBedWarsMap()
                .flatMap(bedWarsMap -> bedWarsMap.getPointsContainer().getGameMapPoint(PointTypes.MAP.MAP_SPAWNPOINT))
                .ifPresent(gameMapPoint -> {
                    Location location = gameMatch.getDimension().getLocation(gameMapPoint.getX(), gameMapPoint.getY(), gameMapPoint.getZ(), gameMapPoint.getYaw(), gameMapPoint.getPitch());
                    BedWarsPlugin.getInstance().getScheduler().executeSync(() -> player.teleport(location));
                });

        // Show scoreboard
        BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
            if (gameMatch.getGameState() == GameState.WAITING) {
                TabAPI.getInstance().getScoreboardManager().showScoreboard(TabAPI.getInstance().getPlayer(player.getUniqueId()), gameMatch.getScoreboard());
            } else {
                TabAPI.getInstance().getScoreboardManager().showScoreboard(TabAPI.getInstance().getPlayer(player.getUniqueId()), gameMatchTeam.getScoreboard());
            }
        });

        // Send join message
        String joinMessage = PlaceholderAPI.setPlaceholders(player, GameConstants.MESSAGE_PLAYER_JOIN);
        player.sendMessage(joinMessage);
        gameMatch.getPlayerList().forEach(streamPlayer -> streamPlayer.sendMessage(joinMessage
                .replaceAll("%game-players-count%", String.valueOf(gameMatch.getPlayersCount()))
                .replaceAll("%game-players-capacity%", String.valueOf(gameMatch.getPlayersCapacity()))
        ));

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

        // Try to update GameStarTask if necessary
        if (gameMatch.getGameState() == GameState.WAITING) {
            gameMatch.getGameTicker().getGameStartTask().stop();
        }
    }
}
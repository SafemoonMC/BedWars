package gg.mooncraft.minecraft.bedwars.game.handlers.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.neznamy.tab.api.TabAPI;

import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameState;
import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.data.map.point.PointTypes;
import gg.mooncraft.minecraft.bedwars.data.map.point.TeamMapPoint;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.GameConstants;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerDeathEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerJoinEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerQuitEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchUpdateGameEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchUpdateStateEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchVillagerInteractEvent;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchTeam;
import gg.mooncraft.minecraft.bedwars.game.match.PlayerStatus;
import gg.mooncraft.minecraft.bedwars.game.match.VillagerType;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.GameMatchEvent;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.GeneratorTask;
import gg.mooncraft.minecraft.bedwars.game.menu.ShopMenu;
import gg.mooncraft.minecraft.bedwars.game.utilities.DisplayUtilities;
import gg.mooncraft.minecraft.bedwars.game.utilities.PointAdapter;

import java.util.ArrayList;
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
                            GameConstants.MESSAGE_STARTING_TIP.forEach(line -> player.sendMessage(Component.text(line)));
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
    public void on(@NotNull MatchPlayerJoinEvent e) {
        Player player = e.getPlayer();
        GameMatch gameMatch = e.getGameMatch();
        GameMatchPlayer gameMatchPlayer = e.getGameMatchPlayer();
        GameMatchTeam gameMatchTeam = e.getGameMatchTeam();

        // Clear chat
        for (int i = 0; i < 256; i++) {
            player.sendMessage(ChatColor.RESET + "");
        }

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
            gameMatchPlayer.setStatus(PlayerStatus.SPECTATING);
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
    public void on(@NotNull MatchPlayerDeathEvent e) {
        Player player = e.getPlayer();
        GameMatch gameMatch = e.getGameMatch();
        gameMatch.getPlayerList().forEach(streamPlayer -> {
            if (e.getReason() == MatchPlayerDeathEvent.Reason.PLAYER) {
                streamPlayer.sendMessage(Component.text(GameConstants.MESSAGE_PLAYER_KILL
                        .replaceAll("%killer%", e.getLastPlayerDamage().getPlayer().getName())
                        .replaceAll("%killed%", player.getName())
                        .replaceAll("%weapon%", DisplayUtilities.getDisplay(e.getLastPlayerDamage().getWeapon()))
                ));
            } else {
                streamPlayer.sendMessage(Component.text(GameConstants.MESSAGE_PLAYER_DIES
                        .replaceAll("%player%", player.getName())
                ));
            }
        });

        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 2, 2);
    }

    @EventHandler
    public void on(@NotNull MatchVillagerInteractEvent e) {
        if (e.getMatchVillager().getVillagerType() == VillagerType.ITEM_SHOP) {
            ShopMenu shopMenu = new ShopMenu(e.getGameMatch());
            e.getPlayer().openInventory(shopMenu.getInventory());
        }
    }
}
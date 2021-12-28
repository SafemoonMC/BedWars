package gg.mooncraft.minecraft.bedwars.game.handlers.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.map.point.PointTypes;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;

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
    public void on(@NotNull PlayerJoinEvent e) {
        Player player = e.getPlayer();
        BedWarsPlugin.getInstance().getMatchManager().getMatchList()
                .stream()
                .filter(gameMatch -> gameMatch.getTeamList().stream().anyMatch(gameMatchTeam -> gameMatchTeam.hasPlayer(player.getUniqueId())))
                .findFirst()
                .ifPresent(gameMatch -> gameMatch.getBedWarsMap().flatMap(bedWarsMap -> bedWarsMap.getPointsContainer().getGameMapPoint(PointTypes.MAP.MAP_SPAWNPOINT))
                        .ifPresent(gameMapPoint -> {
                                    Location location = gameMatch.getSlimeBukkitPair().getLocation(gameMapPoint.getX(), gameMapPoint.getY(), gameMapPoint.getZ(), gameMapPoint.getYaw(), gameMapPoint.getPitch());
                                    BedWarsPlugin.getInstance().getScheduler().executeSync(() -> player.teleport(location));
                                }
                        ));
    }
}
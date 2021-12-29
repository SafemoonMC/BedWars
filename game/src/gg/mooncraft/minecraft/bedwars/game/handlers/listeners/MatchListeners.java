package gg.mooncraft.minecraft.bedwars.game.handlers.listeners;

import me.neznamy.tab.api.TabAPI;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.map.point.PointTypes;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerJoinEvent;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchTeam;

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
    public void on(@NotNull MatchPlayerJoinEvent e) {
        Player player = e.getPlayer();
        GameMatch gameMatch = e.getGameMatch();
        GameMatchTeam gameMatchTeam = e.getGameMatchTeam();

        gameMatch.getBedWarsMap()
                .flatMap(bedWarsMap -> bedWarsMap.getPointsContainer().getGameMapPoint(PointTypes.MAP.MAP_SPAWNPOINT))
                .ifPresent(gameMapPoint -> {
                    Location location = gameMatch.getSlimeBukkitPair().getLocation(gameMapPoint.getX(), gameMapPoint.getY(), gameMapPoint.getZ(), gameMapPoint.getYaw(), gameMapPoint.getPitch());
                    BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
                        player.teleport(location);
                        TabAPI.getInstance().getScoreboardManager().showScoreboard(TabAPI.getInstance().getPlayer(player.getUniqueId()), gameMatchTeam.getScoreboard());
                    });
                });

    }
}
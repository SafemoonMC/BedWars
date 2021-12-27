package gg.mooncraft.minecraft.bedwars.game.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.common.messages.GameRequestMessage;
import gg.mooncraft.minecraft.bedwars.data.map.point.PointTypes;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;

import java.util.UUID;
import java.util.stream.Collectors;

public final class GameRequestManager {

    /*
    Methods
     */
    public void update(@NotNull GameRequestMessage.GameRequest gameRequest) {
        BedWarsPlugin.getInstance().getMatchManager().findMatch(gameRequest.getGameMode(), gameRequest.getPlayerList())
                .thenAccept(gameMatch -> {
                    // Logging
                    String playerListJoin = gameRequest.getPlayerList().stream().map(UUID::toString).collect(Collectors.joining(", "));
                    BedWarsPlugin.getInstance().getLogger().info(String.format("[GameRequest] [Match] Matchmaking [%s] to %s.", playerListJoin, gameMatch.getId()));

                    // Send actually online players to the world
                    gameMatch.getBedWarsMap().flatMap(bedWarsMap -> bedWarsMap.getPointsContainer().getGameMapPoint(PointTypes.MAP.MAP_SPAWNPOINT)).ifPresent(gameMapPoint ->
                            gameRequest.getPlayerList().forEach(uuid -> {
                                Player player = Bukkit.getPlayer(uuid);
                                if (player == null) return;
                                Location location = gameMatch.getSlimeBukkitPair().getLocation(gameMapPoint.getX(), gameMapPoint.getY(), gameMapPoint.getZ(), gameMapPoint.getYaw(), gameMapPoint.getPitch());
                                player.teleport(location);
                            })
                    );
                });
    }
}
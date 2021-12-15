package gg.mooncraft.minecraft.bedwars.game.managers;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.MapDAO;
import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class MapManager {

    /*
    Fields
     */
    private final @NotNull List<BedWarsMap> mapList = new ArrayList<>();
    private final @NotNull Map<String, SlimeBukkitPair> mapWorldsMap = new HashMap<>();

    /*
    Constructor
     */
    public MapManager() {
        BedWarsPlugin.getInstance().getLogger().info("Loading MapManager...");
        loadMaps();
        BedWarsPlugin.getInstance().getLogger().info("MapManager has been loaded!");
    }


    /*
    Methods
     */
    private void loadMaps() {
        MapDAO.read()
                .thenApply(list -> list
                        .stream()
                        .map(bedWarsMap -> BedWarsPlugin.getInstance().getSlimeManager().loadPairAsync(bedWarsMap.getIdentifier()).thenAccept(slimeBukkitPair -> {
                            storeMap(bedWarsMap.getIdentifier(), bedWarsMap, slimeBukkitPair);
                            BedWarsPlugin.getInstance().getLogger().info("[Map] " + bedWarsMap.getIdentifier() + " has been loaded...");
                        }))
                        .toArray(CompletableFuture[]::new))
                .thenAccept(completableFutures -> {
                    CompletableFuture.allOf(completableFutures)
                            .thenAccept(v -> BedWarsPlugin.getInstance().getGameServerManager().sendGameServerMessage());
                });
    }

    public void storeMap(@NotNull String name, @NotNull BedWarsMap bedWarsMap, @NotNull SlimeBukkitPair slimeBukkitPair) {
        this.mapList.add(bedWarsMap);
        this.mapWorldsMap.put(name, slimeBukkitPair);
    }
}
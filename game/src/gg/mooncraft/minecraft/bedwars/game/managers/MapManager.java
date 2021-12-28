package gg.mooncraft.minecraft.bedwars.game.managers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.MapDAO;
import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.slime.SlimeBukkitPair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public final class MapManager {

    /*
    Fields
     */
    private final @NotNull List<BedWarsMap> mapList = new ArrayList<>();
    private final @NotNull Map<String, SlimeBukkitPair> worldsMap = new HashMap<>();

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
                        .map(bedWarsMap -> BedWarsPlugin.getInstance().getSlimeManager().readPairAsync(bedWarsMap.getIdentifier()).thenAccept(slimeBukkitPair -> {
                            // Caches the map
                            this.mapList.add(bedWarsMap);
                            this.worldsMap.put(bedWarsMap.getIdentifier(), slimeBukkitPair);

                            BedWarsPlugin.getInstance().getLogger().info("[Map] " + bedWarsMap.getIdentifier() + " has been loaded...");
                        }))
                        .toArray(CompletableFuture[]::new))
                .thenAccept(completableFutures -> {
                    CompletableFuture.allOf(completableFutures)
                            .thenAccept(v -> BedWarsPlugin.getInstance().getGameServerManager().sendGameServerMessage());
                });
    }

    public void storeMap(@NotNull String mapName, @NotNull BedWarsMap bedWarsMap, @NotNull SlimeBukkitPair slimeBukkitPair) {
        this.mapList.add(bedWarsMap);
        this.worldsMap.put(mapName, slimeBukkitPair);
    }

    public @NotNull CompletableFuture<Boolean> deleteMap(@NotNull String mapName) {
        Optional<BedWarsMap> optionalBedWarsMap = getBedWarsMap(mapName);
        Optional<SlimeBukkitPair> optionalSlimeBukkitPair = getSlimeBukkitPair(mapName);
        if (optionalBedWarsMap.isEmpty() || optionalSlimeBukkitPair.isEmpty()) {
            return CompletableFuture.completedFuture(false);
        }
        BedWarsMap bedWarsMap = optionalBedWarsMap.get();
        SlimeBukkitPair slimeBukkitPair = optionalSlimeBukkitPair.get();

        CompletableFuture<?> futureMapDelete = MapDAO.delete(bedWarsMap);
        CompletableFuture<?> futureWorldDelete = BedWarsPlugin.getInstance().getSlimeManager().deletePairAsync(slimeBukkitPair);
        return CompletableFuture.allOf(futureMapDelete, futureWorldDelete).thenApply(v -> {
            this.mapList.remove(bedWarsMap);
            this.worldsMap.remove(bedWarsMap.getIdentifier());
            return true;
        });
    }

    public @NotNull Optional<BedWarsMap> getBedWarsMap(@NotNull String mapName) {
        return this.mapList.stream().filter(bedWarsMap -> bedWarsMap.getIdentifier().equalsIgnoreCase(mapName)).findFirst();
    }

    public @NotNull Optional<SlimeBukkitPair> getSlimeBukkitPair(@NotNull String mapName) {
        return this.worldsMap.entrySet().stream().filter(entry -> entry.getKey().equalsIgnoreCase(mapName)).findFirst().map(Map.Entry::getValue);
    }

    public @NotNull Optional<BedWarsMap> getRandomMap(@NotNull GameMode gameMode) {
        List<BedWarsMap> mapList = getMapList().stream().filter(bedWarsMap -> bedWarsMap.getGameModeSet().contains(gameMode)).collect(Collectors.toList());
        if (mapList.isEmpty()) return Optional.empty();

        return Optional.of(mapList.get(ThreadLocalRandom.current().nextInt(mapList.size())));
    }

    @UnmodifiableView
    public @NotNull List<BedWarsMap> getMapList() {
        return Collections.unmodifiableList(this.mapList);
    }

    @UnmodifiableView
    public @NotNull Map<String, SlimeBukkitPair> getWorldsMap() {
        return Collections.unmodifiableMap(this.worldsMap);
    }
}
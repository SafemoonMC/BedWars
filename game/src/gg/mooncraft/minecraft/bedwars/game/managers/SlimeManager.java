package gg.mooncraft.minecraft.bedwars.game.managers;

import com.github.benmanes.caffeine.cache.Cache;
import com.google.common.base.Stopwatch;
import com.grinderwolf.swm.api.world.SlimeWorld;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.common.utilities.CaffeineFactory;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.slime.SlimeBukkitPair;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class SlimeManager {

    /*
    Fields
     */
    private final @NotNull Cache<String, SlimeWorld> slimeWorldCache = CaffeineFactory.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS).build();

    /*
    Methods
     */
    public @NotNull CompletableFuture<SlimeBukkitPair> createPairAsync(@NotNull String worldName) {
        Stopwatch createStopwatch = Stopwatch.createStarted();
        Stopwatch generateStopwatch = Stopwatch.createUnstarted();

        return BedWarsPlugin.getAsyncSlimePlugin().createWorld(worldName, false).thenApply(slimeWorld -> {
            long createElapsed = createStopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
            CompletableFuture<SlimeBukkitPair> completableFuture = new CompletableFuture<>();

            generateStopwatch.start();
            loadBukkitWorld(slimeWorld, world -> {
                long generateElapsed = generateStopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
                BedWarsPlugin.getInstance().getLogger().info("[SlimeManager] Created " + worldName + " in " + createElapsed + "ms and generated in " + generateElapsed + "ms.");

                completableFuture.complete(new SlimeBukkitPair(slimeWorld, world, true));
            });
            return completableFuture.join();
        });
    }

    public @NotNull CompletableFuture<SlimeBukkitPair> createTemporaryPairAsync(@NotNull String fromWorldName, @NotNull String toWorldName) {
        Stopwatch copyStopwatch = Stopwatch.createStarted();
        Stopwatch generateStopwatch = Stopwatch.createUnstarted();
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<SlimeBukkitPair> completableFuture = new CompletableFuture<>();

            // Get the SlimeWorld from the cache (or load from database) and clone the world
            SlimeWorld fromSlimeWorld = slimeWorldCache.get(fromWorldName, worldName -> BedWarsPlugin.getAsyncSlimePlugin().loadWorld(fromWorldName, true).join());
            SlimeWorld slimeWorld = fromSlimeWorld.clone(toWorldName);

            long loadElapsed = copyStopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
            generateStopwatch.start();
            loadBukkitWorld(slimeWorld, world -> {
                long generateElapsed = generateStopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
                BedWarsPlugin.getInstance().getLogger().info("[SlimeManager] Copied " + fromWorldName + " to " + toWorldName + " in " + loadElapsed + "ms and generated in " + generateElapsed + "ms with " + world.getLoadedChunks().length + " preloaded chunks.");

                completableFuture.complete(new SlimeBukkitPair(slimeWorld, world, false));
            });

            return completableFuture.join();
        });
    }

    public @NotNull CompletableFuture<SlimeBukkitPair> readPairAsync(@NotNull String worldName) {
        Stopwatch loadStopwatch = Stopwatch.createStarted();
        Stopwatch generateStopwatch = Stopwatch.createUnstarted();
        return BedWarsPlugin.getAsyncSlimePlugin().loadWorld(worldName, false).thenApply(slimeWorld -> {
            long loadElapsed = loadStopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
            CompletableFuture<SlimeBukkitPair> completableFuture = new CompletableFuture<>();

            generateStopwatch.start();
            loadBukkitWorld(slimeWorld, world -> {
                long generateElapsed = generateStopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
                BedWarsPlugin.getInstance().getLogger().info("[SlimeManager] Loaded " + worldName + " in " + loadElapsed + "ms and generated in " + generateElapsed + "ms with " + world.getLoadedChunks().length + " preloaded chunks.");

                completableFuture.complete(new SlimeBukkitPair(slimeWorld, world, true));
            });

            return completableFuture.join();
        });
    }

    public @NotNull CompletableFuture<Boolean> unloadPairAsync(@NotNull SlimeBukkitPair slimeBukkitPair) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
            boolean unloaded = Bukkit.unloadWorld(slimeBukkitPair.world(), slimeBukkitPair.persistent());
            if (unloaded) {
                if (slimeBukkitPair.persistent()) {
                    BedWarsPlugin.getAsyncSlimeLoader().unlockWorld(slimeBukkitPair.slimeWorld().getName()).join();
                }
                completableFuture.complete(true);
            } else {
                BedWarsPlugin.getInstance().getLogger().warning("[SlimeManager] The world " + slimeBukkitPair.slimeWorld().getName() + " cannot be unloaded.");
                completableFuture.complete(false);
            }
        });
        return completableFuture;
    }

    public @NotNull CompletableFuture<Boolean> deletePairAsync(@NotNull SlimeBukkitPair slimeBukkitPair) {
        Bukkit.unloadWorld(slimeBukkitPair.world(), false);
        return BedWarsPlugin.getAsyncSlimeLoader().deleteWorld(slimeBukkitPair.world().getName());
    }

    public void loadBukkitWorld(@NotNull SlimeWorld slimeWorld, @NotNull Consumer<World> worldConsumer) {
        BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
            BedWarsPlugin.getAsyncSlimePlugin().getSync().generateWorld(slimeWorld);
            World newWorld = Bukkit.getWorld(slimeWorld.getName());

            // Force-load 400 chunks
            for (int x = -10; x < 10; x++) {
                for (int z = -10; z < 10; z++) {
                    newWorld.addPluginChunkTicket(x, z, BedWarsPlugin.getInstance());
                }
            }

            // Setup game rules
            newWorld.setFullTime(24000);
            newWorld.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
            newWorld.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            newWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            newWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            newWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            newWorld.setGameRule(GameRule.DO_FIRE_TICK, false);
            newWorld.setGameRule(GameRule.DO_MOB_LOOT, false);
            newWorld.setGameRule(GameRule.DO_TILE_DROPS, false);
            newWorld.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
            newWorld.setGameRule(GameRule.NATURAL_REGENERATION, true);
            newWorld.setGameRule(GameRule.REDUCED_DEBUG_INFO, true);
            worldConsumer.accept(newWorld);
        });
    }
}
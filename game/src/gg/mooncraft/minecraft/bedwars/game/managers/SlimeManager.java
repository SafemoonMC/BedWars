package gg.mooncraft.minecraft.bedwars.game.managers;

import com.google.common.base.Stopwatch;
import com.grinderwolf.swm.api.world.SlimeWorld;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.slime.AsyncSlimeWorld;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class SlimeManager {

    /*
    Methods
     */
    public @NotNull CompletableFuture<SlimeBukkitPair> createPairAsync(@NotNull String worldName) {
        Stopwatch createStopwatch = Stopwatch.createStarted();
        Stopwatch generateStopwatch = Stopwatch.createUnstarted();

        return BedWarsPlugin.getAsyncSlimePlugin().createWorld(worldName, true).thenApply(slimeWorld -> {
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

    public @NotNull CompletableFuture<SlimeBukkitPair> readPairAsync(@NotNull String worldName) {
        Stopwatch loadStopwatch = Stopwatch.createStarted();
        Stopwatch generateStopwatch = Stopwatch.createUnstarted();
        return BedWarsPlugin.getAsyncSlimePlugin().loadWorld(worldName, true).thenApply(slimeWorld -> {
            long loadElapsed = loadStopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
            CompletableFuture<SlimeBukkitPair> completableFuture = new CompletableFuture<>();

            generateStopwatch.start();
            loadBukkitWorld(slimeWorld, world -> {
                long generateElapsed = generateStopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
                BedWarsPlugin.getInstance().getLogger().info("[SlimeManager] Loaded " + worldName + " in " + loadElapsed + "ms and generated in " + generateElapsed + "ms.");

                completableFuture.complete(new SlimeBukkitPair(slimeWorld, world, true));
            });

            return completableFuture.join();
        });
    }

//    public @NotNull CompletableFuture<SlimeBukkitPair> loadPairAsync(@NotNull String worldName) {
//        return CompletableFuture.supplyAsync(() -> {
//            try {
//                CompletableFuture<SlimeBukkitPair> completableFuture = new CompletableFuture<>();
//
//                Stopwatch stopwatch = Stopwatch.createStarted();
//                SlimeWorld slimeWorld = BedWarsPlugin.getAsyncSlimePlugin().loadWorld(slimeLoader, worldName, false, getSlimePropertyMap(0, 256, 0));
//                loadBukkitWorld(slimeWorld, world -> {
//                    SlimeBukkitPair slimeBukkitPair = new SlimeBukkitPair(slimeWorld, world, true);
//                    completableFuture.complete(slimeBukkitPair);
//
//                    long elapsed = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
//                    BedWarsPlugin.getInstance().getLogger().info(worldName + " world has been generated in " + elapsed + "ms.");
//                });
//
//                return completableFuture.join();
//            } catch (Exception e) {
//                BedWarsPlugin.getInstance().getLogger().warning("The world cannot be loaded. Exception: " + e.getMessage());
//                return null;
//            }
//        });
//    }

    public @NotNull CompletableFuture<SlimeBukkitPair> loadTemporaryPairAsync(@NotNull SlimeBukkitPair fromBukkitPair, @NotNull String newWorldName) {
        Stopwatch copyStopwatch = Stopwatch.createStarted();
        Stopwatch generateStopwatch = Stopwatch.createUnstarted();
        return AsyncSlimeWorld.copy(fromBukkitPair.slimeWorld(), newWorldName).thenApply(slimeWorld -> {
            long loadElapsed = copyStopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
            CompletableFuture<SlimeBukkitPair> completableFuture = new CompletableFuture<>();

            generateStopwatch.start();
            loadBukkitWorld(slimeWorld, world -> {
                long generateElapsed = generateStopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
                BedWarsPlugin.getInstance().getLogger().info("[SlimeManager] Copied " + fromBukkitPair.world().getName() + " to " + newWorldName + " in " + loadElapsed + "ms and generated in " + generateElapsed + "ms.");

                completableFuture.complete(new SlimeBukkitPair(slimeWorld, world, false));
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
            worldConsumer.accept(newWorld);
        });
    }
}
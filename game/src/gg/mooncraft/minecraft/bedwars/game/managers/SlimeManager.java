package gg.mooncraft.minecraft.bedwars.game.managers;

import lombok.Getter;

import com.google.common.base.Stopwatch;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Getter
public final class SlimeManager {

    /*
    Fields
     */
    private final @NotNull SlimeLoader slimeLoader;

    /*
    Constructor
     */
    public SlimeManager() {
        this.slimeLoader = BedWarsPlugin.getSlimePlugin().getLoader("mysql");
    }

    /*
    Methods
     */
    public @NotNull CompletableFuture<SlimeBukkitPair> loadPairAsync(@NotNull String worldName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                CompletableFuture<SlimeBukkitPair> completableFuture = new CompletableFuture<>();

                Stopwatch stopwatch = Stopwatch.createStarted();
                SlimeWorld slimeWorld = BedWarsPlugin.getSlimePlugin().loadWorld(slimeLoader, worldName, true, getSlimePropertyMap(0, 256, 0));
                loadBukkitWorld(slimeWorld, world -> {
                    SlimeBukkitPair slimeBukkitPair = new SlimeBukkitPair(slimeWorld, world, true);
                    completableFuture.complete(slimeBukkitPair);

                    long elapsed = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
                    BedWarsPlugin.getInstance().getLogger().info(worldName + " world has been generated in " + elapsed + "ms.");
                });

                return completableFuture.join();
            } catch (Exception e) {
                BedWarsPlugin.getInstance().getLogger().warning("The world cannot be loaded. Exception: " + e.getMessage());
                return null;
            }
        });
    }

    public @NotNull CompletableFuture<SlimeBukkitPair> loadTemporaryPairAsync(@NotNull SlimeBukkitPair fromBukkitPair, @NotNull String newWorldName) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<SlimeBukkitPair> completableFuture = new CompletableFuture<>();

            Stopwatch stopwatch = Stopwatch.createStarted();
            SlimeWorld newSlimeWorld = fromBukkitPair.slimeWorld().clone(newWorldName);
            loadBukkitWorld(newSlimeWorld, world -> {
                SlimeBukkitPair newSlimeBukkitPair = new SlimeBukkitPair(newSlimeWorld, world, false);
                completableFuture.complete(newSlimeBukkitPair);

                long elapsed = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
                BedWarsPlugin.getInstance().getLogger().info(newWorldName + " world has been generated in " + elapsed + "ms.");
            });

            return completableFuture.join();
        });
    }

    public @NotNull CompletableFuture<Boolean> unloadPairAsync(@NotNull SlimeBukkitPair slimeBukkitPair) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
            boolean unloaded = Bukkit.unloadWorld(slimeBukkitPair.world(), false);
            if (unloaded) {
                if (slimeBukkitPair.persistent()) {
                    BedWarsPlugin.getInstance().getScheduler().executeAsync(() -> {
                        try {
                            getSlimeLoader().unlockWorld(slimeBukkitPair.slimeWorld().getName());
                            completableFuture.complete(true);
                        } catch (Exception e) {
                            BedWarsPlugin.getInstance().getLogger().warning("The world " + slimeBukkitPair.slimeWorld().getName() + " cannot be unlocked. Exception: " + e.getMessage());
                            completableFuture.complete(false);
                        }
                    });
                }
            } else {
                BedWarsPlugin.getInstance().getLogger().warning("The world " + slimeBukkitPair.slimeWorld().getName() + " cannot be unloaded.");
                completableFuture.complete(false);
            }
        });
        return completableFuture;
    }

    public @NotNull CompletableFuture<SlimeBukkitPair> createPairAsync(@NotNull String worldName) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<SlimeBukkitPair> completableFuture = new CompletableFuture<>();

            try {
                SlimeWorld newSlimeWorld = BedWarsPlugin.getSlimePlugin().createEmptyWorld(getSlimeLoader(), worldName, false, getSlimePropertyMap(0, 255, 0));
                loadBukkitWorld(newSlimeWorld, world -> {
                    SlimeBukkitPair newSlimeBukkitPair = new SlimeBukkitPair(newSlimeWorld, world, true);
                    completableFuture.complete(newSlimeBukkitPair);
                });
            } catch (Exception e) {
                completableFuture.complete(null);
                BedWarsPlugin.getInstance().getLogger().warning("The world " + worldName + " cannot be created! Exception: " + e.getMessage());
            }

            return completableFuture.join();
        });
    }

    public void loadBukkitWorld(@NotNull SlimeWorld slimeWorld, @NotNull Consumer<World> worldConsumer) {
        BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
            BedWarsPlugin.getSlimePlugin().generateWorld(slimeWorld);
            World newWorld = Bukkit.getWorld(slimeWorld.getName());
            worldConsumer.accept(newWorld);
        });
    }

    /**
     * It creates a new SlimePropertyMap instance with:
     * DIFFICULTY       = normal
     * ALLOW_ANIMALS    = false
     * ALLOW_MONSTERS   = false
     *
     * @return a new SlimePropertyMap with hardcoded properties
     */
    private @NotNull SlimePropertyMap getSlimePropertyMap(int x, int y, int z) {
        SlimePropertyMap properties = new SlimePropertyMap();
        properties.setValue(SlimeProperties.DIFFICULTY, "normal");
        properties.setValue(SlimeProperties.SPAWN_X, x);
        properties.setValue(SlimeProperties.SPAWN_Y, y);
        properties.setValue(SlimeProperties.SPAWN_Z, z);
        properties.setValue(SlimeProperties.ALLOW_ANIMALS, false);
        properties.setValue(SlimeProperties.ALLOW_MONSTERS, false);
        properties.setValue(SlimeProperties.PVP, true);
        return properties;
    }
}
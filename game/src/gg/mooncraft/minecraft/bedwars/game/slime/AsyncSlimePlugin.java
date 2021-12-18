package gg.mooncraft.minecraft.bedwars.game.slime;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

@AllArgsConstructor
public final class AsyncSlimePlugin {

    /*
    Constants
     */
    private static final @NotNull SlimePropertyMap SLIME_PROPERTY_MAP = new SlimePropertyMap();

    static {
        SLIME_PROPERTY_MAP.setValue(SlimeProperties.DIFFICULTY, "normal");
        SLIME_PROPERTY_MAP.setValue(SlimeProperties.SPAWN_X, 0);
        SLIME_PROPERTY_MAP.setValue(SlimeProperties.SPAWN_Y, 256);
        SLIME_PROPERTY_MAP.setValue(SlimeProperties.SPAWN_Z, 0);
        SLIME_PROPERTY_MAP.setValue(SlimeProperties.ALLOW_ANIMALS, false);
        SLIME_PROPERTY_MAP.setValue(SlimeProperties.ALLOW_MONSTERS, false);
        SLIME_PROPERTY_MAP.setValue(SlimeProperties.PVP, true);
    }

    /*
    Fields
     */
    @Getter
    private final @NotNull SlimePlugin sync;
    private final @NotNull ForkJoinPool forkJoinPool = new ForkJoinPool();

    /*
    Methods
     */
    public @NotNull CompletableFuture<SlimeWorld> createWorld(@NotNull String worldName, boolean readOnly) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return this.sync.createEmptyWorld(BedWarsPlugin.getAsyncSlimeLoader().getSync(), worldName, readOnly, SLIME_PROPERTY_MAP);
            } catch (Exception e) {
                BedWarsPlugin.getInstance().getLogger().info("[AsyncSlimePlugin] Error while creating world " + worldName + ": " + e.getMessage());
                return null;
            }
        }, this.forkJoinPool);
    }

    public @NotNull CompletableFuture<SlimeWorld> loadWorld(@NotNull String worldName, boolean readOnly) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return this.sync.loadWorld(BedWarsPlugin.getAsyncSlimeLoader().getSync(), worldName, readOnly, SLIME_PROPERTY_MAP);
            } catch (Exception e) {
                BedWarsPlugin.getInstance().getLogger().info("[AsyncSlimePlugin] Error while loading world " + worldName + ": " + e.getMessage());
                return null;
            }
        }, this.forkJoinPool);
    }
}
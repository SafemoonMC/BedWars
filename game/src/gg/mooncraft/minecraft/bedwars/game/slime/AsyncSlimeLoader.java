package gg.mooncraft.minecraft.bedwars.game.slime;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.grinderwolf.swm.api.loaders.SlimeLoader;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

@AllArgsConstructor
public final class AsyncSlimeLoader {

    /*
    Fields
     */
    @Getter
    private final @NotNull SlimeLoader sync;
    private final @NotNull ForkJoinPool forkJoinPool = new ForkJoinPool();

    /*
    Methods
     */
    public @NotNull CompletableFuture<Boolean> unlockWorld(@NotNull String worldName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                this.sync.unlockWorld(worldName);
                return true;
            } catch (Exception e) {
                BedWarsPlugin.getInstance().getLogger().info("[AsyncSlimeLoader] Error while unlocking world " + worldName + ": " + e.getMessage());
                return false;
            }
        }, this.forkJoinPool);
    }

    public @NotNull CompletableFuture<Boolean> deleteWorld(@NotNull String worldName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                this.sync.deleteWorld(worldName);
                return true;
            } catch (Exception e) {
                BedWarsPlugin.getInstance().getLogger().info("[AsyncSlimeLoader] Error while deleting world " + worldName + ": " + e.getMessage());
                return false;
            }
        }, this.forkJoinPool);
    }
}
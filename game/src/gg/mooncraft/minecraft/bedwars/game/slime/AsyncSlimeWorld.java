package gg.mooncraft.minecraft.bedwars.game.slime;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.grinderwolf.swm.api.world.SlimeWorld;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class AsyncSlimeWorld {

    public static @NotNull CompletableFuture<SlimeWorld> copy(@NotNull SlimeWorld fromSlimeWorld, @NotNull String toWorldName) {
        return CompletableFuture.supplyAsync(() -> fromSlimeWorld.clone(toWorldName));
    }
}
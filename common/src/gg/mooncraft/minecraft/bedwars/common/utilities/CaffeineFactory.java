package gg.mooncraft.minecraft.bedwars.common.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.github.benmanes.caffeine.cache.Caffeine;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class CaffeineFactory {

    /*
    Constants
     */
    private static final @NotNull ForkJoinPool FORK_JOIN_POOL = new ForkJoinPool();

    /*
    Static Methods
     */
    public static @NotNull Caffeine<Object, Object> newBuilder() {
        return Caffeine.newBuilder().executor(FORK_JOIN_POOL);
    }

    public static @NotNull Executor executor() {
        return FORK_JOIN_POOL;
    }
}
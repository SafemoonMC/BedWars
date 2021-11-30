package gg.mooncraft.minecraft.bedwars.common.utilities;

import com.github.benmanes.caffeine.cache.Cache;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A list implementing Caffeine cache
 *
 * @param <E> element type
 */
public class CaffeineList<E> {
    /*
    Fields
     */
    private final @NotNull Cache<E, Long> cache;
    private final long lifetime;

    /*
    Constructor
     */
    public CaffeineList(long duration, @NotNull TimeUnit unit) {
        this.cache = CaffeineFactory.newBuilder().expireAfterWrite(duration, unit).build();
        this.lifetime = unit.toMillis(duration);
    }

    /*
    Methods
     */
    public boolean add(@NotNull E item) {
        boolean present = contains(item);
        this.cache.put(item, System.currentTimeMillis() + this.lifetime);
        return !present;
    }

    public void remove(@NotNull E item) {
        this.cache.invalidate(item);
    }

    public boolean contains(@NotNull E item) {
        Long timeout = this.cache.getIfPresent(item);
        return timeout != null && timeout > System.currentTimeMillis();
    }
}
package gg.mooncraft.minecraft.bedwars.common.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class BukkitRedisUtilities {

    public static @NotNull HostAndPort parseHostAndPort(@NotNull ConfigurationSection configurationSection) {
        return new HostAndPort(configurationSection.getString("hostname"), configurationSection.getInt("port"));
    }

    public static @NotNull JedisPoolConfig parsePoolConfig(@NotNull ConfigurationSection configurationSection) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMinIdle(configurationSection.getInt("min-idle"));
        jedisPoolConfig.setMaxIdle(configurationSection.getInt("max-idle"));
        jedisPoolConfig.setMaxTotal(configurationSection.getInt("max-total"));
        jedisPoolConfig.setNumTestsPerEvictionRun(configurationSection.getInt("num-tests-per-eviction-run"));
        jedisPoolConfig.setMinEvictableIdleTime(Duration.ofMillis(configurationSection.getLong("min-evictable-idle-time-millis")));
        jedisPoolConfig.setTimeBetweenEvictionRuns(Duration.ofMillis(configurationSection.getLong("time-between-eviction-runs-millis")));
        return jedisPoolConfig;
    }
}
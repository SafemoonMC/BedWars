package gg.mooncraft.minecraft.bedwars.lobby;

import me.eduardwayland.mooncraft.waylander.database.Credentials;
import me.eduardwayland.mooncraft.waylander.database.Database;
import me.eduardwayland.mooncraft.waylander.database.connection.hikari.impl.MariaDBConnectionFactory;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.common.ComplexJavaPlugin;
import gg.mooncraft.minecraft.bedwars.common.messaging.RedisChannel;
import gg.mooncraft.minecraft.bedwars.common.messaging.RedisMessenger;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;

public class BedWarsPlugin extends ComplexJavaPlugin {


    /*
    Override Methods
     */
    @Override
    public void onEnable() {

        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {
        super.shutdown();
        getLogger().info("Disabled!");
    }

    @Override
    public @NotNull Database createDatabase(@NotNull Credentials credentials) {
        return Database.builder()
                .identifier(getName())
                .scheduler(getScheduler())
                .connectionFactory(new MariaDBConnectionFactory(getName(), credentials))
                .build();
    }

    @Override
    public @NotNull RedisMessenger createRedisMessenger(@NotNull JedisPoolConfig jedisPoolConfig, @NotNull HostAndPort hostAndPort, @NotNull String username, @NotNull String password) {
        RedisMessenger redisMessenger = new RedisMessenger(this, new LobbyRedisMessenger(this));
        redisMessenger.init(RedisChannel.LOBBY, jedisPoolConfig, hostAndPort, username, password);
        return redisMessenger;
    }
}
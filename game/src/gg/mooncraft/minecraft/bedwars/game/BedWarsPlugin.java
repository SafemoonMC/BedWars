package gg.mooncraft.minecraft.bedwars.game;

import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.database.Credentials;
import me.eduardwayland.mooncraft.waylander.database.Database;
import me.eduardwayland.mooncraft.waylander.database.connection.hikari.impl.MariaDBConnectionFactory;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.common.ComplexJavaPlugin;
import gg.mooncraft.minecraft.bedwars.common.messaging.RedisChannel;
import gg.mooncraft.minecraft.bedwars.common.messaging.RedisMessenger;
import gg.mooncraft.minecraft.bedwars.game.messaging.GameRedisMessenger;
import gg.mooncraft.minecraft.bedwars.game.utilities.ServerUtilities;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;

@Getter
public class BedWarsPlugin extends ComplexJavaPlugin {

    /*
    Fields
     */
    private final String serverName = ServerUtilities.getProperty("server-name");

    /*
    Override Methods
     */
    @Override
    public void onEnable() {
        // Show startup information
        getLogger().info("Database: " + (getDatabase() != null ? getDatabase().getIdentifier() : "not started"));
        getLogger().info("Messenger: " + (getMessenger() != null ? ((RedisMessenger) getMessenger()).isClosed() ? "closed" : "running" : "not started"));



        // Show enabling information
        getLogger().info("Running on " + serverName + "...");
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
        RedisMessenger redisMessenger = new RedisMessenger(this, new GameRedisMessenger(this));
        redisMessenger.init(RedisChannel.LOBBY, jedisPoolConfig, hostAndPort, username, password);
        return redisMessenger;
    }

    /*
    Static Methods
     */
    public static @NotNull BedWarsPlugin getInstance() {
        return BedWarsPlugin.getPlugin(BedWarsPlugin.class);
    }
}
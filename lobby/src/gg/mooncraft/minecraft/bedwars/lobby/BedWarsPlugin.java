package gg.mooncraft.minecraft.bedwars.lobby;

import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.database.Credentials;
import me.eduardwayland.mooncraft.waylander.database.Database;
import me.eduardwayland.mooncraft.waylander.database.connection.hikari.impl.MariaDBConnectionFactory;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.common.ComplexJavaPlugin;
import gg.mooncraft.minecraft.bedwars.common.messaging.RedisChannel;
import gg.mooncraft.minecraft.bedwars.common.messaging.RedisMessenger;
import gg.mooncraft.minecraft.bedwars.data.UserDAO;
import gg.mooncraft.minecraft.bedwars.lobby.factory.UserFactory;
import gg.mooncraft.minecraft.bedwars.lobby.handlers.commands.Commands;
import gg.mooncraft.minecraft.bedwars.lobby.handlers.listeners.PlayerListeners;
import gg.mooncraft.minecraft.bedwars.lobby.managers.GameServerManager;
import gg.mooncraft.minecraft.bedwars.lobby.messaging.LobbyRedisMessenger;
import gg.mooncraft.minecraft.bedwars.lobby.papi.BedWarsExpansion;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;

@Getter
public class BedWarsPlugin extends ComplexJavaPlugin {

    /*
    Fields
     */
    private UserFactory userFactory;
    private GameServerManager gameServerManager;

    /*
    Override Methods
     */
    @Override
    public void onEnable() {
        // Show startup information
        getLogger().info("Database: " + (getDatabase() != null ? "running for " + getDatabase().getIdentifier() : "not started"));
        getLogger().info("Messenger: " + (getMessenger() != null ? ((RedisMessenger) getMessenger()).isClosed() ? "closed" : "running" : "not started"));

        // Stop server if database or messenger are not loaded
        if (getDatabase() == null || getMessenger() == null || ((RedisMessenger) getMessenger()).isClosed()) {
            setEnabled(false);
            return;
        }

        // Load dependencies
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new BedWarsExpansion().register();
            getLogger().info("PlaceholderAPI has been found and BedWars expansion has been registered.");
        }

        // Load daos
        UserDAO.registerDAO(getDatabase());

        // Load factories
        this.userFactory = new UserFactory();

        // Load managers
        this.gameServerManager = new GameServerManager();

        // Listeners
        new PlayerListeners();

        // Commands
        Commands.loadAll();

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

    /*
    Static Methods
     */
    public static @NotNull BedWarsPlugin getInstance() {
        return BedWarsPlugin.getPlugin(BedWarsPlugin.class);
    }
}
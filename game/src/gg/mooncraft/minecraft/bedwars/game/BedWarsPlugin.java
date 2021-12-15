package gg.mooncraft.minecraft.bedwars.game;

import lombok.Getter;

import com.grinderwolf.swm.api.SlimePlugin;

import me.eduardwayland.mooncraft.waylander.database.Credentials;
import me.eduardwayland.mooncraft.waylander.database.Database;
import me.eduardwayland.mooncraft.waylander.database.connection.hikari.impl.MariaDBConnectionFactory;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.common.ComplexJavaPlugin;
import gg.mooncraft.minecraft.bedwars.common.messaging.RedisChannel;
import gg.mooncraft.minecraft.bedwars.common.messaging.RedisMessenger;
import gg.mooncraft.minecraft.bedwars.data.MapDAO;
import gg.mooncraft.minecraft.bedwars.data.UserDAO;
import gg.mooncraft.minecraft.bedwars.game.handlers.commands.Commands;
import gg.mooncraft.minecraft.bedwars.game.handlers.listeners.SetupListeners;
import gg.mooncraft.minecraft.bedwars.game.managers.GameServerManager;
import gg.mooncraft.minecraft.bedwars.game.managers.MapManager;
import gg.mooncraft.minecraft.bedwars.game.managers.SetupManager;
import gg.mooncraft.minecraft.bedwars.game.managers.SlimeManager;
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

    private static SlimePlugin slimePlugin;

    private SlimeManager slimeManager;
    private SetupManager setupManager;
    private MapManager mapManager;

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
        if (!loadDependencies()) {
            setEnabled(false);
            return;
        }

        // Load DAOs
        MapDAO.registerDAO(getDatabase());
        UserDAO.registerDAO(getDatabase());

        // Load managers
        this.slimeManager = new SlimeManager();
        this.setupManager = new SetupManager();
        this.mapManager = new MapManager();
        this.gameServerManager = new GameServerManager();

        // Load commands
        Commands.loadAll();
        // Load listeners
        new SetupListeners();

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

    private boolean loadDependencies() {
        if (!Bukkit.getPluginManager().isPluginEnabled("SlimeWorldManager")) {
            getLogger().warning("Dependency not found: SlimeWorldManager");
            return false;
        } else {
            slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        }
        return true;
    }

    /*
    Static Methods
     */
    public static @NotNull BedWarsPlugin getInstance() {
        return BedWarsPlugin.getPlugin(BedWarsPlugin.class);
    }

    public static @NotNull SlimePlugin getSlimePlugin() {
        return slimePlugin;
    }
}
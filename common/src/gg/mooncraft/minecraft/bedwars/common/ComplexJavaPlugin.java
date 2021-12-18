package gg.mooncraft.minecraft.bedwars.common;

import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.command.LiteralCommand;
import me.eduardwayland.mooncraft.waylander.command.wrapper.Brigadier;
import me.eduardwayland.mooncraft.waylander.database.Credentials;
import me.eduardwayland.mooncraft.waylander.database.Database;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gg.mooncraft.minecraft.bedwars.common.messaging.Messenger;
import gg.mooncraft.minecraft.bedwars.common.messaging.RedisMessenger;
import gg.mooncraft.minecraft.bedwars.common.scheduler.BukkitScheduler;
import gg.mooncraft.minecraft.bedwars.common.utilities.BukkitDatabaseUtilities;
import gg.mooncraft.minecraft.bedwars.common.utilities.BukkitRedisUtilities;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;

@Getter
public abstract class ComplexJavaPlugin extends JavaPlugin {

    /*
    Fields
     */
    private @Nullable Brigadier brigadier;

    private @Nullable Database database;
    private @Nullable RedisMessenger messenger;

    private final @NotNull BukkitScheduler scheduler;

    /*
    Constructor
     */
    public ComplexJavaPlugin() {
        super();
        // Load configuration
        saveDefaultConfig();

        // Load WaylanderScheduler
        this.scheduler = new BukkitScheduler(this);
    }

    /*
    Methods
     */

    /**
     * It closes all tasks of any kind, including Database
     */
    protected void shutdown() {
        // Close the database
        if (this.database != null) this.database.shutdown();
        // Close the messenger
        if (this.messenger != null) this.messenger.stop();

        // Close the scheduler
        this.scheduler.shutdownExecutor();
        this.scheduler.shutdownScheduler();
    }

    /**
     * Registers the command into Brigadier instance
     *
     * @param literalCommand the command to register
     */
    public void registerCommand(@NotNull LiteralCommand<?> literalCommand) {
        // Load Brigadier instance if not loaded yet
        if (getBrigadier() == null) {
            try {
                this.brigadier = new Brigadier(this);
            } catch (Exception e) {
                shutdown();
                getLogger().severe("The plugin cannot work properly without Brigadier.");
                return;
            }
        }
        getBrigadier().getBrigadierCommandWrapper().register(literalCommand);
    }

    /**
     * @return a messenger instance if any, else null
     */
    public @Nullable Messenger getMessenger() {
        return messenger;
    }

    /*
    Override Methods
     */
    @Override
    public void onLoad() {
        try {
            ConfigurationSection mysqlSection = getConfig().getConfigurationSection("mysql");
            if (mysqlSection != null) {
                this.database = createDatabase(BukkitDatabaseUtilities.fromBukkitConfig(mysqlSection));
            } else {
                throw new IllegalStateException("The config.yml doesn't contain mysql section.");
            }

            ConfigurationSection redisSection = getConfig().getConfigurationSection("redis");
            if (redisSection != null) {
                ConfigurationSection redisPoolSettingsSection = redisSection.getConfigurationSection("pool-settings");
                if (redisPoolSettingsSection != null) {
                    HostAndPort hostAndPort = BukkitRedisUtilities.parseHostAndPort(redisSection);
                    JedisPoolConfig jedisPoolConfig = BukkitRedisUtilities.parsePoolConfig(redisPoolSettingsSection);
                    String username = redisSection.getString("username");
                    String password = redisSection.getString("password");
                    if (username == null || password == null) {
                        throw new IllegalStateException("The config.yml doesn't contain redis.username and redis.password options.");
                    }
                    this.messenger = createRedisMessenger(jedisPoolConfig, hostAndPort, username, password);
                } else {
                    throw new IllegalStateException("The config.yml doesn't contain redis.pool-settings section.");
                }
            } else {
                throw new IllegalStateException("The config.yml doesn't contain redis section.");
            }
        } catch (Exception e) {
            setEnabled(false);
            getLogger().severe("The plugin cannot be loaded. Connection to the database cannot be created. Error: ");
            e.printStackTrace();
        }
    }


    /*
    Abstract Methods
     */

    /**
     * Forcing an override of onEnable()
     */
    public abstract void onEnable();

    /**
     * Forcing an override of onDisable()
     */
    public abstract void onDisable();

    /**
     * Creates a Database instance
     */
    public abstract @NotNull Database createDatabase(@NotNull Credentials credentials) throws Exception;

    /**
     * Creates a RedisMessenger instance
     */
    public abstract @NotNull RedisMessenger createRedisMessenger(@NotNull JedisPoolConfig jedisPoolConfig, @NotNull HostAndPort hostAndPort, @NotNull String username, @NotNull String password);
}
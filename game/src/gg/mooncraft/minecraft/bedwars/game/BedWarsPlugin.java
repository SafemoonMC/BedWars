package gg.mooncraft.minecraft.bedwars.game;

import lombok.Getter;

import com.grinderwolf.swm.api.SlimePlugin;

import me.eduardwayland.mooncraft.waylander.database.Credentials;
import me.eduardwayland.mooncraft.waylander.database.Database;
import me.eduardwayland.mooncraft.waylander.database.connection.hikari.impl.MariaDBConnectionFactory;
import me.eduardwayland.mooncraft.waylander.database.scheme.db.NormalDatabaseScheme;
import me.eduardwayland.mooncraft.waylander.database.scheme.file.NormalSchemeFile;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.common.ComplexJavaPlugin;
import gg.mooncraft.minecraft.bedwars.common.messaging.RedisChannel;
import gg.mooncraft.minecraft.bedwars.common.messaging.RedisMessenger;
import gg.mooncraft.minecraft.bedwars.common.utilities.IOUtils;
import gg.mooncraft.minecraft.bedwars.data.MapDAO;
import gg.mooncraft.minecraft.bedwars.data.UserDAO;
import gg.mooncraft.minecraft.bedwars.game.handlers.commands.Commands;
import gg.mooncraft.minecraft.bedwars.game.handlers.listeners.GameListeners;
import gg.mooncraft.minecraft.bedwars.game.handlers.listeners.MatchListeners;
import gg.mooncraft.minecraft.bedwars.game.handlers.listeners.MenuListeners;
import gg.mooncraft.minecraft.bedwars.game.handlers.listeners.PlayerListeners;
import gg.mooncraft.minecraft.bedwars.game.handlers.listeners.SetupListeners;
import gg.mooncraft.minecraft.bedwars.game.managers.BoardManager;
import gg.mooncraft.minecraft.bedwars.game.managers.GameRequestManager;
import gg.mooncraft.minecraft.bedwars.game.managers.GameServerManager;
import gg.mooncraft.minecraft.bedwars.game.managers.MapManager;
import gg.mooncraft.minecraft.bedwars.game.managers.MatchManager;
import gg.mooncraft.minecraft.bedwars.game.managers.SetupManager;
import gg.mooncraft.minecraft.bedwars.game.managers.SlimeManager;
import gg.mooncraft.minecraft.bedwars.game.messaging.GameRedisMessenger;
import gg.mooncraft.minecraft.bedwars.game.papi.TeamExtension;
import gg.mooncraft.minecraft.bedwars.game.slime.AsyncSlimeLoader;
import gg.mooncraft.minecraft.bedwars.game.slime.AsyncSlimePlugin;
import gg.mooncraft.minecraft.bedwars.game.utilities.ServerUtilities;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@Getter
public class BedWarsPlugin extends ComplexJavaPlugin {

    /*
    Fields
     */
    private final String serverName = ServerUtilities.getProperty("server-name");

    private static AsyncSlimePlugin asyncSlimePlugin;
    private static AsyncSlimeLoader asyncSlimeLoader;

    private SlimeManager slimeManager;
    private SetupManager setupManager;
    private MatchManager matchManager;
    private BoardManager boardManager;
    private MapManager mapManager;

    private GameServerManager gameServerManager;
    private GameRequestManager gameRequestManager;

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
        this.matchManager = new MatchManager();
        this.boardManager = new BoardManager();
        this.mapManager = new MapManager();
        this.gameServerManager = new GameServerManager();
        this.gameRequestManager = new GameRequestManager();

        // Load commands
        Commands.loadAll();
        // Load listeners
        new SetupListeners();
        new MatchListeners();
        new PlayerListeners();
        new GameListeners();
        new MenuListeners();

        // Show enabling information
        getLogger().info("Running on " + serverName + "...");
        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {
        // Unlock loaded worlds
        if (mapManager != null) {
            getMapManager().getWorldsMap().values().stream().filter(slimeBukkitPair -> slimeBukkitPair.slimeWorld().isLocked()).forEach(slimeBukkitPair -> {
                try {
                    getAsyncSlimeLoader().getSync().unlockWorld(slimeBukkitPair.slimeWorld().getName());
                    getLogger().info("[MapManager] Unlocked " + slimeBukkitPair.slimeWorld().getName());
                } catch (Exception ignored) {
                    getLogger().info("[MapManager] " + slimeBukkitPair.slimeWorld().getName() + " cannot be unlocked.");
                }
            });
        }

        // Shutdown processes
        super.shutdown();
        getLogger().info("Disabled!");
    }

    @Override
    public @NotNull Database createDatabase(@NotNull Credentials credentials) throws Exception {
        // Load input stream
        InputStream inputStream = getResource("bedwars-db.scheme");
        if (inputStream == null) {
            throw new IllegalStateException("bedwars-db.scheme is not inside the jar.");
        }

        // Create temporary file
        File temporaryFile = new File(getDataFolder(), "bedwars-db.scheme");
        if (!temporaryFile.exists() && !temporaryFile.createNewFile()) {
            throw new IllegalStateException("The temporary file bedwars-db.scheme cannot be created.");
        }

        // Load output stream
        FileOutputStream outputStream = new FileOutputStream(temporaryFile);

        // Copy input to output
        IOUtils.copy(inputStream, outputStream);

        // Close streams
        inputStream.close();
        outputStream.close();

        // Parse database scheme and delete temporary file
        NormalDatabaseScheme normalDatabaseScheme = new NormalSchemeFile(temporaryFile).parse();
        if (!temporaryFile.delete()) {
            getLogger().warning("The temporary file bedwars-db.scheme cannot be deleted. You could ignore this warning!");
        }
        return Database.builder()
                .identifier(getName())
                .scheduler(getScheduler())
                .databaseScheme(normalDatabaseScheme)
                .connectionFactory(new MariaDBConnectionFactory(getName(), credentials))
                .build();
    }

    @Override
    public @NotNull RedisMessenger createRedisMessenger(@NotNull JedisPoolConfig jedisPoolConfig, @NotNull HostAndPort hostAndPort, @NotNull String username, @NotNull String password) {
        RedisMessenger redisMessenger = new RedisMessenger(this, new GameRedisMessenger(this));
        redisMessenger.init(RedisChannel.GAME, jedisPoolConfig, hostAndPort, username, password);
        return redisMessenger;
    }

    private boolean loadDependencies() {
        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().warning("Dependency not found: PlaceholderAPI");
            return false;
        } else {
            new TeamExtension().register();
        }
        if (!Bukkit.getPluginManager().isPluginEnabled("SlimeWorldManager")) {
            getLogger().warning("Dependency not found: SlimeWorldManager");
            return false;
        } else {
            SlimePlugin slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
            if (slimePlugin == null) return false;
            asyncSlimePlugin = new AsyncSlimePlugin(slimePlugin);
            asyncSlimeLoader = new AsyncSlimeLoader(slimePlugin.getLoader("mysql"));
        }
        return true;
    }

    /*
    Static Methods
     */
    public static @NotNull BedWarsPlugin getInstance() {
        return BedWarsPlugin.getPlugin(BedWarsPlugin.class);
    }

    public static @NotNull AsyncSlimePlugin getAsyncSlimePlugin() {
        return asyncSlimePlugin;
    }

    public static @NotNull AsyncSlimeLoader getAsyncSlimeLoader() {
        return asyncSlimeLoader;
    }
}
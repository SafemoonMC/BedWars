package gg.mooncraft.minecraft.bedwars.common;

import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.database.Database;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gg.mooncraft.minecraft.bedwars.common.scheduler.BukkitScheduler;

@Getter
public abstract class ComplexJavaPlugin extends JavaPlugin {

    /*
    Fields
     */
    private @Nullable Database database;
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

        // Close the scheduler
        this.scheduler.shutdownExecutor();
        this.scheduler.shutdownScheduler();
    }

    /*
    Override Methods
     */
    @Override
    public void onLoad() {
        try {
            this.database = createDatabase();
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
     * Creates a Database instance;
     */
    public abstract @NotNull Database createDatabase();
}
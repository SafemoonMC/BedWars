package gg.mooncraft.minecraft.bedwars.common.scheduler;

import me.eduardwayland.mooncraft.waylander.scheduler.AsyncScheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public final class BukkitScheduler extends AsyncScheduler {

    /*
    Fields
     */
    private final Executor syncExecutor;

    /*
    Constructor
     */
    public BukkitScheduler(@NotNull JavaPlugin javaPlugin) {
        super(javaPlugin.getName());
        this.syncExecutor = runnable -> Bukkit.getScheduler().runTask(javaPlugin, runnable);
    }

    /*
    Override Methods
     */
    @Override
    public Executor sync() {
        return syncExecutor;
    }
}

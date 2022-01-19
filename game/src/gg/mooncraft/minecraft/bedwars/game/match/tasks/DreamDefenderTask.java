package gg.mooncraft.minecraft.bedwars.game.match.tasks;

import me.eduardwayland.mooncraft.waylander.scheduler.SchedulerTask;

import org.bukkit.Effect;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class DreamDefenderTask implements Runnable {

    /*
    Fields
     */
    private final @NotNull GameMatchPlayer gameMatchPlayer;
    private final @NotNull IronGolem ironGolem;
    private final @NotNull SchedulerTask schedulerTask;
    private final @NotNull AtomicInteger lifetime;

    /*
    Constructor
     */
    public DreamDefenderTask(@NotNull GameMatchPlayer gameMatchPlayer, @NotNull IronGolem ironGolem) {
        this.gameMatchPlayer = gameMatchPlayer;
        this.ironGolem = ironGolem;
        this.schedulerTask = BedWarsPlugin.getInstance().getScheduler().asyncRepeating(this, 50, TimeUnit.MILLISECONDS);
        this.lifetime = new AtomicInteger(20 * 60 * 4);
    }


    /*
    Override Methods
     */
    @Override
    public void run() {
        // If iron golem is already dead, cancel task
        if (this.ironGolem.isDead()) {
            this.schedulerTask.cancel();
            return;
        }

        // If iron golem is still alive, but lifetime is ended, despawn and cancel task
        if (this.lifetime.getAndDecrement() <= 0) {
            BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
                this.ironGolem.getWorld().playEffect(this.ironGolem.getLocation(), Effect.SMOKE, 5);
                this.ironGolem.remove();
            });
            this.schedulerTask.cancel();
            return;
        }

        // If iron golem is still alive, check for new targets
        BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
            this.ironGolem.getWorld().getNearbyEntitiesByType(Player.class, this.ironGolem.getLocation(), 3, 3, 3)
                    .stream()
                    .filter(player -> !this.gameMatchPlayer.getParent().hasPlayer(player.getUniqueId()))
                    .findAny()
                    .ifPresent(this.ironGolem::setTarget);
        });
    }
}
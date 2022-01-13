package gg.mooncraft.minecraft.bedwars.game.match.tasks;

import me.eduardwayland.mooncraft.waylander.scheduler.SchedulerTask;

import org.bukkit.Effect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Snowball;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.utilities.DisplayUtilities;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class BedbugTask implements Runnable {

    /*
    Inner
     */
    public enum State {
        SNOWBALL, SILVERFISH
    }

    /*
    Fields
     */
    private final @NotNull GameMatchPlayer gameMatchPlayer;
    private final @NotNull Snowball snowball;
    private final @NotNull SchedulerTask schedulerTask;
    private @NotNull State state;
    private final @NotNull AtomicInteger lifetime;

    private Silverfish silverfish;

    /*
    Constructor
     */
    public BedbugTask(@NotNull GameMatchPlayer gameMatchPlayer, @NotNull Snowball snowball) {
        this.gameMatchPlayer = gameMatchPlayer;
        this.snowball = snowball;
        this.schedulerTask = BedWarsPlugin.getInstance().getScheduler().asyncRepeating(this, 50, TimeUnit.MILLISECONDS);
        this.state = State.SNOWBALL;
        this.lifetime = new AtomicInteger(20 * 15);
    }


    /*
    Override Methods
     */
    @Override
    public void run() {
        if (!this.snowball.isDead()) return;

        // If the snowball is dead and state is not updated
        // Update state and spawn silverfish
        if (this.state == State.SNOWBALL) {
            this.state = State.SILVERFISH;
            BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
                this.silverfish = (Silverfish) this.snowball.getWorld().spawnEntity(this.snowball.getLocation().add(0, 1, 0), EntityType.SILVERFISH);
                this.silverfish.setCustomName(DisplayUtilities.getColored("&cBedbug"));
                this.silverfish.setCustomNameVisible(true);
                this.silverfish.setMetadata("owner", new FixedMetadataValue(BedWarsPlugin.getInstance(), this.gameMatchPlayer.getUniqueId().toString()));
                this.silverfish.setMetadata("owner-team", new FixedMetadataValue(BedWarsPlugin.getInstance(), this.gameMatchPlayer.getParent().getGameTeam().name()));
                this.silverfish.getWorld().getNearbyEntitiesByType(Player.class, this.silverfish.getLocation(), 3, 3, 3)
                        .stream()
                        .filter(player -> !this.gameMatchPlayer.getParent().hasPlayer(player.getUniqueId()))
                        .findAny()
                        .ifPresent(this.silverfish::setTarget);
            });
            return;
        }

        // If silverfish is already dead, cancel task
        if (this.silverfish.isDead()) {
            this.schedulerTask.cancel();
            return;
        }
        // If silverfish is still alive, but lifetime is ended, despawn and cancel task
        if (this.lifetime.getAndDecrement() <= 0) {
            BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
                this.silverfish.getWorld().playEffect(this.silverfish.getLocation(), Effect.SMOKE, 5);
                this.silverfish.remove();
            });
            this.schedulerTask.cancel();
        }
    }
}
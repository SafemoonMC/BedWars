package gg.mooncraft.minecraft.bedwars.game.match.tasks;

import me.eduardwayland.mooncraft.waylander.scheduler.SchedulerTask;

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

public final class BedbugTask implements Runnable {

    /*
    Fields
     */
    private final @NotNull GameMatchPlayer gameMatchPlayer;
    private final @NotNull Snowball snowball;
    private final @NotNull SchedulerTask schedulerTask;

    /*
    Constructor
     */
    public BedbugTask(@NotNull GameMatchPlayer gameMatchPlayer, @NotNull Snowball snowball) {
        this.gameMatchPlayer = gameMatchPlayer;
        this.snowball = snowball;
        this.schedulerTask = BedWarsPlugin.getInstance().getScheduler().asyncRepeating(this, 50, TimeUnit.MILLISECONDS);
    }


    /*
    Override Methods
     */
    @Override
    public void run() {
        if (!this.snowball.isOnGround() && !this.snowball.isDead()) return;

        BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
            Silverfish silverfish = (Silverfish) this.snowball.getWorld().spawnEntity(this.snowball.getLocation(), EntityType.SILVERFISH);
            silverfish.setCustomName(DisplayUtilities.getColored("&cBedbug"));
            silverfish.setCustomNameVisible(true);
            silverfish.setMetadata("owner", new FixedMetadataValue(BedWarsPlugin.getInstance(), this.gameMatchPlayer.getUniqueId().toString()));
            silverfish.setMetadata("owner-team", new FixedMetadataValue(BedWarsPlugin.getInstance(), this.gameMatchPlayer.getParent().getGameTeam().name()));
            silverfish.getWorld().getNearbyEntitiesByType(Player.class, silverfish.getLocation(), 3, 3, 3)
                    .stream()
                    .filter(player -> !gameMatchPlayer.getParent().hasPlayer(player.getUniqueId()))
                    .findAny()
                    .ifPresent(silverfish::setTarget);
        });

        this.schedulerTask.cancel();
    }
}
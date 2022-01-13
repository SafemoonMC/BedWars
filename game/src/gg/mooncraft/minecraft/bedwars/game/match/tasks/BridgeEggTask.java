package gg.mooncraft.minecraft.bedwars.game.match.tasks;

import me.eduardwayland.mooncraft.waylander.scheduler.SchedulerTask;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Egg;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.utilities.ItemsUtilities;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BridgeEggTask implements Runnable {

    /*
    Constants
     */
    private static final @NotNull BlockFace[] FACES = {BlockFace.SELF, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST};

    /*
    Fields
     */
    private final @NotNull GameMatchPlayer gameMatchPlayer;
    private final @NotNull Egg egg;
    private final @NotNull SchedulerTask schedulerTask;
    private final @NotNull AtomicInteger lifetime;

    /*
    Constructor
     */
    public BridgeEggTask(@NotNull GameMatchPlayer gameMatchPlayer, @NotNull Egg egg) {
        this.gameMatchPlayer = gameMatchPlayer;
        this.egg = egg;
        this.schedulerTask = BedWarsPlugin.getInstance().getScheduler().asyncRepeating(this, 50, TimeUnit.MILLISECONDS);
        this.lifetime = new AtomicInteger(15);
    }


    /*
    Override Methods
     */
    @Override
    public void run() {
        Location location = egg.getLocation().subtract(0, 2, 0);
        List<Block> blockList = Arrays.stream(FACES)
                .map(blockFace -> location.getBlock().getRelative(blockFace))
                .filter(block -> block.getType().isAir())
                .filter(block -> this.gameMatchPlayer.getParent().getParent().getBlocksSystem().canPlace(block.getLocation()))
                .collect(Collectors.toList());
        Bukkit.getScheduler().runTaskLater(BedWarsPlugin.getInstance(), () -> {
            blockList.forEach(block -> block.setType(ItemsUtilities.createWoolitem(this.gameMatchPlayer.getParent().getGameTeam()).getType()));
        }, 3);
        this.gameMatchPlayer.getParent().getParent().getBlocksSystem().placeBlocks(blockList.stream().map(Block::getLocation).collect(Collectors.toList()));

        if (this.egg.isOnGround() || this.egg.isDead() || this.lifetime.getAndDecrement() == 0) {
            BedWarsPlugin.getInstance().getScheduler().executeSync(this.egg::remove);
            this.schedulerTask.cancel();
        }
    }
}
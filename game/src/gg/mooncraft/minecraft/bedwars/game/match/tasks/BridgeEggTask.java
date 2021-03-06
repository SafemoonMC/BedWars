package gg.mooncraft.minecraft.bedwars.game.match.tasks;

import me.eduardwayland.mooncraft.waylander.scheduler.SchedulerTask;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Egg;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.utilities.ItemsUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BridgeEggTask implements Runnable {

    /*
    Constants
     */
    private static final @NotNull BlockFace[] FACES = {BlockFace.SELF, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_EAST};
//    private static final @NotNull BlockFace[] FACES = {BlockFace.SELF, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST};
//    private static final @NotNull BlockFace[] FACES = {BlockFace.SELF, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST};

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
        this.schedulerTask = BedWarsPlugin.getInstance().getScheduler().asyncRepeating(this, 25, TimeUnit.MILLISECONDS);
        this.lifetime = new AtomicInteger(20);
    }

    /*
    Override Methods
     */
    @Override
    public void run() {
        Location location = this.egg.getLocation().clone().subtract(0, 1, 0);

        List<Block> blockList = new ArrayList<>();
        for (int x = (int) location.getX() - 1; x <= (int) location.getX(); x++) {
            for (int z = (int) location.getZ() - 1; z <= (int) location.getZ(); z++) {
                blockList.add(location.getWorld().getBlockAt(x, location.getBlockY(), z));
            }
        }

        Bukkit.getScheduler().runTaskLater(BedWarsPlugin.getInstance(), () -> blockList.forEach(block -> {
            block.setType(ItemsUtilities.createWoolItem(this.gameMatchPlayer.getParent().getGameTeam()).getType());
            if (ThreadLocalRandom.current().nextBoolean()) {
                block.getWorld().playSound(block.getLocation(), Sound.BLOCK_WOOD_PLACE, 1, 1);
                block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
            }
        }), 3);
        this.gameMatchPlayer.getParent().getParent().getBlocksSystem().placeBlocks(blockList.stream().map(Block::getLocation).collect(Collectors.toList()));

        if (this.egg.isDead() || this.lifetime.getAndDecrement() == 0) {
            BedWarsPlugin.getInstance().getScheduler().executeSync(this.egg::remove);
            this.schedulerTask.cancel();
        }
    }
}
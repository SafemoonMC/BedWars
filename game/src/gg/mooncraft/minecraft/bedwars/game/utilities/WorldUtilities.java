package gg.mooncraft.minecraft.bedwars.game.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WorldUtilities {

    public static @NotNull List<Block> getBlocksBetween(@NotNull Location firstLocation, @NotNull Location secondLocation) {
        Location minimumLocation = new Location(firstLocation.getWorld(), Math.min(firstLocation.getX(), secondLocation.getX()), Math.min(firstLocation.getY(), secondLocation.getY()), Math.min(firstLocation.getZ(), secondLocation.getZ()));
        Location maximumLocation = new Location(firstLocation.getWorld(), Math.max(firstLocation.getX(), secondLocation.getX()), Math.max(firstLocation.getY(), secondLocation.getY()), Math.max(firstLocation.getZ(), secondLocation.getZ()));

        List<Block> blockList = new ArrayList<>();
        for (int x = minimumLocation.getBlockX(); x <= maximumLocation.getBlockX(); x++) {
            for (int z = minimumLocation.getBlockZ(); z <= maximumLocation.getBlockZ(); z++) {
                for (int y = minimumLocation.getBlockY(); y <= maximumLocation.getBlockY(); y++) {
                    Block block = firstLocation.getWorld().getBlockAt(x, y, z);
                    blockList.add(block);
                }
            }
        }
        return blockList;
    }

    public static @NotNull List<Block> getBlocksBetween(@NotNull Location center, int range) {
        Location minimumLocation = center.clone().subtract(range, range, range);
        Location maximumLocation = center.clone().add(range, range, range);
        return getBlocksBetween(minimumLocation, maximumLocation);
    }

    public static boolean isSameChunk(@NotNull Chunk firstChunk, @NotNull Chunk secondChunk) {
        return firstChunk.getX() == secondChunk.getX() && firstChunk.getZ() == secondChunk.getZ();
    }

    public static boolean areBlocksInArea(Location center, int x, int y, int z) {
        if (center == null || center.getWorld() == null) return false;

        for (int xx = center.getBlockX() - x; xx <= center.getBlockX() + x; xx++) {
            for (int yy = center.getBlockY(); yy <= center.getBlockY() + y; yy++) {
                for (int zz = center.getBlockZ() - z; zz <= center.getBlockZ() + z; zz++) {
                    Block block = center.getWorld().getBlockAt(xx, yy, zz);
                    if (block.getType() != Material.AIR && !isSameXYZ(center, block.getLocation()))
                        return true;
                }
            }
        }
        return false;
    }

    public static boolean isSameArea(Location location, Location center, int x, int y, int z) {
        return isSameArea(location, center, x, y, z, false);
    }

    public static boolean isSameArea(@NotNull Location location, @NotNull Location center, int x, int y, int z, boolean upDown) {
        Location minimumLocation = center.clone().subtract(x, upDown ? y : 1, z);
        Location maximumLocation = center.clone().add(x, y, z);

        int xx = location.getBlockX();
        int yy = location.getBlockY();
        int zz = location.getBlockZ();

        return xx > minimumLocation.getBlockX() && xx < maximumLocation.getBlockX() && yy > minimumLocation.getBlockY() && yy < maximumLocation.getBlockY() && zz > minimumLocation.getBlockZ() && zz < maximumLocation.getBlockZ();
    }

    public static boolean isSameXYZ(Location firstLocation, Location secondLocation) {
        if (firstLocation == null || secondLocation == null) return false;
        if (firstLocation.getWorld() == null || secondLocation.getWorld() == null) return false;
        if (!firstLocation.getWorld().getName().equals(secondLocation.getWorld().getName()))
            return false;
        return firstLocation.getBlockX() == secondLocation.getBlockX() && firstLocation.getBlockY() == secondLocation.getBlockY() && firstLocation.getBlockZ() == secondLocation.getBlockZ();
    }

    public static boolean isSameXZ(Location firstLocation, Location secondLocation) {
        if (firstLocation == null || secondLocation == null) return false;
        if (firstLocation.getWorld() == null || secondLocation.getWorld() == null) return false;
        if (!firstLocation.getWorld().getName().equals(secondLocation.getWorld().getName()))
            return false;
        return firstLocation.getBlockX() == secondLocation.getBlockX() && firstLocation.getBlockZ() == secondLocation.getBlockZ();
    }

    public static Location getCenterizedLocation(Location location) {
        if (location == null) return null;
        return new Location(location.getWorld(), location.getBlockX() + 0.5, location.getBlockY(), location.getBlockZ() + 0.5, location.getYaw(), location.getPitch());
    }
}
package gg.mooncraft.minecraft.bedwars.game.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WorldUtilities {

    public static Location[] getBedParts(@NotNull Block block) {
        Location[] locations = new Location[2];
        locations[0] = block.getLocation();

        Bed bed = (Bed) block.getBlockData();
        if (bed.getPart() == Bed.Part.HEAD) {
            locations[1] = block.getRelative(bed.getFacing().getOppositeFace()).getLocation();
        } else {
            locations[1] = block.getRelative(bed.getFacing()).getLocation();
        }

        return locations;
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
}
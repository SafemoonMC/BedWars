package gg.mooncraft.minecraft.bedwars.game.slime;

import com.grinderwolf.swm.api.world.SlimeWorld;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public record SlimeBukkitPair(@NotNull SlimeWorld slimeWorld, @NotNull World world,
                              boolean persistent) {

    public @NotNull Location getLocation(double x, double y, double z, float yaw, float pitch) {
        return new Location(world, x, y, z, yaw, pitch);
    }
}
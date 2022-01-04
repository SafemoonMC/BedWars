package gg.mooncraft.minecraft.bedwars.game.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.map.point.AbstractMapPoint;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class PointAdapter {

    public static @NotNull Location adapt(@NotNull GameMatch gameMatch, @NotNull AbstractMapPoint point) {
        return gameMatch.getDimension().getLocation(point.getX(), point.getY(), point.getZ(), point.getYaw(), point.getPitch());
    }
}
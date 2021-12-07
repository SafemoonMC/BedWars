package gg.mooncraft.minecraft.bedwars.data.map.point;

import lombok.Getter;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.map.MapPointsContainer;

@Getter
public final class GameMapPoint extends AbstractMapPoint {

    /*
    Fields
     */
    private final @NotNull PointTypes.MAP type;

    /*
    Constructor
     */
    public GameMapPoint(@NotNull MapPointsContainer mapPointsContainer, int id, @NotNull GameMode gameMode, @NotNull PointTypes.MAP type, double x, double y, double z, float yaw, float pitch) {
        super(mapPointsContainer, id, gameMode, x, y, z, yaw, pitch);
        this.type = type;
    }
}

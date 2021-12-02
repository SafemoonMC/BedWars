package gg.mooncraft.minecraft.bedwars.data.map.point;

import lombok.Getter;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.map.GameMapPointTypes;

@Getter
public final class GameMapPoint extends AbstractMapPoint {

    /*
    Fields
     */
    private final @NotNull GameMapPointTypes.MAP type;

    /*
    Constructor
     */
    public GameMapPoint(@NotNull GameMode gameMode, @NotNull GameMapPointTypes.MAP type, double x, double y, double z, float yaw, float pitch) {
        super(gameMode, x, y, z, yaw, pitch);
        this.type = type;
    }
}

package gg.mooncraft.minecraft.bedwars.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class MapPoint {

    /*
    Fields
     */
    private final GameMode gameMode;
    private final MapPointType type;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
}
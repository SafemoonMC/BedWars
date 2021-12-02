package gg.mooncraft.minecraft.bedwars.data.map.point;

import lombok.AllArgsConstructor;
import lombok.Getter;

import gg.mooncraft.minecraft.bedwars.data.GameMode;

@Getter
@AllArgsConstructor
public class AbstractMapPoint {

    /*
    Fields
     */
    private final GameMode gameMode;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
}
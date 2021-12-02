package gg.mooncraft.minecraft.bedwars.data.map.point;

import lombok.Getter;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.GameTeam;
import gg.mooncraft.minecraft.bedwars.data.map.GameMapPointTypes;

@Getter
public final class TeamMapPoint extends AbstractMapPoint {

    /*
    Fields
     */
    private final @NotNull GameTeam gameTeam;
    private final @NotNull GameMapPointTypes.TEAM type;

    /*
    Constructor
     */
    public TeamMapPoint(@NotNull GameMode gameMode, @NotNull GameTeam gameTeam, @NotNull GameMapPointTypes.TEAM type, double x, double y, double z, float yaw, float pitch) {
        super(gameMode, x, y, z, yaw, pitch);
        this.gameTeam = gameTeam;
        this.type = type;
    }
}

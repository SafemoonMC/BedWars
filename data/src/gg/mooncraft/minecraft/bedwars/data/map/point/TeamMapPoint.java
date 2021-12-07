package gg.mooncraft.minecraft.bedwars.data.map.point;

import lombok.Getter;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.GameTeam;
import gg.mooncraft.minecraft.bedwars.data.map.MapPointsContainer;

@Getter
public final class TeamMapPoint extends AbstractMapPoint {

    /*
    Fields
     */
    private final @NotNull GameTeam gameTeam;
    private final @NotNull PointTypes.TEAM type;

    /*
    Constructor
     */
    public TeamMapPoint(@NotNull MapPointsContainer mapPointsContainer, int id, @NotNull GameMode gameMode, @NotNull GameTeam gameTeam, @NotNull PointTypes.TEAM type, double x, double y, double z, float yaw, float pitch) {
        super(mapPointsContainer, id, gameMode, x, y, z, yaw, pitch);
        this.gameTeam = gameTeam;
        this.type = type;
    }
}

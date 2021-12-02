package gg.mooncraft.minecraft.bedwars.data.map;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.map.point.AbstractMapPoint;
import gg.mooncraft.minecraft.bedwars.data.map.point.GameMapPoint;
import gg.mooncraft.minecraft.bedwars.data.map.point.TeamMapPoint;

import java.util.LinkedList;
import java.util.List;

public final class MapPointsContainer {

    /*
    Fields
     */
    private final @NotNull List<GameMapPoint> mapPointList;
    private final @NotNull List<TeamMapPoint> teamPointList;

    /*
    Constructor
     */
    public MapPointsContainer() {
        this.mapPointList = new LinkedList<>();
        this.teamPointList = new LinkedList<>();
    }

    /*
    Methods
     */
    public void addPoint(@NotNull AbstractMapPoint mapPoint) {
        // TODO implement with database
    }

    public void delPoint(@NotNull AbstractMapPoint mapPoint) {
        // TODO implement with database
    }
}
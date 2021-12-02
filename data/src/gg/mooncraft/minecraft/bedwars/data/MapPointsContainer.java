package gg.mooncraft.minecraft.bedwars.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class MapPointsContainer {

    /*
    Fields
     */
    private final @NotNull List<MapPoint> pointList;
    private final @NotNull Map<GameTeam, List<MapPoint>> teamPointMap;

    /*
    Constructor
     */
    public MapPointsContainer() {
        this.pointList = new LinkedList<>();
        this.teamPointMap = new HashMap<>();
    }

    /*
    Methods
     */
    public void addPoint(@NotNull MapPoint mapPoint) {
        // TODO implement with database
    }

    public void delPoint(@NotNull MapPoint mapPoint) {
        // TODO implement with database
    }

    @UnmodifiableView
    public @NotNull List<MapPoint> getPointList(@NotNull GameMode gameMode, @NotNull MapPointType mapPointType) {
        return this.pointList.stream().filter(mapPoint -> mapPoint.getGameMode() == gameMode && mapPoint.getType() == mapPointType).toList();
    }
}
package gg.mooncraft.minecraft.bedwars.data.map;

import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.database.entities.EntityChild;
import me.eduardwayland.mooncraft.waylander.database.entities.EntityParent;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameMap;
import gg.mooncraft.minecraft.bedwars.data.map.point.AbstractMapPoint;
import gg.mooncraft.minecraft.bedwars.data.map.point.GameMapPoint;
import gg.mooncraft.minecraft.bedwars.data.map.point.TeamMapPoint;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Getter
public final class MapPointsContainer implements EntityParent<MapPointsContainer>, EntityChild<GameMap> {

    /*
    Fields
     */
    private final @NotNull GameMap parent;
    private final @NotNull List<GameMapPoint> mapPointList;
    private final @NotNull List<TeamMapPoint> teamPointList;

    /*
    Constructor
     */
    public MapPointsContainer(@NotNull GameMap parent) {
        this.parent = parent;
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

    /*
    Override Methods
     */
    @Override
    public @NotNull CompletableFuture<MapPointsContainer> withChildren() {
        return CompletableFuture.completedFuture(this);
    }
}
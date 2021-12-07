package gg.mooncraft.minecraft.bedwars.data.map;

import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.database.entities.EntityChild;
import me.eduardwayland.mooncraft.waylander.database.entities.EntityParent;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.MapPointsDAO;
import gg.mooncraft.minecraft.bedwars.data.map.point.AbstractMapPoint;
import gg.mooncraft.minecraft.bedwars.data.map.point.GameMapPoint;
import gg.mooncraft.minecraft.bedwars.data.map.point.TeamMapPoint;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Getter
public final class MapPointsContainer implements EntityParent<MapPointsContainer>, EntityChild<BedWarsMap> {

    /*
    Fields
     */
    private final @NotNull BedWarsMap parent;
    private final @NotNull List<GameMapPoint> mapPointList;
    private final @NotNull List<TeamMapPoint> teamPointList;

    /*
    Constructor
     */
    public MapPointsContainer(@NotNull BedWarsMap parent) {
        this.parent = parent;
        this.mapPointList = new LinkedList<>();
        this.teamPointList = new LinkedList<>();
    }

    /*
    Methods
     */
    public void addPoint(@NotNull AbstractMapPoint mapPoint) {
        if (mapPoint instanceof GameMapPoint gameMapPoint) {
            this.mapPointList.add(gameMapPoint);
            MapPointsDAO.createGamePoint(gameMapPoint);
        } else if (mapPoint instanceof TeamMapPoint teamMapPoint) {
            this.teamPointList.add(teamMapPoint);
            MapPointsDAO.createTeamPoint(teamMapPoint);
        } else {
            throw new IllegalArgumentException("AbstractMapPoint implementation not recognized.");
        }
    }

    public void delPoint(@NotNull AbstractMapPoint mapPoint) {
        if (mapPoint instanceof GameMapPoint gameMapPoint) {
            boolean removed = this.mapPointList.remove(gameMapPoint);
            if (removed) {
                MapPointsDAO.deleteGame(gameMapPoint);
            }
        } else if (mapPoint instanceof TeamMapPoint teamMapPoint) {
            boolean removed = this.teamPointList.remove(teamMapPoint);
            if (removed) {
                MapPointsDAO.deleteTeam(teamMapPoint);
            }
        } else {
            throw new IllegalArgumentException("AbstractMapPoint implementation not recognized.");
        }
    }

    /*
    Override Methods
     */
    @Override
    public @NotNull CompletableFuture<MapPointsContainer> withChildren() {
        CompletableFuture<?> futureGamePoint = MapPointsDAO.readGamePoint(this).thenAccept(this.mapPointList::addAll);
        CompletableFuture<?> futureTeamPoint = MapPointsDAO.readTeamPoint(this).thenAccept(this.teamPointList::addAll);
        return CompletableFuture.allOf(futureGamePoint, futureTeamPoint).thenApply(v -> this);
    }
}
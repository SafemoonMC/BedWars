package gg.mooncraft.minecraft.bedwars.data.map;

import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.database.entities.EntityChild;
import me.eduardwayland.mooncraft.waylander.database.entities.EntityParent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.MapPointsDAO;
import gg.mooncraft.minecraft.bedwars.data.map.point.AbstractMapPoint;
import gg.mooncraft.minecraft.bedwars.data.map.point.GameMapPoint;
import gg.mooncraft.minecraft.bedwars.data.map.point.PointTypes;
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
    private int minimumBlockHeight;
    private int maximumBlockHeight;

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
            MapPointsDAO.createGamePoint(gameMapPoint).thenAccept(this.mapPointList::add);
        } else if (mapPoint instanceof TeamMapPoint teamMapPoint) {
            MapPointsDAO.createTeamPoint(teamMapPoint).thenAccept(this.teamPointList::add);
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

    @UnmodifiableView
    public @NotNull List<GameMapPoint> getGameMapPoint(@NotNull GameMode gameMode, @NotNull PointTypes.MAP map) {
        return this.mapPointList.stream().filter(gameMapPoint -> gameMapPoint.getGameMode() == gameMode && gameMapPoint.getType() == map).toList();
    }

    @UnmodifiableView
    public @NotNull List<TeamMapPoint> getTeamMapPoint(@NotNull GameMode gameMode, @NotNull PointTypes.TEAM map) {
        return this.teamPointList.stream().filter(teamMapPoint -> teamMapPoint.getGameMode() == gameMode && teamMapPoint.getType() == map).toList();
    }

    /*
    Override Methods
     */
    @Override
    public @NotNull CompletableFuture<MapPointsContainer> withChildren() {
        CompletableFuture<?> futureGamePoint = MapPointsDAO.readGamePoint(this).thenAccept(this.mapPointList::addAll);
        CompletableFuture<?> futureTeamPoint = MapPointsDAO.readTeamPoint(this).thenAccept(this.teamPointList::addAll);
        return CompletableFuture.allOf(futureGamePoint, futureTeamPoint).thenApply(v -> {
            this.maximumBlockHeight = (int) (this.mapPointList.stream().filter(gameMapPoint -> gameMapPoint.getType() != PointTypes.MAP.MAP_CENTER && gameMapPoint.getType() != PointTypes.MAP.MAP_SPAWNPOINT).mapToDouble(GameMapPoint::getY).average().orElse(0) + 21);
            this.minimumBlockHeight = (int) (this.teamPointList.stream().mapToDouble(TeamMapPoint::getY).average().orElse(0) - 12);
            return this;
        });
    }
}
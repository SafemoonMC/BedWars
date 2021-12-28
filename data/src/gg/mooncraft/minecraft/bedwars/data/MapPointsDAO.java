package gg.mooncraft.minecraft.bedwars.data;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import me.eduardwayland.mooncraft.waylander.database.Database;
import me.eduardwayland.mooncraft.waylander.database.DatabaseManager;
import me.eduardwayland.mooncraft.waylander.database.queries.Query;
import me.eduardwayland.mooncraft.waylander.database.resultset.ResultSetWrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gg.mooncraft.minecraft.bedwars.data.map.MapPointsContainer;
import gg.mooncraft.minecraft.bedwars.data.map.point.GameMapPoint;
import gg.mooncraft.minecraft.bedwars.data.map.point.PointTypes;
import gg.mooncraft.minecraft.bedwars.data.map.point.TeamMapPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class MapPointsDAO {

    /*
    Constants
     */
    private static final @NotNull String POINTS_GAME_TABLE_NAME = "bw_game_maps_points";
    private static final @NotNull String POINTS_TEAM_TABLE_NAME = "bw_game_maps_points_team";
    /*
    Static Fields
     */
    private static @Nullable DatabaseManager database;

    /*
    Static registration
     */
    static void registerDAO(@Nullable Database database) {
        if (MapPointsDAO.database != null || database == null) return;
        MapPointsDAO.database = database.getDatabaseManager();
        System.out.println("BedWars-Data: MapPointsDAO has been registered.");
    }

    /*
    Static Methods
     */
    public static @NotNull CompletableFuture<GameMapPoint> createGamePoint(@NotNull GameMapPoint gameMapPoint) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("INSERT INTO " + POINTS_GAME_TABLE_NAME + "(identifier, mode, type, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?);")
                .with(gameMapPoint.getParent().getParent().getIdentifier())
                .with(gameMapPoint.getGameMode().name())
                .with(gameMapPoint.getType().name())
                .with(gameMapPoint.getX())
                .with(gameMapPoint.getY())
                .with(gameMapPoint.getZ())
                .with(gameMapPoint.getYaw())
                .with(gameMapPoint.getPitch())
                .build();
        return database.updateQuery(query, id -> new GameMapPoint(gameMapPoint.getParent(), id, gameMapPoint.getGameMode(), gameMapPoint.getType(), gameMapPoint.getX(), gameMapPoint.getY(), gameMapPoint.getZ(), gameMapPoint.getYaw(), gameMapPoint.getPitch()), true);
    }

    public static @NotNull CompletableFuture<TeamMapPoint> createTeamPoint(@NotNull TeamMapPoint teamMapPoint) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("INSERT INTO " + POINTS_TEAM_TABLE_NAME + "(identifier, mode, team, type, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);")
                .with(teamMapPoint.getParent().getParent().getIdentifier())
                .with(teamMapPoint.getGameMode().name())
                .with(teamMapPoint.getGameTeam().name())
                .with(teamMapPoint.getType().name())
                .with(teamMapPoint.getX())
                .with(teamMapPoint.getY())
                .with(teamMapPoint.getZ())
                .with(teamMapPoint.getYaw())
                .with(teamMapPoint.getPitch())
                .build();
        return database.updateQuery(query, id -> new TeamMapPoint(teamMapPoint.getParent(), id, teamMapPoint.getGameMode(), teamMapPoint.getGameTeam(), teamMapPoint.getType(), teamMapPoint.getX(), teamMapPoint.getY(), teamMapPoint.getZ(), teamMapPoint.getYaw(), teamMapPoint.getPitch()), true);
    }

    public static @NotNull CompletableFuture<List<GameMapPoint>> readGamePoint(@NotNull MapPointsContainer mapPointsContainer) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("SELECT * FROM " + POINTS_GAME_TABLE_NAME + " WHERE identifier = ?;")
                .with(mapPointsContainer.getParent().getIdentifier())
                .build();
        return database.executeQuery(query, resultSetIterator -> {
            if (resultSetIterator == null || !resultSetIterator.hasNext()) {
                return new ArrayList<>();
            }
            List<GameMapPoint> list = new ArrayList<>();
            resultSetIterator.forEachRemaining(resultSet -> {
                ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSet);
                int id = resultSetWrapper.get("id", Integer.class);
                GameMode mode = GameMode.valueOf(resultSetWrapper.get("mode", String.class));
                PointTypes.MAP type = PointTypes.MAP.valueOf(resultSetWrapper.get("type", String.class));
                double x = resultSetWrapper.get("x", Double.class);
                double y = resultSetWrapper.get("x", Double.class);
                double z = resultSetWrapper.get("x", Double.class);
                float yaw = resultSetWrapper.get("x", Float.class);
                float pitch = resultSetWrapper.get("x", Float.class);
                list.add(new GameMapPoint(mapPointsContainer, id, mode, type, x, y, z, yaw, pitch));
            });
            return list;
        });
    }


    public static @NotNull CompletableFuture<List<TeamMapPoint>> readTeamPoint(@NotNull MapPointsContainer mapPointsContainer) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("SELECT * FROM " + POINTS_TEAM_TABLE_NAME + " WHERE identifier = ?;")
                .with(mapPointsContainer.getParent().getIdentifier())
                .build();
        return database.executeQuery(query, resultSetIterator -> {
            if (resultSetIterator == null || !resultSetIterator.hasNext()) {
                return new ArrayList<>();
            }
            List<TeamMapPoint> list = new ArrayList<>();
            resultSetIterator.forEachRemaining(resultSet -> {
                ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSetIterator.next());
                int id = resultSetWrapper.get("id", Integer.class);
                GameMode mode = GameMode.valueOf(resultSetWrapper.get("mode", String.class));
                GameTeam team = GameTeam.valueOf(resultSetWrapper.get("team", String.class));
                PointTypes.TEAM type = PointTypes.TEAM.valueOf(resultSetWrapper.get("type", String.class));
                double x = resultSetWrapper.get("x", Double.class);
                double y = resultSetWrapper.get("x", Double.class);
                double z = resultSetWrapper.get("x", Double.class);
                float yaw = resultSetWrapper.get("x", Float.class);
                float pitch = resultSetWrapper.get("x", Float.class);
                list.add(new TeamMapPoint(mapPointsContainer, id, mode, team, type, x, y, z, yaw, pitch));
            });
            return list;
        });
    }

    public static @NotNull CompletableFuture<GameMapPoint> deleteGame(@NotNull GameMapPoint gameMapPoint) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("DELETE FROM " + POINTS_GAME_TABLE_NAME + " WHERE id = ?;")
                .with(gameMapPoint.getId())
                .build();
        return database.updateQuery(query, u -> gameMapPoint);
    }

    public static @NotNull CompletableFuture<TeamMapPoint> deleteTeam(@NotNull TeamMapPoint teamMapPoint) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("DELETE FROM " + POINTS_TEAM_TABLE_NAME + " WHERE id = ?;")
                .with(teamMapPoint.getId())
                .build();
        return database.updateQuery(query, u -> teamMapPoint);
    }
}
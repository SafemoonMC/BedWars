package gg.mooncraft.minecraft.bedwars.data;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import me.eduardwayland.mooncraft.waylander.database.Database;
import me.eduardwayland.mooncraft.waylander.database.DatabaseManager;
import me.eduardwayland.mooncraft.waylander.database.queries.Query;
import me.eduardwayland.mooncraft.waylander.database.resultset.ResultSetWrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.data.map.MapInfo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class MapDAO {

    /*
    Constants
     */
    private static final @NotNull String TABLE_NAME = "bw_game_maps";

    /*
    Static Fields
     */
    private static @Nullable DatabaseManager database;

    /*
    Static registration
     */
    public static void registerDAO(@Nullable Database database) {
        if (MapDAO.database != null || database == null) return;
        MapDAO.database = database.getDatabaseManager();
        System.out.println("BedWars-Data: MapDAO has been registered.");
        // Register necessary daos
        MapModesDAO.registerDAO(database);
        MapPointsDAO.registerDAO(database);
        MapSettingsDAO.registerDAO(database);
    }

    /*
    Static Methods
     */
    public static @NotNull CompletableFuture<BedWarsMap> create(@NotNull BedWarsMap bedWarsMap) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?);")
                .with(bedWarsMap.getIdentifier())
                .with(bedWarsMap.getInformation().getDisplay())
                .with(bedWarsMap.getInformation().getTimestamp())
                .build();
        return database.updateQuery(query, u -> bedWarsMap);
    }

    public static @NotNull CompletableFuture<List<BedWarsMap>> read() {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("SELECT * FROM " + TABLE_NAME + ";")
                .build();
        return database.executeQuery(query, resultSetIterator -> {
            if (resultSetIterator == null || !resultSetIterator.hasNext()) {
                return new ArrayList<>();
            }
            List<BedWarsMap> bedWarsMapList = new ArrayList<>();
            resultSetIterator.forEachRemaining(resultSet -> {
                ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSetIterator.next());
                String identifier = resultSetWrapper.get("identifier", String.class);
                String display = resultSetWrapper.get("display", String.class);
                Timestamp timestamp = resultSetWrapper.get("timestamp", Timestamp.class);

                bedWarsMapList.add(new BedWarsMap(identifier, new MapInfo(display, timestamp)).withChildren().join());
            });
            return bedWarsMapList;
        });
    }

    public static @NotNull CompletableFuture<BedWarsMap> update(@NotNull BedWarsMap bedWarsMap) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("UPDATE " + TABLE_NAME + " SET display = ? WHERE identifier = ?;")
                .with(bedWarsMap.getInformation().getDisplay())
                .with(bedWarsMap.getIdentifier())
                .build();
        return database.updateQuery(query, u -> bedWarsMap);
    }

    public static @NotNull CompletableFuture<BedWarsMap> delete(@NotNull BedWarsMap bedWarsMap) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("DELETE FROM " + TABLE_NAME + " WHERE identifier = ?;")
                .with(bedWarsMap.getIdentifier())
                .build();
        return database.updateQuery(query, u -> bedWarsMap);
    }
}
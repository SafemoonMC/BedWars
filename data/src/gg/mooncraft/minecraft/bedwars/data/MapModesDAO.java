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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class MapModesDAO {

    /*
    Constants
     */
    private static final @NotNull String TABLE_NAME = "bw_game_maps_modes";

    /*
    Static Fields
     */
    private static @Nullable DatabaseManager database;

    /*
    Static registration
     */
    static void registerDAO(@Nullable Database database) {
        if (MapModesDAO.database != null || database == null) return;
        MapModesDAO.database = database.getDatabaseManager();
        System.out.println("BedWars-Data: MapModesDAO has been registered.");
    }

    /*
    Static Methods
     */
    public static @NotNull CompletableFuture<Void> create(@NotNull BedWarsMap bedWarsMap, @NotNull GameMode gameMode) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("INSERT INTO " + TABLE_NAME + "(identifier, mode) VALUES (?, ?);")
                .with(bedWarsMap.getIdentifier())
                .with(gameMode.name())
                .build();
        return database.updateQuery(query);
    }

    public static @NotNull CompletableFuture<List<GameMode>> read(@NotNull BedWarsMap bedWarsMap) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("SELECT * FROM " + TABLE_NAME + " WHERE identifier = ?;")
                .with(bedWarsMap.getIdentifier())
                .build();
        return database.executeQuery(query, resultSetIterator -> {
            if (resultSetIterator == null || !resultSetIterator.hasNext()) {
                return new ArrayList<>();
            }
            List<GameMode> list = new ArrayList<>();
            resultSetIterator.forEachRemaining(resultSet -> {
                ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSetIterator.next());
                GameMode gameMode = GameMode.valueOf(resultSetWrapper.get("mode", String.class));
                list.add(gameMode);
            });
            return list;
        });
    }

    public static @NotNull CompletableFuture<Void> delete(@NotNull BedWarsMap bedWarsMap, @NotNull GameMode gameMode) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("DELETE FROM " + TABLE_NAME + " WHERE identifier = ? AND mode = ?;")
                .with(bedWarsMap.getIdentifier())
                .with(gameMode.name())
                .build();
        return database.updateQuery(query);
    }
}
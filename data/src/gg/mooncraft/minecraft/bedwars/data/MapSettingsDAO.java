package gg.mooncraft.minecraft.bedwars.data;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import me.eduardwayland.mooncraft.waylander.database.Database;
import me.eduardwayland.mooncraft.waylander.database.DatabaseManager;
import me.eduardwayland.mooncraft.waylander.database.queries.Query;
import me.eduardwayland.mooncraft.waylander.database.resultset.ResultSetWrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gg.mooncraft.minecraft.bedwars.data.map.MapSettingsContainer;
import gg.mooncraft.minecraft.bedwars.data.map.setting.MapSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class MapSettingsDAO {

    /*
    Constants
     */
    private static final @NotNull String TABLE_NAME = "bw_game_maps_settings";

    /*
    Static Fields
     */
    private static @Nullable DatabaseManager database;

    /*
    Static registration
     */
    static void registerDAO(@Nullable Database database) {
        if (MapSettingsDAO.database != null || database == null) return;
        MapSettingsDAO.database = database.getDatabaseManager();
        System.out.println("BedWars-Data: MapSettingsDAO has been registered.");
    }

    /*
    Static Methods
     */
    public static @NotNull CompletableFuture<MapSetting> create(@NotNull MapSetting mapSetting) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("INSERT INTO " + TABLE_NAME + " (identifier, mode, path, value) VALUES (?, ?, ?, ?);")
                .with(mapSetting.getParent().getParent().getIdentifier())
                .with(mapSetting.getGameMode().name())
                .with(mapSetting.getPath())
                .with(mapSetting.getValue())
                .build();
        return database.updateQuery(query, u -> mapSetting);
    }

    public static @NotNull CompletableFuture<List<MapSetting>> read(@NotNull MapSettingsContainer mapSettingsContainer) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("SELECT * FROM " + TABLE_NAME + " WHERE identifier = ?;")
                .with(mapSettingsContainer.getParent().getIdentifier())
                .build();
        return database.executeQuery(query, resultSetIterator -> {
            if (resultSetIterator == null || !resultSetIterator.hasNext()) {
                return new ArrayList<>();
            }
            List<MapSetting> list = new ArrayList<>();
            resultSetIterator.forEachRemaining(resultSet -> {
                ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSet);
                GameMode gameMode = GameMode.valueOf(resultSetWrapper.get("mode", String.class));
                String path = resultSetWrapper.get("path", String.class);
                String value = resultSetWrapper.get("value", String.class);
                list.add(new MapSetting(mapSettingsContainer, gameMode, path, value));
            });
            return list;
        });
    }

    public static @NotNull CompletableFuture<MapSetting> update(@NotNull MapSetting mapSetting) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("UPDATE " + TABLE_NAME + " SET value = ? WHERE identifier = ? AND mode = ? AND path = ?;")
                .with(mapSetting.getValue())
                .with(mapSetting.getParent().getParent().getIdentifier())
                .with(mapSetting.getGameMode().name())
                .with(mapSetting.getPath())
                .build();
        return database.updateQuery(query, u -> mapSetting);
    }

    public static @NotNull CompletableFuture<Void> delete(@NotNull MapSetting mapSetting) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("DELETE FROM " + TABLE_NAME + " WHERE identifier = ? AND mode = ? AND path = ?;")
                .with(mapSetting.getParent().getParent().getIdentifier())
                .with(mapSetting.getGameMode().name())
                .with(mapSetting.getPath())
                .build();
        return database.updateQuery(query);
    }

}
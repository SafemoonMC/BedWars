package gg.mooncraft.minecraft.bedwars.data;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import me.eduardwayland.mooncraft.waylander.database.Database;
import me.eduardwayland.mooncraft.waylander.database.DatabaseManager;
import me.eduardwayland.mooncraft.waylander.database.queries.Query;
import me.eduardwayland.mooncraft.waylander.database.resultset.ResultSetWrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gg.mooncraft.minecraft.bedwars.data.user.UserStatisticContainer;
import gg.mooncraft.minecraft.bedwars.data.user.stats.GameStatistic;
import gg.mooncraft.minecraft.bedwars.data.user.stats.OverallStatistic;
import gg.mooncraft.minecraft.bedwars.data.user.stats.StatisticTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class UserStatisticDAO {

    /*
    Constants
     */
    private static final @NotNull String GAME_TABLE_NAME = "bw_users_stats_games";
    private static final @NotNull String OVERALL_TABLE_NAME = "bw_users_stats_overall";

    /*
    Static Fields
     */
    private static @Nullable DatabaseManager database;

    /*
    Static registration
     */
    static void registerDAO(@Nullable Database database) {
        if (UserStatisticDAO.database != null || database == null) return;
        UserStatisticDAO.database = database.getDatabaseManager();
        System.out.println("BedWars-Data: UserStatisticDAO has been registered.");
    }

    /*
    Static Methods
     */
    public static @NotNull CompletableFuture<GameStatistic> create(@NotNull GameStatistic gameStatistic) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("INSERT INTO " + GAME_TABLE_NAME + " VALUES (?, ?, ?, ?);")
                .with(gameStatistic.getParent().getParent().getUniqueId().toString())
                .with(gameStatistic.getGameMode().name())
                .with(gameStatistic.getType().name())
                .with(gameStatistic.getAmount().get())
                .build();
        return database.updateQuery(query, u -> gameStatistic);
    }

    public static @NotNull CompletableFuture<OverallStatistic> create(@NotNull OverallStatistic overallStatistic) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("INSERT INTO " + OVERALL_TABLE_NAME + " VALUES (?, ?, ?);")
                .with(overallStatistic.getParent().getParent().getUniqueId().toString())
                .with(overallStatistic.getType().name())
                .with(overallStatistic.getAmount().get())
                .build();
        return database.updateQuery(query, u -> overallStatistic);
    }

    public static @NotNull CompletableFuture<List<GameStatistic>> readGame(@NotNull UserStatisticContainer userStatisticContainer) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("SELECT * FROM " + GAME_TABLE_NAME + " WHERE unique_id = ?;")
                .with(userStatisticContainer.getParent().getUniqueId().toString())
                .build();
        return database.executeQuery(query, resultSetIterator -> {
            if (resultSetIterator == null || !resultSetIterator.hasNext()) {
                return new ArrayList<>();
            }
            List<GameStatistic> list = new ArrayList<>();
            resultSetIterator.forEachRemaining(resultSet -> {
                ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSet);
                GameMode mode = GameMode.valueOf(resultSetWrapper.get("mode", String.class));
                StatisticTypes.GAME type = StatisticTypes.GAME.valueOf(resultSetWrapper.get("name", String.class));
                AtomicInteger amount = new AtomicInteger(resultSetWrapper.get("amount", Integer.class));
                list.add(new GameStatistic(userStatisticContainer, mode, type, amount));
            });
            return list;
        });
    }

    public static @NotNull CompletableFuture<List<OverallStatistic>> readOverall(@NotNull UserStatisticContainer userStatisticContainer) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("SELECT * FROM " + OVERALL_TABLE_NAME + " WHERE unique_id = ?;")
                .with(userStatisticContainer.getParent().getUniqueId().toString())
                .build();
        return database.executeQuery(query, resultSetIterator -> {
            if (resultSetIterator == null || !resultSetIterator.hasNext()) {
                return new ArrayList<>();
            }
            List<OverallStatistic> list = new ArrayList<>();
            resultSetIterator.forEachRemaining(resultSet -> {
                ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSet);
                StatisticTypes.OVERALL type = StatisticTypes.OVERALL.valueOf(resultSetWrapper.get("name", String.class));
                AtomicInteger amount = new AtomicInteger(resultSetWrapper.get("amount", Integer.class));
                list.add(new OverallStatistic(userStatisticContainer, type, amount));
            });
            return list;
        });
    }

    public static @NotNull CompletableFuture<GameStatistic> update(@NotNull GameStatistic gameStatistic) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("UPDATE " + GAME_TABLE_NAME + " SET amount = ? WHERE unique_id = ? AND mode = ? AND name = ?;")
                .with(gameStatistic.getAmount().get())
                .with(gameStatistic.getParent().getParent().getUniqueId().toString())
                .with(gameStatistic.getGameMode().name())
                .with(gameStatistic.getType().name())
                .build();
        return database.updateQuery(query, u -> gameStatistic);
    }

    public static @NotNull CompletableFuture<OverallStatistic> update(@NotNull OverallStatistic overallStatistic) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("UPDATE " + OVERALL_TABLE_NAME + " SET amount = ? WHERE unique_id = ? AND name = ?;")
                .with(overallStatistic.getAmount().get())
                .with(overallStatistic.getParent().getParent().getUniqueId().toString())
                .with(overallStatistic.getType().name())
                .build();
        return database.updateQuery(query, u -> overallStatistic);
    }
}
package gg.mooncraft.minecraft.bedwars.data;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import me.eduardwayland.mooncraft.waylander.database.Database;
import me.eduardwayland.mooncraft.waylander.database.DatabaseManager;
import me.eduardwayland.mooncraft.waylander.database.queries.Query;
import me.eduardwayland.mooncraft.waylander.database.resultset.ResultSetWrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gg.mooncraft.minecraft.bedwars.data.user.BedWarsUser;

import java.math.BigInteger;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class UserDAO {

    /*
    Constants
     */
    private static final @NotNull String TABLE_NAME = "bw_users";

    /*
    Static Fields
     */
    private static @Nullable DatabaseManager database;

    /*
    Static registration
     */
    public static void registerDAO(@Nullable Database database) {
        if (UserDAO.database != null || database == null) return;
        UserDAO.database = database.getDatabaseManager();
        System.out.println("BedWars-Data: UserDAO has been registered.");
        // Register necessary daos
        UserStatisticDAO.registerDAO(database);
    }

    /*
    Static Methods
     */
    public static @NotNull CompletableFuture<BedWarsUser> create(@NotNull BedWarsUser bedWarsUser) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?);")
                .with(bedWarsUser.getUniqueId().toString())
                .with(bedWarsUser.getCoins())
                .with(bedWarsUser.getLevel())
                .with(bedWarsUser.getExperience())
                .build();
        return database.updateQuery(query, u -> bedWarsUser);
    }

    public static @NotNull CompletableFuture<BedWarsUser> read(@NotNull UUID uniqueId) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("SELECT * FROM " + TABLE_NAME + " WHERE unique_id = ?;")
                .with(uniqueId.toString())
                .build();
        return database.executeQuery(query, resultSetIterator -> {
            if (resultSetIterator == null || !resultSetIterator.hasNext()) {
                return create(new BedWarsUser(uniqueId)).join();
            }
            ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSetIterator.next());
            BigInteger coins = resultSetWrapper.get("coins", BigInteger.class);
            BigInteger level = resultSetWrapper.get("level", BigInteger.class);
            BigInteger experience = resultSetWrapper.get("experience", BigInteger.class);
            return new BedWarsUser(uniqueId, coins, level, experience).withChildren().join();
        });
    }

    public static @NotNull CompletableFuture<BedWarsUser> update(@NotNull BedWarsUser bedWarsUser) {
        Objects.requireNonNull(database, "The DAO hasn't been registered yet.");
        Query query = Query.single("UPDATE " + TABLE_NAME + " SET coins = ?, level = ?, experience = ? WHERE unique_id = ?;")
                .with(bedWarsUser.getCoins())
                .with(bedWarsUser.getLevel())
                .with(bedWarsUser.getExperience())
                .with(bedWarsUser.getUniqueId().toString())
                .build();
        return database.updateQuery(query, u -> bedWarsUser);
    }

}
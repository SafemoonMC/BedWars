package gg.mooncraft.minecraft.bedwars.data;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import me.eduardwayland.mooncraft.waylander.database.Database;
import me.eduardwayland.mooncraft.waylander.database.DatabaseManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gg.mooncraft.minecraft.bedwars.data.user.BedWarsUser;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class UserDAO {

    /*
    Static Fields
     */
    private static @Nullable DatabaseManager database;

    /*
    Static registration
     */
    public static void registerDAO(@NotNull Database database) {
        if (UserDAO.database != null) return;
        UserDAO.database = database.getDatabaseManager();
        System.out.println("BedWars-Data: UserDAO has been registered.");
    }

    /*
    Static Methods
     */
    public static @NotNull CompletableFuture<BedWarsUser> load(@NotNull UUID uniqueId) {

        return CompletableFuture.completedFuture(null);
    }
}
package gg.mooncraft.minecraft.bedwars.data;

import me.eduardwayland.mooncraft.waylander.database.entities.EntityParent;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.map.MapPointsContainer;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;

public final class GameMap implements EntityParent<GameMap> {

    /*
    Fields
     */
    private final @NotNull String identifier;
    private final @NotNull MapInfo information;
    private final @NotNull EnumSet<GameMode> gameModeSet;
    private @NotNull MapSettingsContainer settingsContainer;
    private @NotNull MapPointsContainer pointsContainer;

    /*
    Constructor
     */
    public GameMap(@NotNull String identifier) {
        this.identifier = identifier;
        this.information = new MapInfo(identifier.toUpperCase(), Timestamp.from(Instant.now()));
        this.gameModeSet = EnumSet.noneOf(GameMode.class);
        this.settingsContainer = new MapSettingsContainer(this);
        this.pointsContainer = new MapPointsContainer(this);
    }

    /*
    Override Methods
     */
    @Override
    public @NotNull CompletableFuture<GameMap> withChildren() {
        CompletableFuture<?> futureSettings = settingsContainer.withChildren().thenAccept(mapSettingsContainer -> this.settingsContainer = mapSettingsContainer);
        CompletableFuture<?> futurePoints = pointsContainer.withChildren().thenAccept(mapPointsContainer -> this.pointsContainer = mapPointsContainer);

        return CompletableFuture.allOf(futureSettings, futurePoints).thenApply(v -> this);
    }
}
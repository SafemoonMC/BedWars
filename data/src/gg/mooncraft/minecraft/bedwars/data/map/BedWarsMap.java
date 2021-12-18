package gg.mooncraft.minecraft.bedwars.data.map;

import lombok.Getter;

import com.google.common.collect.ImmutableSet;

import me.eduardwayland.mooncraft.waylander.database.entities.EntityParent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.MapModesDAO;

import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Getter
public final class BedWarsMap implements EntityParent<BedWarsMap> {

    /*
    Fields
     */
    private final @NotNull String identifier;
    private final @NotNull MapInfo information;
    private final @NotNull EnumSet<GameMode> gameModeSet;
    private @NotNull MapPointsContainer pointsContainer;
    private @NotNull MapSettingsContainer settingsContainer;

    /*
    Constructor
     */
    public BedWarsMap(@NotNull String identifier, @NotNull MapInfo mapInfo) {
        this.identifier = identifier;
        this.information = mapInfo;
        this.gameModeSet = EnumSet.noneOf(GameMode.class);
        this.settingsContainer = new MapSettingsContainer(this);
        this.pointsContainer = new MapPointsContainer(this);
    }

    /*
    Methods
     */
    public void addGameMode(@NotNull GameMode gameMode) {
        if (this.gameModeSet.contains(gameMode)) return;
        this.gameModeSet.add(gameMode);
        MapModesDAO.create(this, gameMode);
    }

    public void delGameMode(@NotNull GameMode gameMode) {
        if (!this.gameModeSet.contains(gameMode)) return;
        this.gameModeSet.remove(gameMode);
        MapModesDAO.delete(this, gameMode);
    }

    @UnmodifiableView
    public @NotNull Set<GameMode> getGameModeSet() {
        return ImmutableSet.copyOf(this.gameModeSet);
    }

    /*
    Override Methods
     */
    @Override
    public @NotNull CompletableFuture<BedWarsMap> withChildren() {
        CompletableFuture<?> futureModes = MapModesDAO.read(this).thenAccept(this.gameModeSet::addAll);
        CompletableFuture<?> futureSettings = settingsContainer.withChildren().thenAccept(mapSettingsContainer -> this.settingsContainer = mapSettingsContainer);
        CompletableFuture<?> futurePoints = pointsContainer.withChildren().thenAccept(mapPointsContainer -> this.pointsContainer = mapPointsContainer);

        return CompletableFuture.allOf(futureModes, futureSettings, futurePoints).thenApply(v -> this);
    }
}
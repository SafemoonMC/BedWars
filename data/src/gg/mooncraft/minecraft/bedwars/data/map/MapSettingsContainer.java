package gg.mooncraft.minecraft.bedwars.data.map;

import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.database.entities.EntityChild;
import me.eduardwayland.mooncraft.waylander.database.entities.EntityParent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.MapSettingsDAO;
import gg.mooncraft.minecraft.bedwars.data.map.setting.MapSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Getter
public final class MapSettingsContainer implements EntityParent<MapSettingsContainer>, EntityChild<BedWarsMap> {

    /*
    Fields
     */
    private final @NotNull BedWarsMap parent;
    private final @NotNull List<MapSetting> settingList;

    /*
    Constructor
     */
    public MapSettingsContainer(@NotNull BedWarsMap parent) {
        this.parent = parent;
        this.settingList = new ArrayList<>();
    }

    /*
    Methods
     */
    public void set(@NotNull GameMode gameMode, @NotNull String path, @Nullable String value) {
        getSetting(gameMode, path).ifPresentOrElse(streamMapSetting -> {
            this.settingList.remove(streamMapSetting);
            if (value != null) {
                MapSetting mapSetting = new MapSetting(this, gameMode, path, value);
                this.settingList.add(mapSetting);
                MapSettingsDAO.update(mapSetting);
            } else {
                MapSettingsDAO.delete(streamMapSetting);
            }
        }, () -> {
            if (value == null) return;
            MapSetting mapSetting = new MapSetting(this, gameMode, path, value);
            this.settingList.add(mapSetting);
            MapSettingsDAO.create(mapSetting);
        });
    }

    public void del(@NotNull MapSetting mapSetting) {
        boolean removed = this.settingList.remove(mapSetting);
        if (!removed) return;
        MapSettingsDAO.delete(mapSetting);
    }

    public @NotNull Optional<MapSetting> getSetting(@NotNull GameMode gameMode, @NotNull String path) {
        return this.settingList.stream().filter(streamMapSetting -> streamMapSetting.getGameMode() == gameMode && streamMapSetting.getPath().equals(path)).findFirst();
    }

    @UnmodifiableView
    public @NotNull List<MapSetting> getSettingList(@NotNull GameMode gameMode) {
        return this.settingList.stream().filter(mapSetting -> mapSetting.getGameMode() == gameMode).toList();
    }

    /*
    Override Methods
     */
    @Override
    public @NotNull CompletableFuture<MapSettingsContainer> withChildren() {
        return MapSettingsDAO.read(this).thenApply(list -> {
            this.settingList.addAll(list);
            return this;
        });
    }
}
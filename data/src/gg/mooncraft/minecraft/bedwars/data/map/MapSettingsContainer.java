package gg.mooncraft.minecraft.bedwars.data.map;

import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.database.entities.EntityChild;
import me.eduardwayland.mooncraft.waylander.database.entities.EntityParent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.map.setting.MapSetting;

import java.util.ArrayList;
import java.util.List;
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
    public void set(@NotNull MapSetting mapSetting) {
        // TODO implement with database
    }

    public void del(@NotNull MapSetting mapSetting) {
        // TODO implement with database
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
        return CompletableFuture.completedFuture(this);
    }
}
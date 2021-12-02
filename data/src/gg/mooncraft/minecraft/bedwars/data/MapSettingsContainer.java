package gg.mooncraft.minecraft.bedwars.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.List;

public final class MapSettingsContainer {

    /*
    Fields
     */
    private final @NotNull List<MapSetting> settingList;

    /*
    Constructor
     */
    public MapSettingsContainer() {
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
}
package gg.mooncraft.minecraft.bedwars.data.map.setting;

import lombok.AllArgsConstructor;
import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.database.entities.EntityChild;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.map.MapSettingsContainer;

@Getter
@AllArgsConstructor
public final class MapSetting implements EntityChild<MapSettingsContainer> {

    /*
    Fields
     */
    private final @NotNull MapSettingsContainer parent;
    private final @NotNull GameMode gameMode;
    private final @NotNull String path;
    private final @NotNull String value;
}
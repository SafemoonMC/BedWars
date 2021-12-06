package gg.mooncraft.minecraft.bedwars.data.map.setting;

import lombok.AllArgsConstructor;
import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.database.entities.EntityChild;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.map.MapSettingsContainer;

import java.util.Objects;

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

    /*
    Override Methods
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapSetting that = (MapSetting) o;
        return getGameMode() == that.getGameMode() && getPath().equals(that.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGameMode(), getPath());
    }
}
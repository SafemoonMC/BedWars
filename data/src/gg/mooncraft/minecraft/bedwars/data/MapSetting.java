package gg.mooncraft.minecraft.bedwars.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public final class MapSetting {

    /*
    Fields
     */
    private final @NotNull GameMode gameMode;
    private final @NotNull String path;
    private final @NotNull Object value;
}
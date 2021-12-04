package gg.mooncraft.minecraft.bedwars.data.map;

import lombok.AllArgsConstructor;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

@AllArgsConstructor
public final class MapInfo {

    private final @NotNull String display;
    private final @NotNull Timestamp timestamp;
}

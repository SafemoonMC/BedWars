package gg.mooncraft.minecraft.bedwars.data;

import lombok.AllArgsConstructor;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

@AllArgsConstructor
public final class MapInfo {

    private final @NotNull String display;
    private final @NotNull String description;
    private final @NotNull Timestamp timestamp;
}

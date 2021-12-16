package gg.mooncraft.minecraft.bedwars.data.map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public final class MapInfo {

    @Setter
    private @NotNull String display;
    private final @NotNull Timestamp timestamp;
}

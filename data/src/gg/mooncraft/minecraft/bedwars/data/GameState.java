package gg.mooncraft.minecraft.bedwars.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public enum GameState {

    LOADING("Loading..."),
    WAITING("Waiting for players..."),
    PLAYING("Playing..."),
    ENDING("Ending..."),
    UNLOADING("Unloading...");

    /*
    Fields
     */
    private final @NotNull String display;
}
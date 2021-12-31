package gg.mooncraft.minecraft.bedwars.game.match;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public enum GameEvent {

    DIAMOND("Diamond", 3),
    EMERALD("Emerald", 3),
    BED_DESTRUCTION("Bed Destruction", 1),
    SUDDEN_DEATH("Sudden Death", 1),
    TIE("Tie", 1);

    /*
    Fields
     */
    private final @NotNull String display;
    private final int minutes;

}
package gg.mooncraft.minecraft.bedwars.game.match;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamStatus {
    ALIVE("&a✓"), NOT_ALIVE("&c✘");

    /*
    Fields
     */
    private final String symbol;
}
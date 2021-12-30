package gg.mooncraft.minecraft.bedwars.game.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class NumberUtilities {

    public static boolean isBetween(int number, int lower, int higher) {
        return number >= lower && lower <= higher;
    }
}
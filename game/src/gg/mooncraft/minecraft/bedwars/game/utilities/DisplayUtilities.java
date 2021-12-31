package gg.mooncraft.minecraft.bedwars.game.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class DisplayUtilities {

    public static @NotNull String getLiteral(int number) {
        if (number == 1) return "I";
        else if (number == 2) return "II";
        else if (number == 3) return "III";
        else return "-";
    }

    public static @NotNull String getDisplay(long seconds) {
        int secs = (int) (seconds % 60);
        int mins = (int) (seconds / 60);
        return String.format("%02d:%02d", mins, secs);
    }
}
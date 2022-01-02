package gg.mooncraft.minecraft.bedwars.game.match.options;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameMode;

import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class MatchOptions {

    /*
    Inner
     */
    @Getter
    public static class SOLO implements MatchOption {
        private final @NotNull OptionEntry<TimeUnit, Integer> generatorDiamondStartDelay = new OptionEntry<>(TimeUnit.SECONDS, 0);
        private final @NotNull OptionEntry<TimeUnit, Integer> generatorEmeraldStartDelay = new OptionEntry<>(TimeUnit.SECONDS, 10);
        private final int[] generatorDiamondDropRates = {30, 23, 12};
        private final int[] generatorEmeraldDropRates = {60, 46, 24};
    }

    @Getter
    public static class DUOS implements MatchOption {
        private final @NotNull OptionEntry<TimeUnit, Integer> generatorDiamondStartDelay = new OptionEntry<>(TimeUnit.SECONDS, 0);
        private final @NotNull OptionEntry<TimeUnit, Integer> generatorEmeraldStartDelay = new OptionEntry<>(TimeUnit.SECONDS, 10);
        private final int[] generatorDiamondDropRates = {30, 23, 12};
        private final int[] generatorEmeraldDropRates = {60, 46, 24};
    }

    @Getter
    public static class TRIOS implements MatchOption {
        private final @NotNull OptionEntry<TimeUnit, Integer> generatorDiamondStartDelay = new OptionEntry<>(TimeUnit.SECONDS, 0);
        private final @NotNull OptionEntry<TimeUnit, Integer> generatorEmeraldStartDelay = new OptionEntry<>(TimeUnit.SECONDS, 10);
        private final int[] generatorDiamondDropRates = {30, 23, 12};
        private final int[] generatorEmeraldDropRates = {60, 46, 24};
    }

    @Getter
    public static class QUADS implements MatchOption {
        public final @NotNull OptionEntry<TimeUnit, Integer> generatorDiamondStartDelay = new OptionEntry<>(TimeUnit.SECONDS, 0);
        public final @NotNull OptionEntry<TimeUnit, Integer> generatorEmeraldStartDelay = new OptionEntry<>(TimeUnit.SECONDS, 10);
        public final int[] generatorDiamondDropRates = {30, 23, 12};
        public final int[] generatorEmeraldDropRates = {65, 55, 27};
    }

    /*
    Constants
     */
    private static final @NotNull MatchOption SOLO = new SOLO();
    private static final @NotNull MatchOption DUOS = new DUOS();
    private static final @NotNull MatchOption TRIOS = new TRIOS();
    private static final @NotNull MatchOption QUADS = new QUADS();

    /*
    Static Methods
     */
    public static @NotNull MatchOption getMatchOption(@NotNull GameMode gameMode) {
        if (gameMode == GameMode.SOLO) {
            return SOLO;
        } else if (gameMode == GameMode.DUOS) {
            return DUOS;
        } else if (gameMode == GameMode.TRIOS) {
            return TRIOS;
        } else {
            return QUADS;
        }
    }
}
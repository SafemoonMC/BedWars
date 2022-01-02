package gg.mooncraft.minecraft.bedwars.game.match.options;

import java.util.concurrent.TimeUnit;

public interface MatchOption {

    OptionEntry<TimeUnit, Integer> getGeneratorDiamondStartDelay();

    OptionEntry<TimeUnit, Integer> getGeneratorEmeraldStartDelay();

    int[] getGeneratorDiamondDropRates();

    int[] getGeneratorEmeraldDropRates();
}
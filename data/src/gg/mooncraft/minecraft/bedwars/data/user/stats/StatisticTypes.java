package gg.mooncraft.minecraft.bedwars.data.user.stats;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class StatisticTypes {

    public enum GAME {

        WINS,
        LOSSES,
        NORMAL_KILLS,
        NORMAL_DEATHS,
        FINAL_KILLS,
        FINAL_DEATHS,
        BEDS_BROKEN
    }

    public enum OVERALL {

        IRON,
        GOLD,
        DIAMOND,
        EMERALD
    }
}
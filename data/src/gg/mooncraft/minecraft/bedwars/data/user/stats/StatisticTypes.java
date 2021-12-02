package gg.mooncraft.minecraft.bedwars.data.user.stats;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class StatisticTypes {

    public enum GAME {

        NORMAL_KILL,
        NORMAL_DEATH,
        FINAL_KILL,
        FINAL_DEATH,
        BEDS_BROKEN
    }

    public enum OVERALL {

        IRON,
        GOLD,
        DIAMOND,
        EMERALD
    }
}
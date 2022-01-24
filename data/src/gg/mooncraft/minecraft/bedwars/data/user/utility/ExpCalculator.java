package gg.mooncraft.minecraft.bedwars.data.user.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.Prestige;
import gg.mooncraft.minecraft.bedwars.data.Prestiges;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class ExpCalculator {

    /*
    Constants
     */
    private static final int EASY_LEVELS = 4;
    private static final long EASY_LEVELS_XP = 7000;
    private static final long XP_PER_PRESTIGE = 96 * 5000 * EASY_LEVELS_XP;
    private static final int LEVELS_PER_PRESTIGE = 100;

    /*
    Static Methods
     */
    public static @NotNull Prestige getPrestigeForLevel(int level) {
        int prestige = (int) ((double) level / LEVELS_PER_PRESTIGE);
        return Prestiges.getPrestige(prestige);
    }

    public static @NotNull Prestige getPrestigeForExperience(long experience) {
        return getPrestigeForLevel(getLevelForExperience(experience));
    }

    public static int getLevelForExperience(long experience) {
        int prestige = (int) ((double) experience / XP_PER_PRESTIGE);
        int level = prestige * LEVELS_PER_PRESTIGE;
        long experienceWithoutPrestiges = experience - (prestige * XP_PER_PRESTIGE);
        for (int i = 1; i <= EASY_LEVELS; i++) {
            long experienceForEasyLevel = getExperienceForLevel(i);
            if (experienceWithoutPrestiges < experienceForEasyLevel) break;
            level++;
            experienceWithoutPrestiges -= experienceForEasyLevel;
        }
        level += experienceWithoutPrestiges / 5000;
        return level;
    }

    public static long getExperienceForLevelPlain(int level) {
        int prestige = (int) ((double) level / LEVELS_PER_PRESTIGE);
        long experience = prestige * XP_PER_PRESTIGE;

        int levelRespectingPrestige = getLevelRespectingPrestige(level);

        if (levelRespectingPrestige > EASY_LEVELS) {
            experience += 5000L * (levelRespectingPrestige - EASY_LEVELS);
        }
        for (int i = 1; i <= EASY_LEVELS; i++) {
            if (i > levelRespectingPrestige) break;
            long experienceForEasyLevel = getExperienceForLevel(i);
            experience += experienceForEasyLevel;
        }

        return experience;
    }

    public static long getExperienceForLevel(int level) {
        if (level == 0) return 0;
        int respectedLevel = getLevelRespectingPrestige(level);
        switch (respectedLevel) {
            case 1 -> {
                return 500;
            }
            case 2 -> {
                return 1000;
            }
            case 3 -> {
                return 2000;
            }
            case 4 -> {
                return 3500;
            }
            default -> {
                return 5000;
            }
        }
    }

    public static int getLevelRespectingPrestige(int level) {
        if (level > Prestiges.getPrestigesCount() * LEVELS_PER_PRESTIGE) {
            return level - Prestiges.getPrestigesCount() * LEVELS_PER_PRESTIGE;
        } else {
            return level % LEVELS_PER_PRESTIGE;
        }
    }
}
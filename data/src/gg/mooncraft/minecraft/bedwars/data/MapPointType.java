package gg.mooncraft.minecraft.bedwars.data;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MapPointType {

    MAP_CENTER(1, 1),
    MAP_GENERATOR_DIAMOND(1, -1),
    MAP_GENERATOR_EMERALD(1, -1),
    TEAM_SPAWNPOINT(1, 4),
    TEAM_BED(1, 1),
    TEAM_SHOP(1, 1),
    TEAM_SHOP_UPGRADES(1, 1),
    TEAM_GENERATOR(1, 1);

    /*
    Fields
     */
    private final int minimum;
    private final int maximum;

    /*
    Methods
     */
    public boolean hasMaximum() {
        return maximum != -1;
    }
}
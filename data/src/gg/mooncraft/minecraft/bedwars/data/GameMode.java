package gg.mooncraft.minecraft.bedwars.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameMode {

    SOLO(8, 1),
    DUOS(8, 2),
    TRIOS(4, 3),
    QUADS(4, 4);

    /*
    Fields
     */
    private final int teams;
    private final int playersPerTeam;

    /*
    Methods
     */
    public int getWeight() {
        return getTeams() * getPlayersPerTeam();
    }
}
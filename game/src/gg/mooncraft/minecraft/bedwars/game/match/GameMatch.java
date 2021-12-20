package gg.mooncraft.minecraft.bedwars.game.match;

import lombok.Getter;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.slime.SlimeBukkitPair;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
public final class GameMatch {

    /*
    Fields
     */
    private final int id;
    private final @NotNull String identifier;
    private final @NotNull GameMode gameMode;
    private final @NotNull SlimeBukkitPair slimeBukkitPair;
    private final @NotNull List<GameMatchTeam> teamList = new LinkedList<>();

    /*
    Constructor
     */
    public GameMatch(int id, @NotNull String identifier, @NotNull GameMode gameMode, @NotNull SlimeBukkitPair slimeBukkitPair) {
        this.id = id;
        this.identifier = identifier;
        this.gameMode = gameMode;
        this.slimeBukkitPair = slimeBukkitPair;

        getBedWarsMap().ifPresent(bedWarsMap -> {
            bedWarsMap.getPointsContainer().getTeamPointList().forEach(teamMapPoint -> this.teamList.add(new GameMatchTeam(this.teamList.size(), teamMapPoint.getGameTeam())));
        });
    }

    /*
    Methods
     */
    public boolean findTeamFor(@NotNull List<UUID> playerList) {
        GameMatchTeam freeMatchTeam = null;
        for (GameMatchTeam gameMatchTeam : teamList) {
            int teamSize = gameMatchTeam.getMatchPlayerList().size();
            int freeSlots = gameMode.getPlayersPerTeam() - teamSize;
            if (freeSlots < playerList.size()) continue;
            freeMatchTeam = gameMatchTeam;
            break;
        }
        if (freeMatchTeam == null) return false;

        playerList.forEach(freeMatchTeam::addPlayer);
        return true;
    }

    public @NotNull Optional<BedWarsMap> getBedWarsMap() {
        return BedWarsPlugin.getInstance().getMapManager().getBedWarsMap(this.identifier);
    }
}
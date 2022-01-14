package gg.mooncraft.minecraft.bedwars.game.events;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.map.point.PointTypes;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchTeam;
import gg.mooncraft.minecraft.bedwars.game.utilities.PointAdapter;
import gg.mooncraft.minecraft.bedwars.game.utilities.WorldUtilities;

import java.util.Arrays;
import java.util.Optional;

@Getter
public class MatchBedBreakEvent extends MatchPlayerEvent implements Cancellable {

    /*
    Constants
     */
    private static final @NotNull HandlerList HANDLERS = new HandlerList();

    /*
    Fields
     */
    private final @NotNull Location[] bedParts;
    @Setter
    private boolean cancelled = false;

    /*
    Constructor
     */
    public MatchBedBreakEvent(@NotNull Player player, @NotNull GameMatchPlayer matchPlayer, @NotNull Location[] bedParts) {
        super(player, matchPlayer);
        this.bedParts = bedParts;
    }

    /*
    Methods
     */
    public boolean isOwnBed() {
        return getMatchTeam().getMapPointList()
                .stream()
                .filter(teamMapPoint -> teamMapPoint.getType() == PointTypes.TEAM.TEAM_BED)
                .anyMatch(teamMapPoint -> Arrays.stream(this.bedParts)
                        .anyMatch(part -> WorldUtilities.isSameXYZ(PointAdapter.adapt(getMatch(), teamMapPoint), part))
                );
    }

    public @NotNull Optional<GameMatchTeam> getMatchTeamOwner() {
        return getMatch().getTeamList()
                .stream()
                .filter(gameMatchTeam -> gameMatchTeam.getMapPointList()
                        .stream()
                        .anyMatch(teamMapPoint -> Arrays.stream(this.bedParts)
                                .anyMatch(part -> WorldUtilities.isSameXYZ(PointAdapter.adapt(getMatch(), teamMapPoint), part))))
                .findFirst();
    }
}
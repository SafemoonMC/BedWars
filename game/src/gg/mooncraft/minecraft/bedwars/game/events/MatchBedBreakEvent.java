package gg.mooncraft.minecraft.bedwars.game.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.map.point.PointTypes;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchTeam;
import gg.mooncraft.minecraft.bedwars.game.utilities.PointAdapter;
import gg.mooncraft.minecraft.bedwars.game.utilities.WorldUtilities;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public class MatchBedBreakEvent extends Event implements Cancellable {

    /*
    Constants
     */
    private static final @NotNull HandlerList HANDLERS = new HandlerList();

    /*
    Fields
     */
    private final @NotNull Player player;
    private final @NotNull Location[] bedParts;

    private final @NotNull GameMatch gameMatch;
    private final @NotNull GameMatchPlayer gameMatchPlayer;
    @Setter
    private boolean cancelled = false;

    /*
    Constructor
     */
    public MatchBedBreakEvent(@NotNull Player player, @NotNull Location[] bedParts, @NotNull GameMatch gameMatch, @NotNull GameMatchPlayer gameMatchPlayer) {
        this.player = player;
        this.bedParts = bedParts;
        this.gameMatch = gameMatch;
        this.gameMatchPlayer = gameMatchPlayer;
    }

    /*
    Methods
     */
    public boolean isOwnBed() {
        return getGameMatchTeam().getMapPointList()
                .stream()
                .filter(teamMapPoint -> teamMapPoint.getType() == PointTypes.TEAM.TEAM_BED)
                .anyMatch(teamMapPoint -> Arrays.stream(this.bedParts)
                        .anyMatch(part -> WorldUtilities.isSameXYZ(PointAdapter.adapt(this.gameMatch, teamMapPoint), part))
                );
    }

    public @NotNull Optional<GameMatchTeam> getGameMatchTeamOwner() {
        return this.gameMatch.getTeamList()
                .stream()
                .filter(gameMatchTeam -> gameMatchTeam.getMapPointList()
                        .stream()
                        .anyMatch(teamMapPoint -> Arrays.stream(this.bedParts)
                                .anyMatch(part -> WorldUtilities.isSameXYZ(PointAdapter.adapt(this.gameMatch, teamMapPoint), part))))
                .findFirst();
    }

    public @NotNull GameMatchTeam getGameMatchTeam() {
        return this.gameMatchPlayer.getParent();
    }

    /*
    Static Methods
     */
    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

    /*
    Override Methods
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}

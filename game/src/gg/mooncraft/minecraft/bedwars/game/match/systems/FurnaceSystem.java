package gg.mooncraft.minecraft.bedwars.game.match.systems;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.data.map.MapPointsContainer;
import gg.mooncraft.minecraft.bedwars.data.map.point.PointTypes;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.FurnaceTask;

import java.util.ArrayList;
import java.util.List;

public final class FurnaceSystem implements TickSystem {

    /*
    Fields
     */
    private final @NotNull GameMatch gameMatch;
    private final @NotNull List<FurnaceTask> taskList;

    /*
    Constructor
     */
    public FurnaceSystem(@NotNull GameMatch gameMatch) {
        this.gameMatch = gameMatch;
        this.taskList = new ArrayList<>();
        gameMatch.getBedWarsMap()
                .map(BedWarsMap::getPointsContainer)
                .map(MapPointsContainer::getTeamPointList)
                .ifPresent(list -> {
                    list.stream()
                            .filter(mapPoint -> mapPoint.getGameMode() == gameMatch.getGameMode())
                            .filter(mapPoint -> mapPoint.getType() == PointTypes.TEAM.TEAM_GENERATOR)
                            .forEach(mapPoint -> {
                                FurnaceTask furnaceTask = new FurnaceTask(gameMatch, mapPoint.getGameTeam(), mapPoint);
                                this.taskList.add(furnaceTask);
                            });
                });
    }

    /*
    Override Methods
     */
    @Override
    public void tick() {
        this.taskList.forEach(FurnaceTask::run);
    }

}
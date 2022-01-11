package gg.mooncraft.minecraft.bedwars.game.match.systems;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.data.map.MapPointsContainer;
import gg.mooncraft.minecraft.bedwars.data.map.point.PointTypes;
import gg.mooncraft.minecraft.bedwars.game.match.GameEvent;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GeneratorType;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.GeneratorTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GeneratorSystem implements TickSystem {

    /*
    Fields
     */
    private final @NotNull GameMatch gameMatch;
    private final @NotNull List<GeneratorTask> taskList;

    /*
    Constructor
     */
    public GeneratorSystem(@NotNull GameMatch gameMatch) {
        this.gameMatch = gameMatch;
        this.taskList = new ArrayList<>();
    }

    /*
    Override Methods
     */
    public void play() {
        this.gameMatch.getBedWarsMap()
                .map(BedWarsMap::getPointsContainer)
                .map(MapPointsContainer::getMapPointList)
                .ifPresent(list -> {
                    list.stream()
                            .filter(gameMapPoint -> gameMapPoint.getGameMode() == gameMatch.getGameMode())
                            .forEach(gameMapPoint -> {
                                GeneratorType generatorType;
                                if (gameMapPoint.getType() == PointTypes.MAP.MAP_GENERATOR_DIAMOND) {
                                    generatorType = GeneratorType.DIAMOND;
                                } else if (gameMapPoint.getType() == PointTypes.MAP.MAP_GENERATOR_EMERALD) {
                                    generatorType = GeneratorType.EMERALD;
                                } else {
                                    return;
                                }

                                GeneratorTask generatorTask = new GeneratorTask(gameMatch, gameMapPoint, generatorType);
                                this.taskList.add(generatorTask);
                            });
                });
    }

    @Override
    public void tick() {
        this.taskList.forEach(GeneratorTask::run);
    }

    /*
    Methods
     */
    public int getDiamondTier() {
        long count = this.gameMatch.getEventSystem().getEventList()
                .stream()
                .filter(gameMatchEvent -> gameMatchEvent.getTimeLeft() == -1)
                .filter(gameMatchEvent -> gameMatchEvent.getGameEvent() == GameEvent.DIAMOND)
                .count();
        return (int) (count + 1);
    }

    public int getNextDiamondTier() {
        return getDiamondTier() + 1;
    }

    public int getEmeraldTier() {
        long count = this.gameMatch.getEventSystem().getEventList()
                .stream()
                .filter(gameMatchEvent -> gameMatchEvent.getTimeLeft() == -1)
                .filter(gameMatchEvent -> gameMatchEvent.getGameEvent() == GameEvent.EMERALD)
                .count();
        return (int) (count + 1);
    }

    public int getNextEmeraldTier() {
        return getEmeraldTier() + 1;
    }

    @UnmodifiableView
    public @NotNull List<GeneratorTask> getTaskList() {
        return Collections.unmodifiableList(this.taskList);
    }
}
package gg.mooncraft.minecraft.bedwars.game.match.systems;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.data.map.MapPointsContainer;
import gg.mooncraft.minecraft.bedwars.data.map.point.PointTypes;
import gg.mooncraft.minecraft.bedwars.game.match.GameEvent;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GeneratorType;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.GeneratorTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class GeneratorSystem {

    /*
    Fields
     */
    private final @NotNull GameMatch gameMatch;
    private final @NotNull AtomicInteger diamondTier;
    private final @NotNull AtomicInteger emeraldTier;
    private final @NotNull List<GeneratorTask> taskList;

    /*
    Constructor
     */
    public GeneratorSystem(@NotNull GameMatch gameMatch) {
        this.gameMatch = gameMatch;
        this.diamondTier = new AtomicInteger(1);
        this.emeraldTier = new AtomicInteger(1);
        this.taskList = new ArrayList<>();
        gameMatch.getBedWarsMap().map(BedWarsMap::getPointsContainer).map(MapPointsContainer::getMapPointList).ifPresent(list -> {
            list.forEach(gameMapPoint -> {
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

    /*
    Methods
     */
    public void tick() {
        this.taskList.forEach(GeneratorTask::run);
    }

    public void updateDiamondTier() {
        if (this.diamondTier.get() == GameEvent.DIAMOND.getMaximumTier()) return;
        this.diamondTier.incrementAndGet();
    }

    public void updateEmeraldTier() {
        if (this.emeraldTier.get() == GameEvent.EMERALD.getMaximumTier()) return;
        this.emeraldTier.incrementAndGet();
    }

    public int getDiamondTier() {
        return diamondTier.get();
    }

    public int getNextDiamondTier() {
        return diamondTier.get() + 1;
    }

    public int getEmeraldTier() {
        return emeraldTier.get();
    }

    public int getNextEmeraldTier() {
        return emeraldTier.get() + 1;
    }
}
package gg.mooncraft.minecraft.bedwars.game.match.systems;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.data.map.MapPointsContainer;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.utilities.PointAdapter;
import gg.mooncraft.minecraft.bedwars.game.utilities.WorldUtilities;

import java.util.ArrayList;
import java.util.List;

public final class BlocksSystem {

    /*
    Fields
     */
    private final @NotNull GameMatch gameMatch;
    private final @NotNull List<Location> blocksList;

    /*
    Constructor
     */
    public BlocksSystem(@NotNull GameMatch gameMatch) {
        this.gameMatch = gameMatch;
        this.blocksList = new ArrayList<>();
    }

    /*
    Methods
     */
    public void placeBlock(@NotNull Location location) {
        this.blocksList.add(location);
    }

    public void placeBlocks(@NotNull List<Location> locationList) {
        this.blocksList.addAll(locationList);
    }

    public void breakBlock(@NotNull Location location) {
        this.blocksList.remove(location);
    }

    public void breakBlocks(@NotNull List<Location> locationList) {
        this.blocksList.removeAll(locationList);
    }

    public boolean canPlace(@NotNull Location location) {
        if (location.getBlockY() > gameMatch.getBedWarsMap().map(BedWarsMap::getPointsContainer).map(MapPointsContainer::getMaximumBlockHeight).orElse(0) || location.getBlockY() < gameMatch.getBedWarsMap().map(BedWarsMap::getPointsContainer).map(MapPointsContainer::getMinimumBlockHeight).orElse(0)) {
            return false;
        }
        return gameMatch.getBedWarsMap().map(BedWarsMap::getPointsContainer).map(MapPointsContainer::getMapPointList).stream().flatMap(List::stream).noneMatch(gameMapPoint -> {
            Location pointLocation = PointAdapter.adapt(gameMatch, gameMapPoint);
            return WorldUtilities.isSameArea(location, pointLocation, 3, 3, 3, true);
        });
    }

    public boolean canBreak(@NotNull Location location) {
        return this.blocksList.stream().anyMatch(streamLocation -> WorldUtilities.isSameXYZ(streamLocation, location));
    }
}
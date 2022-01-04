package gg.mooncraft.minecraft.bedwars.game.match.systems;

import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.data.map.MapPointsContainer;
import gg.mooncraft.minecraft.bedwars.data.map.point.PointTypes;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.VillagerType;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.MatchVillager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class VillagersSystem {

    /*
    Fields
     */
    private final @NotNull GameMatch gameMatch;
    private final @NotNull List<MatchVillager> villagerList;

    /*
    Constructor
     */
    public VillagersSystem(@NotNull GameMatch gameMatch) {
        this.gameMatch = gameMatch;
        this.villagerList = new ArrayList<>();
        gameMatch.getBedWarsMap()
                .map(BedWarsMap::getPointsContainer)
                .map(MapPointsContainer::getTeamPointList)
                .ifPresent(list -> {
                    list.stream()
                            .filter(mapPoint -> mapPoint.getGameMode() == gameMatch.getGameMode())
                            .forEach(mapPoint -> {
                                VillagerType villagerType = mapPoint.getType() == PointTypes.TEAM.TEAM_SHOP ? VillagerType.ITEM_SHOP : mapPoint.getType() == PointTypes.TEAM.TEAM_SHOP_UPGRADES ? VillagerType.UPGRADE_SHOP : null;
                                if (villagerType == null) return;
                                MatchVillager matchVillager = new MatchVillager(gameMatch, villagerType, mapPoint);
                                this.villagerList.add(matchVillager);
                            });
                });
    }

    /*
    Methods
     */
    public @NotNull Optional<MatchVillager> getMatchVillager(@NotNull LivingEntity livingEntity) {
        return this.villagerList.stream().filter(matchVillager -> matchVillager.isVillager(livingEntity)).findFirst();
    }
}
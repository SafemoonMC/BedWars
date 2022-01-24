package gg.mooncraft.minecraft.bedwars.game.match.tasks;

import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.map.point.AbstractMapPoint;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.VillagerType;

@Getter
public final class MatchVillager {

    /*
    Fields
     */
    private final @NotNull GameMatch gameMatch;
    private final @NotNull VillagerType villagerType;

    private Villager villager;

    /*
    Constructor
     */
    public MatchVillager(@NotNull GameMatch gameMatch, @NotNull VillagerType villagerType, @NotNull AbstractMapPoint mapPoint) {
        this.gameMatch = gameMatch;
        this.villagerType = villagerType;

        Location location = gameMatch.getDimension().getLocation(mapPoint.getX(), mapPoint.getY(), mapPoint.getZ(), mapPoint.getYaw(), mapPoint.getPitch());
        BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
            this.villager = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
            this.villager.setProfession(villagerType == VillagerType.ITEM_SHOP ? Villager.Profession.WEAPONSMITH : Villager.Profession.LIBRARIAN);
            this.villager.setAI(false);
            this.villager.setGravity(false);
            this.villager.setInvulnerable(true);
            this.villager.setCustomNameVisible(true);
            this.villager.setCustomName(villagerType.getDisplay());
        });
    }

    /*
    Methods
     */
    public boolean isVillager(@NotNull LivingEntity livingEntity) {
        if (this.villager == null) return false;
        return this.villager.equals(livingEntity);
    }
}
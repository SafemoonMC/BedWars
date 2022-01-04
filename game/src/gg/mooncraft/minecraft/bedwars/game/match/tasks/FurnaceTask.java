package gg.mooncraft.minecraft.bedwars.game.match.tasks;

import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameTeam;
import gg.mooncraft.minecraft.bedwars.data.map.point.AbstractMapPoint;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.options.MatchOptions;
import gg.mooncraft.minecraft.bedwars.game.utilities.EntityUtilities;

import java.util.concurrent.ThreadLocalRandom;

@Getter
public class FurnaceTask extends GameRunnable {

    /*
    Fields
     */
    private final @NotNull GameMatch gameMatch;
    private final @NotNull GameTeam gameTeam;
    private final @NotNull Location location;

    /*
    Constructor
     */
    public FurnaceTask(@NotNull GameMatch gameMatch, @NotNull GameTeam gameTeam, @NotNull AbstractMapPoint mapPoint) {
        super();
        this.gameMatch = gameMatch;
        this.gameTeam = gameTeam;
        this.location = gameMatch.getDimension().getLocation(mapPoint.getX(), mapPoint.getY(), mapPoint.getZ(), mapPoint.getYaw(), mapPoint.getPitch());
        System.out.println("Default: " + MatchOptions.getMatchOption(gameMatch.getGameMode()).getDefaultResourceDropRate());
    }

    /*
    Override Methods
     */
    @Override
    public void tick() {
        if (getTick() == MatchOptions.getMatchOption(gameMatch.getGameMode()).getDefaultResourceDropRate()) {
            Material[] dropArray = {Material.IRON_INGOT, Material.GOLD_INGOT};
            Material drop = dropArray[ThreadLocalRandom.current().nextInt(dropArray.length)];

            ItemStack itemStack = new ItemStack(drop);
            EntityUtilities.spawnItemStack(this.location, itemStack);
        }
    }
}
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
import gg.mooncraft.minecraft.bedwars.game.utilities.ItemsUtilities;

import java.util.concurrent.ThreadLocalRandom;

@Getter
public class FurnaceTask extends GameRunnable {

    /*
    Fields
     */
    private final @NotNull GameMatch gameMatch;
    private final @NotNull GameTeam gameTeam;
    private final @NotNull Location location;

    private int ironTicking;
    private int goldTicking;


    /*
    Constructor
     */
    public FurnaceTask(@NotNull GameMatch gameMatch, @NotNull GameTeam gameTeam, @NotNull AbstractMapPoint mapPoint) {
        super();
        this.gameMatch = gameMatch;
        this.gameTeam = gameTeam;
        this.location = gameMatch.getDimension().getLocation(mapPoint.getX(), mapPoint.getY(), mapPoint.getZ(), mapPoint.getYaw(), mapPoint.getPitch());
    }

    /*
    Override Methods
     */
    @Override
    public void tick() {
        ironTicking += 1;
        goldTicking += 1;
        if (ironTicking == MatchOptions.getMatchOption(gameMatch.getGameMode()).getResourceIronDropRate()) {
            ItemStack itemStack = ItemsUtilities.createPureItem(Material.IRON_INGOT);
            itemStack.setAmount(getAmount());
            EntityUtilities.spawnItemStack(this.location, itemStack);

            this.ironTicking = 0;
        }
        if (goldTicking == MatchOptions.getMatchOption(gameMatch.getGameMode()).getResourceGoldDropRate()) {
            ItemStack itemStack = ItemsUtilities.createPureItem(Material.GOLD_INGOT);
            itemStack.setAmount(getAmount());

            EntityUtilities.spawnItemStack(this.location, itemStack);

            this.goldTicking = 0;
        }
    }

    /*
    Methods
     */
    private int getAmount() {
        int level = this.gameMatch.getTeam(this.gameTeam).map(gameMatchTeam -> gameMatchTeam.getUpgradeTier("furnace")).orElse(0);
        if (level == 4) {
            return 2;
        } else if (level >= 1 && level < 4) {
            return ThreadLocalRandom.current().nextInt(1, 3);
        } else {
            return 1;
        }
    }
}
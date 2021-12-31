package gg.mooncraft.minecraft.bedwars.game.match.tasks;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.map.point.AbstractMapPoint;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GeneratorType;
import gg.mooncraft.minecraft.bedwars.game.utilities.EntityUtilities;

import java.util.concurrent.atomic.AtomicInteger;

public class GeneratorTask implements Runnable {

    /*
    Fields
     */
    private final @NotNull GameMatch gameMatch;
    private final @NotNull AbstractMapPoint mapPoint;
    private final @NotNull GeneratorType type;
    private final @NotNull AtomicInteger ticking;

    private ArmorStand armorStand;
    private boolean spawnedArmorStand;

    /*
    Constructor
     */
    public GeneratorTask(@NotNull GameMatch gameMatch, @NotNull AbstractMapPoint mapPoint, @NotNull GeneratorType type) {
        this.gameMatch = gameMatch;
        this.mapPoint = mapPoint;
        this.type = type;
        this.ticking = new AtomicInteger(0);
    }

    /*
    Override Methods
     */
    @Override
    public void run() {
        int tick = updateTick();
        if (this.armorStand == null && !spawnedArmorStand) {
            this.spawnedArmorStand = true;

            Location location = gameMatch.getDimension().getLocation(mapPoint.getX(), mapPoint.getY(), mapPoint.getZ(), mapPoint.getYaw(), mapPoint.getPitch());
            BedWarsPlugin.getInstance().getScheduler().executeSync(() -> this.armorStand = EntityUtilities.createGeneratorStand(location, type.getHeadMaterial()));
            return;
        }
        if (this.armorStand == null) return;

        // Update head pose
        this.armorStand.setHeadPose(this.armorStand.getHeadPose().add(0, 0.15, 0));
    }


    /*
    Methods
     */
    private int updateTick() {
        int newTick = ticking.incrementAndGet();
        if (newTick > 20) ticking.set(0);

        return ticking.get();
    }
}
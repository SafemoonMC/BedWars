package gg.mooncraft.minecraft.bedwars.game.match.tasks;

import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameState;
import gg.mooncraft.minecraft.bedwars.data.GameTeam;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.utilities.DragonUtilities;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class DragonTask extends GameRunnable {

    /*
    Fields
     */
    private final @NotNull GameMatch gameMatch;
    private final @NotNull GameTeam gameTeam;
    private final @NotNull Location location;

    private EnderDragon enderDragon;
    private boolean spawnedEnderDragon;

    private final AtomicInteger lifetime = new AtomicInteger();

    /*
    Constructor
     */
    public DragonTask(@NotNull GameMatch gameMatch, @NotNull GameTeam gameTeam, @NotNull Location location) {
        super();
        this.gameMatch = gameMatch;
        this.gameTeam = gameTeam;
        this.location = location;
    }

    /*
    Override Methods
     */
    @Override
    public void tick() {
        if (this.gameMatch.getGameState() != GameState.PLAYING) return;

        if (!spawnedEnderDragon) {
            this.spawnedEnderDragon = true;
            BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
                this.enderDragon = (EnderDragon) location.getWorld().spawnEntity(location, EntityType.ENDER_DRAGON);
                this.enderDragon.setCustomName(gameTeam.name());
            });
            return;
        }
        if (this.enderDragon.isDead()) return;

        int tick = getTick();
        if (tick == 20) {
            this.lifetime.incrementAndGet();
        }

        if (this.lifetime.get() % 10 == 0) {
            BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
                gameMatch.getMatchPlayerList()
                        .stream()
                        .filter(gameMatchPlayer -> gameMatchPlayer.getParent().getGameTeam() != gameTeam)
                        .findFirst()
                        .flatMap(GameMatchPlayer::getPlayer)
                        .ifPresent(player -> DragonUtilities.setTarget(enderDragon, player));
            });
        }
    }
}
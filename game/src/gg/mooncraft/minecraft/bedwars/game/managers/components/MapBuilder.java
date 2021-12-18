package gg.mooncraft.minecraft.bedwars.game.managers.components;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.MapDAO;
import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.GameConstants;
import gg.mooncraft.minecraft.bedwars.game.managers.SlimeBukkitPair;

@Getter
public final class MapBuilder {

    /*
    Fields
     */
    private final @NotNull Player player;
    private final @NotNull String name;
    private final @NotNull BedWarsMap bedWarsMap;
    private final @NotNull SlimeBukkitPair slimeBukkitPair;

    /*
    Constructor
     */
    public MapBuilder(@NotNull Player player, @NotNull String name, @NotNull BedWarsMap bedWarsMap, @NotNull SlimeBukkitPair slimeBukkitPair) {
        this.player = player;
        this.name = name;
        this.bedWarsMap = bedWarsMap;
        this.slimeBukkitPair = slimeBukkitPair;
    }

    /*
    Methods
     */
    public void cancel() {
        clearWorld();

        MapDAO.delete(bedWarsMap).thenAccept(oldBedWarsMap -> BedWarsPlugin.getInstance().getSlimeManager().unloadPairAsync(slimeBukkitPair));

        BedWarsPlugin.getInstance().getSetupManager().stopSetup(this);
    }

    public void complete() {
        clearWorld();

        slimeBukkitPair.world().save();
        BedWarsPlugin.getAsyncSlimeLoader().unlockWorld(slimeBukkitPair.slimeWorld().getName());

        BedWarsPlugin.getInstance().getSetupManager().stopSetup(this);
        BedWarsPlugin.getInstance().getMapManager().storeMap(name, bedWarsMap, slimeBukkitPair);
    }

    public void clearWorld() {
        World world = Bukkit.getWorld(GameConstants.DEFAULT_WORLD_NAME);

        slimeBukkitPair.world().getPlayers().forEach(streamPlayer -> {
            if (world != null) {
                player.teleport(world.getSpawnLocation());
            } else {
                player.kickPlayer("");
            }
        });
    }
}
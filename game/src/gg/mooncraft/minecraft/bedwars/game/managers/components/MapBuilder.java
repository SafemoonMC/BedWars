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
        MapDAO.delete(bedWarsMap);
        BedWarsPlugin.getInstance().getSlimeManager().unloadPairAsync(slimeBukkitPair).thenAccept(unloaded -> {
            if (unloaded) {
                try {
                    BedWarsPlugin.getInstance().getSlimeManager().getSlimeLoader().deleteWorld(name);
                } catch (Exception e) {
                    BedWarsPlugin.getInstance().getLogger().warning("The world " + name + " cannot be deleted! Exception: " + e.getMessage());
                }
            }
        });

        World world = Bukkit.getWorld(GameConstants.DEFAULT_WORLD_NAME);
        if (world != null) {
            player.teleport(world.getSpawnLocation());
        } else {
            player.kickPlayer("The setup has been cancelled, but default-world " + GameConstants.DEFAULT_WORLD_NAME + " was missing.");
        }
    }

    public void complete() {
        World world = Bukkit.getWorld(GameConstants.DEFAULT_WORLD_NAME);

        slimeBukkitPair.world().save();
        slimeBukkitPair.world().getPlayers().forEach(player -> {
            if (world != null) {
                player.teleport(world.getSpawnLocation());
            } else {
                player.kickPlayer("The setup has been completed, but default-world " + GameConstants.DEFAULT_WORLD_NAME + " was missing.");
            }
        });
        
        BedWarsPlugin.getInstance().getMapManager().storeMap(name, bedWarsMap, slimeBukkitPair);
    }
}
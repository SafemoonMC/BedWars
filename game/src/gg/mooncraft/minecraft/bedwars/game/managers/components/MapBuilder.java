package gg.mooncraft.minecraft.bedwars.game.managers.components;

import lombok.Getter;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.MapDAO;
import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
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
    }

    public void complete() {
        slimeBukkitPair.world().save();
        slimeBukkitPair.world().getPlayers().forEach(player -> player.kickPlayer("That map has been set-up."));

        BedWarsPlugin.getInstance().getMapManager().storeMap(name, bedWarsMap, slimeBukkitPair);
    }
}
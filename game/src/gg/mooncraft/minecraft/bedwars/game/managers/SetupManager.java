package gg.mooncraft.minecraft.bedwars.game.managers;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.MapDAO;
import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.data.map.MapInfo;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.managers.components.MapBuilder;
import gg.mooncraft.minecraft.bedwars.game.slime.SlimeBukkitPair;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class SetupManager {

    /*
    Fields
     */
    private final @NotNull List<MapBuilder> mapBuilderList = new ArrayList<>();

    /*
    Methods
     */
    public void startSetup(@NotNull Player player, @NotNull String mapName) {
        Optional<BedWarsMap> optionalBedWarsMap = BedWarsPlugin.getInstance().getMapManager().getBedWarsMap(mapName);
        Optional<SlimeBukkitPair> optionalSlimeBukkitPair = BedWarsPlugin.getInstance().getMapManager().getSlimeBukkitPair(mapName);

        if (optionalBedWarsMap.isPresent() && optionalSlimeBukkitPair.isPresent()) {
            BedWarsMap bedWarsMap = optionalBedWarsMap.get();
            SlimeBukkitPair slimeBukkitPair = optionalSlimeBukkitPair.get();

            MapBuilder mapBuilder = new MapBuilder(player, mapName, bedWarsMap, slimeBukkitPair);
            this.mapBuilderList.add(mapBuilder);

            player.teleport(slimeBukkitPair.world().getSpawnLocation());
            player.setFlying(true);
            player.setAllowFlight(true);
            player.setGameMode(GameMode.CREATIVE);
        } else {
            BedWarsPlugin.getInstance().getSlimeManager().createPairAsync(mapName).thenAccept(slimeBukkitPair -> {
                BedWarsMap bedWarsMap = new BedWarsMap(mapName, new MapInfo(mapName, Timestamp.from(Instant.now())));
                MapDAO.create(bedWarsMap).thenAccept(newBedWarsMap -> {
                    MapBuilder mapBuilder = new MapBuilder(player, mapName, newBedWarsMap, slimeBukkitPair);
                    this.mapBuilderList.add(mapBuilder);

                    BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
                        player.teleport(slimeBukkitPair.world().getSpawnLocation());
                        player.getLocation().getBlock().getRelative(BlockFace.DOWN).setType(Material.BEDROCK);

                        player.setFlying(true);
                        player.setAllowFlight(true);
                        player.setGameMode(GameMode.CREATIVE);
                    });
                });
            });
        }
    }

    public void stopSetup(@NotNull MapBuilder mapBuilder) {
        this.mapBuilderList.remove(mapBuilder);
    }

    public @NotNull Optional<MapBuilder> getMapBuilder(@NotNull Player player) {
        return this.mapBuilderList.stream().filter(mapBuilder -> mapBuilder.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst();
    }
}
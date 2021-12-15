package gg.mooncraft.minecraft.bedwars.game.managers;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.MapDAO;
import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.data.map.MapInfo;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.managers.components.MapBuilder;

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
        BedWarsPlugin.getInstance().getSlimeManager().createPairAsync(mapName).thenAccept(slimeBukkitPair -> {
            BedWarsMap bedWarsMap = new BedWarsMap(mapName, new MapInfo(mapName, Timestamp.from(Instant.now())));
            MapDAO.create(bedWarsMap).thenAccept(newBedWarsMap -> {
                player.teleport(slimeBukkitPair.world().getSpawnLocation());

                MapBuilder mapBuilder = new MapBuilder(player, mapName, bedWarsMap, slimeBukkitPair);
                this.mapBuilderList.add(mapBuilder);
            });
        });
    }

    public void stopSetup(@NotNull MapBuilder mapBuilder) {
        this.mapBuilderList.remove(mapBuilder);
    }

    public @NotNull Optional<MapBuilder> getMapBuilder(@NotNull Player player) {
        return this.mapBuilderList.stream().filter(mapBuilder -> mapBuilder.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst();
    }
}
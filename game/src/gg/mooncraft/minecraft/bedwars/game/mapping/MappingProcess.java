package gg.mooncraft.minecraft.bedwars.game.mapping;

import lombok.Getter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import gg.mooncraft.minecraft.bedwars.game.slime.SlimeBukkitPair;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Getter
public final class MappingProcess {

    /*
    Record
     */
    public static record MappingResult(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
    }

    /*
    Fields
     */
    private final @NotNull SlimeBukkitPair world;
    private final @NotNull MappingTask mappingTask;
    private final @NotNull CompletableFuture<MappingResult> futureResult;

    /*
    Constructor
     */
    public MappingProcess(@NotNull SlimeBukkitPair world) {
        this.world = world;
        this.mappingTask = new MappingTask(this);
        this.futureResult = new CompletableFuture<>();
    }

    /*
    Methods
     */
    public CompletableFuture<MappingResult> retrieve() {
        this.mappingTask.iterate();
        return futureResult;
    }

    @Unmodifiable @NotNull List<Map.Entry<Integer, Integer>> getBounds(int steps) {
        if (steps == 1) {
            return List.of(new AbstractMap.SimpleEntry<>(0, 0));
        }

        List<Map.Entry<Integer, Integer>> list = new ArrayList<>();

        int min = -steps + 1;
        int max = steps - 1;

        for (int i = min; i <= max; i++) {
            list.add(new AbstractMap.SimpleEntry<>(max, i));
            list.add(new AbstractMap.SimpleEntry<>(min, i));
            list.add(new AbstractMap.SimpleEntry<>(i, max));
            list.add(new AbstractMap.SimpleEntry<>(i, min));
        }

        return list;
    }
}
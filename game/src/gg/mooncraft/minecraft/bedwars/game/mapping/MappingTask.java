package gg.mooncraft.minecraft.bedwars.game.mapping;

import com.google.common.base.Stopwatch;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MappingTask implements Runnable {

    /*
    Fields
     */
    private final @NotNull MappingProcess mappingProcess;
    private final @NotNull List<ChunkResult> chunkResultList = new ArrayList<>();

    private final @NotNull Stopwatch stopwatch;
    private final @NotNull AtomicInteger emptyCounter = new AtomicInteger();
    private final @NotNull AtomicInteger stepsCounter = new AtomicInteger();

    /*
    Constructor
     */
    public MappingTask(@NotNull MappingProcess mappingProcess) {
        this.mappingProcess = mappingProcess;
        this.stopwatch = Stopwatch.createUnstarted();
    }

    /*
    Methods
     */
    void iterate() {
        BedWarsPlugin.getInstance().getScheduler().asyncLater(this, 0, TimeUnit.SECONDS);
    }


    /*
    Override Methods
     */
    @Override
    public void run() {
        // Start the stopwatch
        if (!this.stopwatch.isRunning()) {
            this.stopwatch.start();
        }

        // Get the list of chunks coordinates
        List<Map.Entry<Integer, Integer>> list = this.mappingProcess.getBounds(this.stepsCounter.incrementAndGet());

        // Scan the chunks and get the results. Empty chunks are not considered.
        List<ChunkResult> chunkResultList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : list) {
            ChunkResult chunkResult = mappingProcess.getWorld().world().getChunkAtAsync(entry.getKey(), entry.getValue())
                    .thenApply(chunk -> new ChunkSupplier(this.mappingProcess.getWorld().world(), chunk).get())
                    .join();
            if (chunkResult.isEmpty()) continue;
            chunkResultList.add(chunkResult);
        }

        // Store the results
        this.chunkResultList.addAll(chunkResultList);

        // If the current step didn't produce any result, it could be the end of map.
        // Checking for 2 empty steps before determining that.
        if (chunkResultList.isEmpty()) {
            int emptySteps = this.emptyCounter.incrementAndGet();
            if (emptySteps == 2) {
                long elapsedTime = this.stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
                System.out.printf("The mapping result has been processed in %.2f", (double) elapsedTime / 1000);

                int maxX = this.chunkResultList.stream().max(Comparator.comparingInt(ChunkResult::getMaxX)).map(ChunkResult::getMaxX).orElse(0);
                int maxY = this.chunkResultList.stream().max(Comparator.comparingInt(ChunkResult::getMaxY)).map(ChunkResult::getMaxY).orElse(0);
                int maxZ = this.chunkResultList.stream().max(Comparator.comparingInt(ChunkResult::getMaxZ)).map(ChunkResult::getMaxZ).orElse(0);
                int minX = this.chunkResultList.stream().min(Comparator.comparingInt(ChunkResult::getMinX)).map(ChunkResult::getMinX).orElse(0);
                int minY = this.chunkResultList.stream().min(Comparator.comparingInt(ChunkResult::getMinY)).map(ChunkResult::getMinY).orElse(0);
                int minZ = this.chunkResultList.stream().min(Comparator.comparingInt(ChunkResult::getMinZ)).map(ChunkResult::getMinZ).orElse(0);

                MappingProcess.MappingResult result = new MappingProcess.MappingResult(minX, minY, minZ, maxX, maxY, maxZ);
                this.mappingProcess.getFutureResult().complete(result);
                return;
            }
        }
        iterate();
    }
}
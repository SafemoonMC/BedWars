package gg.mooncraft.minecraft.bedwars.game.mapping;

import lombok.AllArgsConstructor;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@AllArgsConstructor
public final class ChunkSupplier implements Supplier<ChunkResult> {

    /*
    Fields
     */
    private final @NotNull World world;
    private final @NotNull Chunk chunk;

    /*
    Override Methods
     */
    @Override
    public @NotNull ChunkResult get() {
        int minX = 0;
        int minY = -1;
        int minZ = 0;
        int maxX = 0;
        int maxY = 0;
        int maxZ = 0;
        boolean empty = true;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < world.getMaxHeight(); y++) {
                    Block block = chunk.getBlock(x, y, z);
                    if (!block.getType().isAir()) {
                        empty = false;
                        if (block.getX() > maxX) {
                            maxX = block.getX();
                        }
                        if (block.getY() > maxY) {
                            maxY = block.getY();
                        }
                        if (block.getZ() > maxZ) {
                            maxZ = block.getZ();
                        }
                        if (block.getX() < minX) {
                            minX = block.getX();
                        }
                        if (block.getY() > minY && minY == -1) {
                            minY = block.getY();
                        }
                        if (block.getZ() < minZ) {
                            minZ = block.getZ();
                        }
                    }
                }
            }
        }
        return new ChunkResult(minX, minY, minZ, maxX, maxY, maxZ, empty);
    }
}
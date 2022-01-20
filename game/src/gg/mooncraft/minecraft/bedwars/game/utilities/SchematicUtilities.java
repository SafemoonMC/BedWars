package gg.mooncraft.minecraft.bedwars.game.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockTypes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchTeam;

import java.io.File;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class SchematicUtilities {

    public static void placeBlocks(@NotNull Player player, @NotNull Location location, @NotNull GameMatchTeam gameMatchTeam) throws IOException {
        File file = new File(BedWarsPlugin.getInstance().getDataFolder(), "tower.schem");
        if (!file.exists()) {
            BedWarsPlugin.getInstance().getLogger().warning("The tower.schem is not present!");
            return;
        }
        double xOffset = player.getLocation().getX() >= location.getX() ? -2.0 : 2.0;
        double zOffset = player.getLocation().getZ() >= location.getZ() ? -2.0 : 2.0;

        location.add(zOffset > 0 ? 0 : xOffset, 0, xOffset > 0 ? 0 : zOffset);

        World world = BukkitAdapter.adapt(location.getWorld());
        Clipboard clipboard = ClipboardFormats.findByExtension("schem").load(file);
        clipboard.setOrigin(clipboard.getOrigin().add(0, -2, 0));

        int relx = location.getBlockX() - clipboard.getOrigin().getBlockX();
        int rely = location.getBlockY() - clipboard.getOrigin().getBlockY();
        int relz = location.getBlockZ() - clipboard.getOrigin().getBlockZ();

        ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard);

        float yaw = player.getLocation().getYaw();
        float yawFixed = 0;
        if (yaw >= 0 && yaw < 45 || yaw >= 315) {
            yawFixed = 3;
        } else if (yaw >= 45.0 && yaw < 135.0) {
            yawFixed = 2;
        } else if (yaw >= 135.0 && yaw < 225.0) {
            yawFixed = 1;
        }
        yawFixed *= 90;

        AffineTransform affineTransform = new AffineTransform().rotateY(yawFixed);
        clipboardHolder.setTransform(affineTransform);

        EditSession editSession = FaweAPI.getEditSessionBuilder(world)
                .autoQueue(true)
                .checkMemory(false)
                .allowedRegionsEverywhere()
                .limitUnlimited()
                .changeSetNull()
                .build();

        Material material = ItemsUtilities.createWoolItem(gameMatchTeam.getGameTeam()).getType();
        for (BlockVector3 position : clipboard) {
            BlockState blockState = position.getBlock(editSession);
            if (blockState.getBlockType() == BlockTypes.WHITE_WOOL) {
                editSession.setBlock(position.getBlockX(), position.getBlockY(), position.getBlockZ(), new BaseBlock(BlockTypes.parse(material.name()).getDefaultState()));
            }
        }

        Operation operation = clipboardHolder.createPaste(editSession)
                .ignoreAirBlocks(true)
                .filter(position -> {
                    int xx = position.getBlockX() + relx;
                    int yy = position.getBlockY() + rely;
                    int zz = position.getBlockZ() + relz;

                    Location blockLocation = location.getWorld().getBlockAt(xx, yy, zz).getLocation();
                    boolean canPlace = gameMatchTeam.getParent().getBlocksSystem().canPlace(blockLocation);

                    if (canPlace) {
                        gameMatchTeam.getParent().getBlocksSystem().placeBlock(blockLocation);
                        location.getWorld().spawnParticle(Particle.CLOUD, blockLocation, 2, 0.0, 0.0, 0.0, 0.0);
                    }
                    return canPlace;
                })
                .copyEntities(false)
                .copyBiomes(false)
                .to(BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ()))
                .build();
        Operations.complete(operation);
        editSession.flushQueue();
    }
}
package gg.mooncraft.minecraft.bedwars.game.managers;

import com.grinderwolf.swm.api.world.SlimeWorld;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public record SlimeBukkitPair(@NotNull SlimeWorld slimeWorld, @NotNull World world,
                              boolean persistent) {

}
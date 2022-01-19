package gg.mooncraft.minecraft.bedwars.game.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.boss.EntityComplexPart;
import net.minecraft.world.entity.boss.enderdragon.EntityEnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonControllerPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonControllerStrafe;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEnderDragon;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class DragonUtilities {

    public static @NotNull Location getHeadLocation(@NotNull EnderDragon enderDragon) {
        EntityComplexPart part = ((CraftEnderDragon) enderDragon).getHandle().e;
        return new Location(enderDragon.getWorld(), part.u, part.v, part.w);
    }

    public static void setTarget(@NotNull EnderDragon enderDragon, @NotNull Entity entity) {
        EntityEnderDragon entityEnderDragon = ((CraftEnderDragon) enderDragon).getHandle();
        entityEnderDragon.getDragonControllerManager().setControllerPhase(DragonControllerPhase.b);
        EntityLiving entityLiving = entity instanceof Player ? ((CraftPlayer) entity).getHandle() : ((CraftArmorStand) entity).getHandle();
        if (entityEnderDragon.getDragonControllerManager().a() instanceof DragonControllerStrafe charge) {
            charge.a(entityLiving);
        }
    }

    public static void setMoving(EnderDragon enderDragon) {
        EntityEnderDragon entityEnderDragon = ((CraftEnderDragon) enderDragon).getHandle();
        entityEnderDragon.getDragonControllerManager().setControllerPhase(DragonControllerPhase.c);
    }
}
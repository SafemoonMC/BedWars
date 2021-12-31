package gg.mooncraft.minecraft.bedwars.game.match.systems;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.match.GameEvent;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;

import java.util.concurrent.atomic.AtomicInteger;

public final class GeneratorSystem {

    /*
    Fields
     */
    private final @NotNull GameMatch gameMatch;
    private final @NotNull AtomicInteger diamondTier;
    private final @NotNull AtomicInteger emeraldTier;

    /*
    Constructor
     */
    public GeneratorSystem(@NotNull GameMatch gameMatch) {
        this.gameMatch = gameMatch;
        this.diamondTier = new AtomicInteger(1);
        this.emeraldTier = new AtomicInteger(1);
    }

    /*
    Methods
     */
    public void updateDiamondTier() {
        if (this.diamondTier.get() == GameEvent.DIAMOND.getMaximumTier()) return;
        this.diamondTier.incrementAndGet();
    }

    public void updateEmeraldTier() {
        if (this.emeraldTier.get() == GameEvent.EMERALD.getMaximumTier()) return;
        this.emeraldTier.incrementAndGet();
    }

    public int getDiamondTier() {
        return diamondTier.get();
    }

    public int getNextDiamondTier() {
        return diamondTier.get() + 1;
    }

    public int getEmeraldTier() {
        return emeraldTier.get();
    }

    public int getNextEmeraldTier() {
        return emeraldTier.get() + 1;
    }
}
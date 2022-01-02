package gg.mooncraft.minecraft.bedwars.game.match.tasks;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class GameRunnable implements Runnable {

    /*
    Fields
     */
    private final @NotNull AtomicInteger ticking;

    /*
    Constructor
     */
    public GameRunnable() {
        this.ticking = new AtomicInteger(0);
    }

    /*
    Abstract Methods
     */
    public abstract void tick();

    /*
    Override Methods
     */
    @Override
    public void run() {
        updateTick();
        tick();
    }

    /*
    Methods
     */
    private void updateTick() {
        int newTick = ticking.incrementAndGet();
        if (newTick > 20) ticking.set(0);
    }

    public int getTick() {
        return ticking.get();
    }
}
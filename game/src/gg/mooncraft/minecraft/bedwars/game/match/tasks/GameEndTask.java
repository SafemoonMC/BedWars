package gg.mooncraft.minecraft.bedwars.game.match.tasks;

import lombok.AllArgsConstructor;
import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.scheduler.SchedulerTask;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameState;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@AllArgsConstructor
public final class GameEndTask implements Runnable {

    /*
    Constants
     */
    private static final int GAME_COUNTDOWN_END = 10;

    /*
    Fields
     */
    private final @NotNull GameMatch gameMatch;
    private AtomicInteger count;
    private SchedulerTask schedulerTask;

    /*
    Constructor
     */
    public GameEndTask(@NotNull GameMatch gameMatch) {
        this.gameMatch = gameMatch;
    }

    /*
    Methods
     */
    public void play() {
        if (this.schedulerTask != null) return;
        this.gameMatch.updateState(GameState.UNLOADING);
        this.count = new AtomicInteger(GAME_COUNTDOWN_END);
        this.schedulerTask = BedWarsPlugin.getInstance().getScheduler().asyncRepeating(this, 1, TimeUnit.SECONDS);
    }

    public int getTimeLeft() {
        return this.count.get();
    }

    /*
    Override Methods
     */
    @Override
    public void run() {
        if (getTimeLeft() == 0) {
            gameMatch.updateState(GameState.ENDING);

            this.schedulerTask.cancel();
            this.schedulerTask = null;
            return;
        }
        this.count.getAndDecrement();
    }
}
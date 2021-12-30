package gg.mooncraft.minecraft.bedwars.game.match;

import lombok.Getter;

import net.kyori.adventure.text.Component;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.GameConstants;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.GameStartTask;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public final class GameTicker implements Runnable {

    /*
    Fields
     */
    private final @NotNull GameMatch gameMatch;
    private final @NotNull AtomicInteger ticking;

    private final @NotNull GameStartTask gameStartTask;

    /*
    Constructor
     */
    public GameTicker(@NotNull GameMatch gameMatch) {
        this.gameMatch = gameMatch;
        this.ticking = new AtomicInteger();
        this.gameStartTask = new GameStartTask(gameMatch);

        BedWarsPlugin.getInstance().getScheduler().asyncRepeating(this, 250, TimeUnit.MILLISECONDS);
    }

    /*
    Override Methods
     */
    @Override
    public void run() {
        if (gameMatch.getGameState() == null) return;

        int tick = updateTick();
        switch (gameMatch.getGameState()) {
            case WAITING -> {
                if (tick % 5 == 0) {
                    gameMatch.getPlayerList().forEach(player -> player.sendActionBar(Component.text(GameConstants.MESSAGE_ACTIONBAR_WAITING + " " + tick)));
                }
            }
            case PLAYING -> {

            }
        }
    }

    /*
    Methods
     */
    private int updateTick() {
        int newTick = ticking.incrementAndGet();
        if (newTick > 20) ticking.set(0);

        return ticking.get();
    }
}
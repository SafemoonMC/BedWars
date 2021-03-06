package gg.mooncraft.minecraft.bedwars.game.match.tasks;

import lombok.AllArgsConstructor;
import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.scheduler.SchedulerTask;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameState;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.GameConstants;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.utilities.NumberUtilities;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@AllArgsConstructor
public final class GameStartTask implements Runnable {

    /*
    Constants
     */
    private static final int GAME_COUNTDOWN_START = 20;

    /*
    Fields
     */
    private final @NotNull GameMatch gameMatch;
    private AtomicInteger count;
    private SchedulerTask schedulerTask;

    /*
    Constructor
     */
    public GameStartTask(@NotNull GameMatch gameMatch) {
        this.gameMatch = gameMatch;
    }

    /*
    Methods
     */
    public void play() {
        if (this.schedulerTask != null) return;
        this.gameMatch.updateState(GameState.STARTING);
        this.count = new AtomicInteger(GAME_COUNTDOWN_START);
        this.schedulerTask = BedWarsPlugin.getInstance().getScheduler().asyncRepeating(this, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        if (this.schedulerTask == null) return;
        this.gameMatch.updateState(GameState.WAITING);
        this.schedulerTask.cancel();
        this.schedulerTask = null;
    }

    public boolean isRunning() {
        return this.schedulerTask != null;
    }

    public int getTimeLeft() {
        return this.count.get();
    }

    public @NotNull String getTimeUnit() {
        return getTimeLeft() == 1 ? "second" : "seconds";
    }

    public @NotNull ChatColor getTimeColor() {
        if (NumberUtilities.isBetween(getTimeLeft(), 11, 20)) {
            return ChatColor.YELLOW;
        } else if (NumberUtilities.isBetween(getTimeLeft(), 6, 10)) {
            return ChatColor.GOLD;
        } else {
            return ChatColor.RED;
        }
    }

    /*
    Override Methods
     */
    @Override
    public void run() {
        if (getTimeLeft() == 0) {
            gameMatch.updateState(GameState.PLAYING);

            this.schedulerTask.cancel();
            this.schedulerTask = null;
            return;
        }
        if (getTimeLeft() == GAME_COUNTDOWN_START || getTimeLeft() == 10 || getTimeLeft() <= 5) {
            gameMatch.getPlayerList().forEach(player -> {
                player.showTitle(Title.title(Component.text(getTimeColor().toString() + getTimeLeft()), Component.text(""), Title.Times.of(Duration.ofMillis(125), Duration.ofMillis(500), Duration.ofMillis(125))));
                player.sendMessage(GameConstants.MESSAGE_GLOBAL_STARTING
                        .replaceAll("%time-color%", getTimeColor().toString())
                        .replaceAll("%time-left%", String.valueOf(getTimeLeft()))
                        .replaceAll("%time-unit%", getTimeUnit())
                );
                player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 2, 2);
            });
        }

        // Update scoreboard
        gameMatch.getPlayerList().forEach(player -> {
            TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(player.getUniqueId());
            BedWarsPlugin.getInstance().getBoardManager().updateScoreboard(tabPlayer);
        });

        this.count.getAndDecrement();
    }
}
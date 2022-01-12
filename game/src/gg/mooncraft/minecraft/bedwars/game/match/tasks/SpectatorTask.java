package gg.mooncraft.minecraft.bedwars.game.match.tasks;

import lombok.AllArgsConstructor;
import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.scheduler.SchedulerTask;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.GameConstants;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.PlayerStatus;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@AllArgsConstructor
public class SpectatorTask implements Runnable {
    /*
    Constants
     */
    private static final int SPECTATOR_COUNTDOWN_END = 5;

    /*
    Fields
     */
    private final @NotNull GameMatch gameMatch;
    private final @NotNull GameMatchPlayer gameMatchPlayer;
    private AtomicInteger count;
    private SchedulerTask schedulerTask;

    /*
    Constructor
     */
    public SpectatorTask(@NotNull GameMatch gameMatch, @NotNull GameMatchPlayer gameMatchPlayer) {
        this.gameMatch = gameMatch;
        this.gameMatchPlayer = gameMatchPlayer;
    }

    /*
    Methods
     */
    public void play() {
        if (this.schedulerTask != null) return;
        this.count = new AtomicInteger(SPECTATOR_COUNTDOWN_END);
        this.schedulerTask = BedWarsPlugin.getInstance().getScheduler().asyncRepeating(this, 1, TimeUnit.SECONDS);
    }

    public int getTimeLeft() {
        return this.count.get();
    }

    public @NotNull String getTimeUnit() {
        return getTimeLeft() == 1 ? "second" : "seconds";
    }

    /*
    Override Methods
     */
    @Override
    public void run() {
        if (getTimeLeft() == 0) {
            gameMatchPlayer.updateStatus(PlayerStatus.ALIVE);

            gameMatchPlayer.getPlayer().ifPresent(player -> {
                player.sendMessage(GameConstants.MESSAGE_SPECTATOR_RESPAWN_CHAT);
                player.showTitle(Title.title(Component.text(GameConstants.MESSAGE_SPECTATOR_RESPAWN_TITLE), Component.empty(), Title.Times.of(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
            });

            this.schedulerTask.cancel();
            this.schedulerTask = null;
            return;
        }
        gameMatchPlayer.getPlayer().ifPresent(player -> {
            player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 2, 2);

            String message = GameConstants.MESSAGE_SPECTATOR_SUBTITLE
                    .replaceAll("%time%", String.valueOf(getTimeLeft()))
                    .replaceAll("%time-unit%", getTimeUnit());
            player.sendMessage(message);
            player.showTitle(Title.title(Component.text(GameConstants.MESSAGE_SPECTATOR_TITLE), Component.text(message), Title.Times.of(Duration.ofMillis(0), Duration.ofMillis(2000), Duration.ofMillis(0))));
        });

        this.count.getAndDecrement();
    }
}
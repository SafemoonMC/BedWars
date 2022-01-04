package gg.mooncraft.minecraft.bedwars.game.match;

import lombok.Getter;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;

import net.kyori.adventure.text.Component;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.GameConstants;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.GameRunnable;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.GameStartTask;

import java.util.concurrent.TimeUnit;

@Getter
public final class GameTicker extends GameRunnable {

    /*
    Fields
     */
    private final @NotNull GameMatch gameMatch;

    private final @NotNull GameStartTask gameStartTask;

    /*
    Constructor
     */
    public GameTicker(@NotNull GameMatch gameMatch) {
        super();
        this.gameMatch = gameMatch;
        this.gameStartTask = new GameStartTask(gameMatch);

        BedWarsPlugin.getInstance().getScheduler().asyncRepeating(this, 50, TimeUnit.MILLISECONDS);
    }

    /*
    Override Methods
     */
    @Override
    public void tick() {
        if (gameMatch.getGameState() == null) return;

        int tick = getTick();
        switch (gameMatch.getGameState()) {
            case WAITING -> {
                if (tick % 5 == 0) {
                    gameMatch.getPlayerList().forEach(player -> player.sendActionBar(Component.text(GameConstants.MESSAGE_ACTIONBAR_WAITING)));
                }
            }
            case PLAYING -> {
                gameMatch.getEventSystem().tick();
                gameMatch.getFurnaceSystem().tick();
                gameMatch.getGeneratorSystem().tick();
                if (tick % 2 == 0) {
                    gameMatch.getPlayerList().forEach(player -> {
                        TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(player.getUniqueId());
                        BedWarsPlugin.getInstance().getBoardManager().updateScoreboard(tabPlayer);
                    });
                }
            }
        }
    }
}
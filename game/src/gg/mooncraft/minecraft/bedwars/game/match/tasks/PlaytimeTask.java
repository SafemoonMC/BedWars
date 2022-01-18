package gg.mooncraft.minecraft.bedwars.game.match.tasks;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameState;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.GameConstants;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;

public class PlaytimeTask extends GameRunnable {

    /*
    Fields
     */
    private final @NotNull GameMatchPlayer matchPlayer;
    private int lifetime = 0;

    /*
    Constructor
     */
    public PlaytimeTask(@NotNull GameMatchPlayer matchPlayer) {
        super();
        this.matchPlayer = matchPlayer;
    }

    /*
    Override Methods
     */
    @Override
    public void tick() {
        // Check if the game is in the right state
        if (this.matchPlayer.getParent().getParent().getGameState() != GameState.PLAYING) return;

        int tick = getTick();

        // Increment countdown and send message at right interval
        if (tick == 20) {
            this.lifetime++;
            if (this.lifetime % 60 == 0) {
                this.matchPlayer.getPlayer().ifPresent(player -> {
                    BedWarsPlugin.getInstance().getUserFactory().getUser(player).ifPresent(bedWarsUser -> {
                        bedWarsUser.addExperience(GameConstants.EXPERIENCE_PLAYTIME);
                        player.sendMessage(GameConstants.MESSAGE_EXPERIENCE_RECEIVED
                                .replaceAll("%amount%", String.valueOf(GameConstants.EXPERIENCE_PLAYTIME))
                                .replaceAll("%action%", "Time Played")
                        );
                    });
                });
                this.matchPlayer.updateExperience(GameConstants.EXPERIENCE_PLAYTIME);
            }
        }
    }
}
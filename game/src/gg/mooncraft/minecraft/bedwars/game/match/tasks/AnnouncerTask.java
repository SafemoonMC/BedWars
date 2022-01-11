package gg.mooncraft.minecraft.bedwars.game.match.tasks;

import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameState;
import gg.mooncraft.minecraft.bedwars.game.GameConstants;
import gg.mooncraft.minecraft.bedwars.game.match.systems.AnnouncerSystem;

public class AnnouncerTask extends GameRunnable {

    /*
    Fields
     */
    private final @NotNull AnnouncerSystem announcerSystem;
    private int countdown = 0;

    /*
    Constructor
     */
    public AnnouncerTask(@NotNull AnnouncerSystem announcerSystem) {
        super();
        this.announcerSystem = announcerSystem;
    }

    /*
    Override Methods
     */
    @Override
    public void tick() {
        int tick = getTick();
        // Check if the game is in the right state
        if (this.announcerSystem.getGameMatch().getGameState() != GameState.PLAYING) {
            return;
        }

        // Increment countdown and send message at right interval
        if (tick == 20) {
            this.countdown++;
            if (this.countdown == GameConstants.ANNOUNCER_TIP_INTERVAL) {
                this.countdown = 0;

                int index = this.announcerSystem.getMessageCounter().getAndIncrement();
                if (index == GameConstants.ANNOUNCER_MESSAGES.size()) {
                    index = 0;
                    this.announcerSystem.getMessageCounter().set(0);
                }

                String message = GameConstants.ANNOUNCER_MESSAGES.get(index);
                this.announcerSystem.getGameMatch().getPlayerList().forEach(player -> {
                    player.sendMessage("", message, "");
                });
            }
        }
    }
}
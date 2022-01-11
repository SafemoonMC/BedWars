package gg.mooncraft.minecraft.bedwars.game.match.systems;

import lombok.Getter;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.AnnouncerTask;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class AnnouncerSystem implements TickSystem {

    /*
    Fields
     */
    private final @NotNull GameMatch gameMatch;
    private final @NotNull AnnouncerTask announcerTask;
    private final @NotNull AtomicInteger messageCounter;

    /*
    Constructor
     */
    public AnnouncerSystem(@NotNull GameMatch gameMatch) {
        this.gameMatch = gameMatch;
        this.announcerTask = new AnnouncerTask(this);
        this.messageCounter = new AtomicInteger();
    }

    /*
    Methods
     */
    @Override
    public void tick() {
        this.announcerTask.run();
    }
}

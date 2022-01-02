package gg.mooncraft.minecraft.bedwars.game.match.systems;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import gg.mooncraft.minecraft.bedwars.game.match.GameEvent;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.GameMatchEvent;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public final class EventSystem {

    /*
    Fields
     */
    private final @NotNull GameMatch gameMatch;
    private final @NotNull List<GameMatchEvent> eventList = new LinkedList<>();

    /*
    Constructor
     */
    public EventSystem(@NotNull GameMatch gameMatch) {
        this.gameMatch = gameMatch;
    }

    /*
    Methods
     */
    public void play() {
        if (!this.eventList.isEmpty()) return;

        // After 5 minutes DIAMOND generator will upgrade to Tier 2
        long currentTimeMillis = System.currentTimeMillis();
        this.eventList.add(new GameMatchEvent(0, GameEvent.DIAMOND, Instant.ofEpochMilli(currentTimeMillis).plus(5, ChronoUnit.MINUTES), () -> {
            gameMatch.getGeneratorSystem().updateDiamondTier();
        }));
        // After 11 (+6) minutes EMERALD generator will upgrade to Tier 2
        this.eventList.add(new GameMatchEvent(1, GameEvent.EMERALD, Instant.ofEpochMilli(currentTimeMillis).plus(11, ChronoUnit.MINUTES), () -> {
            gameMatch.getGeneratorSystem().updateEmeraldTier();
        }));
        // After 17 (+6) minutes DIAMOND generator will max out at Tier 3
        this.eventList.add(new GameMatchEvent(2, GameEvent.DIAMOND, Instant.ofEpochMilli(currentTimeMillis).plus(17, ChronoUnit.MINUTES), () -> {
            gameMatch.getGeneratorSystem().updateDiamondTier();
        }));
        // After 23 (+6) minutes EMERALD generator will max out at Tier 3
        this.eventList.add(new GameMatchEvent(3, GameEvent.EMERALD, Instant.ofEpochMilli(currentTimeMillis).plus(23, ChronoUnit.MINUTES), () -> {
            gameMatch.getGeneratorSystem().updateEmeraldTier();
        }));
        // After 29 (+6) minutes BED_DESTRUCTION occurs and every team loses their bed
        this.eventList.add(new GameMatchEvent(4, GameEvent.BED_DESTRUCTION, Instant.ofEpochMilli(currentTimeMillis).plus(29, ChronoUnit.MINUTES), () -> {

        }));
        // After 39 (10) minutes SUDDEN_DEATH begins and the dragons spawn in
        this.eventList.add(new GameMatchEvent(5, GameEvent.SUDDEN_DEATH, Instant.ofEpochMilli(currentTimeMillis).plus(39, ChronoUnit.MINUTES), () -> {

        }));
        // After 49 (10) minutes the game ends in a tie and nobody wins
        this.eventList.add(new GameMatchEvent(6, GameEvent.TIE, Instant.ofEpochMilli(currentTimeMillis).plus(49, ChronoUnit.MINUTES), () -> {

        }));
    }

    public @NotNull Optional<GameMatchEvent> getEvent(int id) {
        return this.eventList.stream().filter(gameMatchEvent -> gameMatchEvent.getId() == id).findFirst();
    }

    public @NotNull Optional<GameMatchEvent> getCurrentEvent() {
        return this.eventList.stream().filter(gameMatchEvent -> gameMatchEvent.getTimeLeft() != -1).min(Comparator.comparingLong(GameMatchEvent::getTimeLeft));
    }

    @UnmodifiableView
    public @NotNull List<GameMatchEvent> getEventList() {
        return Collections.unmodifiableList(this.eventList);
    }
}
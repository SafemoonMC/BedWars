package gg.mooncraft.minecraft.bedwars.game.match.tasks;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.match.GameEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@Getter
@AllArgsConstructor
public final class GameMatchEvent {

    /*
    Fields
     */
    private final int id;
    private final @NotNull GameEvent gameEvent;
    private final @NotNull Instant instant;

    /*
    Methods
     */
    public long getTimeLeft() {
        if (this.instant.isBefore(Instant.now())) return -1;
        return Duration.between(Instant.now(), this.instant).toSeconds();
    }

    /*
    Override Methods
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameMatchEvent that = (GameMatchEvent) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
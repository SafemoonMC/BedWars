package gg.mooncraft.minecraft.bedwars.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.database.entities.EntityChild;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@AllArgsConstructor
public final class GameStatistic implements EntityChild<GameStatisticContainer> {

    /*
    Fields
     */
    private final @NotNull GameStatisticContainer parent;
    private final @NotNull GameMode gameMode;
    private final @NotNull StatisticType type;
    private final @NotNull AtomicInteger amount;


    /*
    Override Methods
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameStatistic that = (GameStatistic) o;
        return getGameMode() == that.getGameMode() && getType() == that.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGameMode(), getType());
    }
}
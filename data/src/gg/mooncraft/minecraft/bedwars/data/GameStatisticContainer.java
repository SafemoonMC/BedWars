package gg.mooncraft.minecraft.bedwars.data;

import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.database.entities.EntityChild;
import me.eduardwayland.mooncraft.waylander.database.entities.EntityParent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Getter
public final class GameStatisticContainer implements EntityChild<BedWarsUser>, EntityParent<GameStatisticContainer> {

    /*
    Fields
     */
    private final @NotNull BedWarsUser parent;
    private final @NotNull List<GameStatistic> statisticList;

    /*
    Constructor
     */
    public GameStatisticContainer(@NotNull BedWarsUser parent) {
        this.parent = parent;
        this.statisticList = new ArrayList<>();
    }

    /*
    Override Methods
     */
    @Override
    public @NotNull CompletableFuture<GameStatisticContainer> withChildren() {
        return CompletableFuture.completedFuture(this);
    }
}
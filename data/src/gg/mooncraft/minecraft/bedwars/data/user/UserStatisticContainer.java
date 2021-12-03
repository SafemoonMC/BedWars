package gg.mooncraft.minecraft.bedwars.data.user;

import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.database.entities.EntityChild;
import me.eduardwayland.mooncraft.waylander.database.entities.EntityParent;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.user.stats.GameStatistic;
import gg.mooncraft.minecraft.bedwars.data.user.stats.OverallStatistic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Getter
public final class UserStatisticContainer implements EntityChild<BedWarsUser>, EntityParent<UserStatisticContainer> {

    /*
    Fields
     */
    private final @NotNull BedWarsUser parent;
    private final @NotNull List<GameStatistic> gameStatisticList;
    private final @NotNull List<OverallStatistic> overallStatisticList;

    /*
    Constructor
     */
    public UserStatisticContainer(@NotNull BedWarsUser parent) {
        this.parent = parent;
        this.gameStatisticList = new ArrayList<>();
        this.overallStatisticList = new ArrayList<>();
    }

    /*
    Override Methods
     */
    @Override
    public @NotNull CompletableFuture<UserStatisticContainer> withChildren() {
        return CompletableFuture.completedFuture(this);
    }
}
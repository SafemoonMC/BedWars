package gg.mooncraft.minecraft.bedwars.data;

import me.eduardwayland.mooncraft.waylander.database.entities.EntityParent;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.user.UserStatisticContainer;

import java.math.BigInteger;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class BedWarsUser implements EntityParent<BedWarsUser> {

    /*
    Fields
     */
    private final @NotNull UUID uniqueId;
    private @NotNull BigInteger coins;
    private @NotNull BigInteger level;
    private @NotNull BigInteger experience;
    private @NotNull UserStatisticContainer statisticContainer;

    /*
    Constructor
     */
    public BedWarsUser(@NotNull UUID uniqueId) {
        this(uniqueId, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
    }

    public BedWarsUser(@NotNull UUID uniqueId, @NotNull BigInteger coins, @NotNull BigInteger level, @NotNull BigInteger experience) {
        this.uniqueId = uniqueId;
        this.coins = coins;
        this.level = level;
        this.experience = experience;
        this.statisticContainer = new UserStatisticContainer(this);
    }

    /*
    Methods
     */
    public void addCoins(int coins) {
        if (coins <= 0) return;
        this.coins = this.coins.add(BigInteger.valueOf(coins));
    }

    public void delCoins(int coins) {
        if (coins <= 0) return;
        this.coins = this.coins.subtract(BigInteger.valueOf(coins)).max(BigInteger.ZERO);
    }

    public @NotNull BigInteger getCoins() {
        return this.coins;
    }

    /*
    Override Methods
     */
    @Override
    public @NotNull CompletableFuture<BedWarsUser> withChildren() {
        CompletableFuture<?> statisticFuture = statisticContainer.withChildren().thenAccept(userStatisticContainer -> this.statisticContainer = userStatisticContainer);
        return CompletableFuture.allOf(statisticFuture).thenApply(v -> this);
    }
}
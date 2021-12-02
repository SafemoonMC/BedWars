package gg.mooncraft.minecraft.bedwars.data;

import me.eduardwayland.mooncraft.waylander.database.entities.EntityParent;

import org.jetbrains.annotations.NotNull;

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

    /*
    Constructor
     */
    public BedWarsUser(@NotNull UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.coins = BigInteger.ZERO;
        this.level = BigInteger.ZERO;
        this.experience = BigInteger.ZERO;
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
        return CompletableFuture.completedFuture(this);
    }
}
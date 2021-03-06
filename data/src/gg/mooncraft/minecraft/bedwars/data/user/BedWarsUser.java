package gg.mooncraft.minecraft.bedwars.data.user;

import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.database.entities.EntityParent;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.Prestige;
import gg.mooncraft.minecraft.bedwars.data.UserDAO;
import gg.mooncraft.minecraft.bedwars.data.user.utility.ExpCalculator;
import joptsimple.internal.Strings;

import java.math.BigInteger;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
public final class BedWarsUser implements EntityParent<BedWarsUser> {

    /*
    Fields
     */
    private final @NotNull UUID uniqueId;
    private @NotNull BigInteger coins;
    private @NotNull BigInteger experience;
    private @NotNull UserStatisticContainer statisticContainer;

    /*
    Constructor
     */
    public BedWarsUser(@NotNull UUID uniqueId) {
        this(uniqueId, BigInteger.ZERO, BigInteger.ZERO);
    }

    public BedWarsUser(@NotNull UUID uniqueId, @NotNull BigInteger coins, @NotNull BigInteger experience) {
        this.uniqueId = uniqueId;
        this.coins = coins;
        this.experience = experience;
        this.statisticContainer = new UserStatisticContainer(this);
    }

    /*
    Methods
     */
    public void addCoins(int coins) {
        if (coins <= 0) return;
        this.coins = this.coins.add(BigInteger.valueOf(coins));
        UserDAO.update(this);
    }

    public void delCoins(int coins) {
        if (coins <= 0) return;
        this.coins = this.coins.subtract(BigInteger.valueOf(coins)).max(BigInteger.ZERO);
        UserDAO.update(this);
    }

    public void addExperience(long experience) {
        if (experience <= 0) return;
        this.experience = this.experience.add(BigInteger.valueOf(experience));
        UserDAO.update(this);
    }

    public int getLevel() {
        return ExpCalculator.getLevelForExperience(getExperience());
    }

    public int getNextLevel() {
        return getLevel() + 1;
    }

    public long getExperience() {
        return this.experience.longValue();
    }

    public long getExperienceCurrent() {
        return getExperience() - ExpCalculator.getExperienceForLevelPlain(getLevel());
    }

    public long getExperienceRequired() {
        return ExpCalculator.getExperienceForLevel(getNextLevel());
    }

    public float getExperiencePercentage() {
        long current = getExperienceCurrent();
        long required = getExperienceRequired();
        return (float) (100 * current) / required;
    }

    public @NotNull String getExperienceProgressBar() {
        char square = '???';
        int squares = 32;

        float percentage = getExperiencePercentage();
        int yesSquares = percentage < 1 ? 0 : (int) (squares * percentage / 100);
        int noSquares = 32 - yesSquares;
        return "??8[??b" + Strings.repeat(square, yesSquares) + "??7" + Strings.repeat(square, noSquares) + "??8]";
    }

    public @NotNull Prestige getPrestige() {
        return ExpCalculator.getPrestigeForExperience(getExperience());
    }

    public @NotNull Prestige getNextPrestige() {
        return ExpCalculator.getPrestigeForLevel(getNextLevel());
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
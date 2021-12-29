package gg.mooncraft.minecraft.bedwars.game.managers;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public final class MatchManager {

    /*
    Fields
     */
    private final @NotNull AtomicInteger indexCounter = new AtomicInteger();
    private final @NotNull List<GameMatch> matchList = new ArrayList<>();

    /*
    Methods
     */
    public CompletableFuture<GameMatch> findMatch(@NotNull GameMode gameMode, @NotNull List<UUID> playerList) {
        for (GameMatch gameMatch : getMatchList(gameMode)) {
            if (gameMatch.findTeamFor(playerList)) {
                return CompletableFuture.completedFuture(gameMatch);
            }
        }
        return createMatch(gameMode).thenApply(gameMatch -> {
            this.matchList.add(gameMatch);
            gameMatch.findTeamFor(playerList);
            return gameMatch;
        });
    }

    private CompletableFuture<GameMatch> createMatch(@NotNull GameMode gameMode) {
        // Get a random map for that game mode and check if exists
        Optional<BedWarsMap> mapOptional = BedWarsPlugin.getInstance().getMapManager().getRandomMap(gameMode);
        if (mapOptional.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        BedWarsMap bedWarsMap = mapOptional.get();

        int id = indexCounter.getAndIncrement();
        return BedWarsPlugin.getInstance().getSlimeManager().createTemporaryPairAsync(bedWarsMap.getIdentifier(), String.format("%s-%d", bedWarsMap.getIdentifier(), id))
                .thenApply(newSlimePair -> new GameMatch(id, bedWarsMap.getIdentifier(), gameMode, newSlimePair));
    }

    public @NotNull Optional<GameMatch> getGameMatch(@NotNull Player player) {
        return BedWarsPlugin.getInstance().getMatchManager().getMatchList()
                .stream()
                .filter(gameMatch -> gameMatch.getTeamList().stream().anyMatch(gameMatchTeam -> gameMatchTeam.hasPlayer(player.getUniqueId())))
                .findFirst();
    }

    @UnmodifiableView
    public @NotNull List<GameMatch> getMatchList() {
        return Collections.unmodifiableList(this.matchList);
    }

    @UnmodifiableView
    public @NotNull List<GameMatch> getMatchList(@NotNull GameMode gameMode) {
        return getMatchList().stream().filter(gameMatch -> gameMatch.getGameMode() == gameMode).collect(Collectors.toList());
    }
}
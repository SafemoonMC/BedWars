package gg.mooncraft.minecraft.bedwars.game.managers;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public final class MatchManager {

    /*
    Fields
     */
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
            if (gameMatch == null) {
                BedWarsPlugin.getInstance().getLogger().warning("No match can be created for " + gameMode.name() + " gamemode.");
                return null;
            }
            gameMatch.findTeamFor(playerList);
            this.matchList.add(gameMatch);
            return gameMatch;
        }).exceptionally(t -> {
            BedWarsPlugin.getInstance().getLogger().warning("No match can be created for " + gameMode.name() + " gamemode. Exception: " + t.getMessage());
            return null;
        });
    }

    private CompletableFuture<GameMatch> createMatch(@NotNull GameMode gameMode) {
        // Get a random map for that game mode and check if exists
        Optional<BedWarsMap> mapOptional = BedWarsPlugin.getInstance().getMapManager().getRandomMap(gameMode);
        if (mapOptional.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        BedWarsMap bedWarsMap = mapOptional.get();

        UUID uniqueId = UUID.randomUUID();
        return BedWarsPlugin.getInstance().getSlimeManager().createTemporaryPairAsync(bedWarsMap.getIdentifier(), String.format("%s-%s", bedWarsMap.getIdentifier(), uniqueId))
                .thenApply(newSlimePair -> new GameMatch(uniqueId, bedWarsMap.getIdentifier(), gameMode, newSlimePair));
    }

    public void destroyMatch(@NotNull GameMatch gameMatch) {
        this.matchList.remove(gameMatch);
    }

    public @NotNull Optional<GameMatch> getGameMatch(@NotNull World world) {
        return this.matchList
                .stream()
                .filter(gameMatch -> gameMatch.getDimension().getName().equals(world.getName()))
                .findFirst();
    }

    public @NotNull Optional<GameMatch> getGameMatch(@NotNull Player player) {
        return this.matchList
                .stream()
                .filter(gameMatch -> gameMatch.getTeamOf(player).isPresent())
                .findFirst();
    }

    @UnmodifiableView
    public @NotNull List<GameMatch> getMatchList() {
        return this.matchList.stream().sorted(Comparator.comparingInt(o -> o.getPlayersCapacity() - o.getPlayersCount())).toList();
    }

    @UnmodifiableView
    public @NotNull List<GameMatch> getMatchList(@NotNull GameMode gameMode) {
        return getMatchList().stream().filter(gameMatch -> gameMatch.getGameMode() == gameMode).collect(Collectors.toList());
    }
}
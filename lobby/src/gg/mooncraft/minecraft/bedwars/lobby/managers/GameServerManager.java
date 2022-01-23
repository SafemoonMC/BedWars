package gg.mooncraft.minecraft.bedwars.lobby.managers;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.common.messages.GameServerMessage;
import gg.mooncraft.minecraft.bedwars.data.GameMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class GameServerManager {

    /*
    Fields
     */
    private final @NotNull List<GameServerMessage.GameServer> gameServerList = new ArrayList<>();

    /*
    Methods
     */
    public void updateGameServer(@NotNull GameServerMessage.GameServer gameServer) {
        this.gameServerList.remove(gameServer);
        this.gameServerList.add(gameServer);
    }

    public @NotNull Optional<GameServerMessage.GameServer> getGameServer(@NotNull GameMode gameMode, int players) {
        Optional<GameServerMessage.GameServer> freeGameServer = this.gameServerList.stream().filter(gameServer -> gameServer.isAvailableFor(gameMode, players)).findFirst();
        if (freeGameServer.isPresent()) return freeGameServer;

        return this.gameServerList.stream()
                .min((o1, o2) -> {
                    int stServerWeight = o1.getMatchList().stream().mapToInt(gameServerMatch -> gameServerMatch.getGameMode().getWeight()).sum();
                    int ndServerWeight = o2.getMatchList().stream().mapToInt(gameServerMatch -> gameServerMatch.getGameMode().getWeight()).sum();
                    return Integer.compare(stServerWeight, ndServerWeight);
                });
    }

    public @NotNull Optional<GameServerMessage.GameServer> getGameServer(@NotNull String serverName) {
        return this.gameServerList.stream().filter(gameServer -> gameServer.getServerName().equalsIgnoreCase(serverName)).findFirst();
    }

    public int getOnlinePlayers(@NotNull GameMode gameMode) {
        return this.gameServerList.stream()
                .flatMap(gameServer -> gameServer.getMatchList().stream())
                .filter(gameServerMatch -> gameServerMatch.getGameMode() == gameMode)
                .toList()
                .stream()
                .mapToInt(GameServerMessage.GameServerMatch::getPlayers)
                .sum();
    }

    public boolean areGameServersRunning() {
        return this.gameServerList.stream().anyMatch(gameServer -> gameServer.getServerStatus() == GameServerMessage.ServerStatus.ENABLED);
    }
}
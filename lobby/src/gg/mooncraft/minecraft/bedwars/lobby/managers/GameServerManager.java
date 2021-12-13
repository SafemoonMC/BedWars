package gg.mooncraft.minecraft.bedwars.lobby.managers;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.common.messages.GameServerMessage;
import gg.mooncraft.minecraft.bedwars.data.GameMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public @NotNull Optional<GameServerMessage.GameServer> getGameServer(@NotNull String serverName) {
        return this.gameServerList.stream().filter(gameServer -> gameServer.getServerName().equalsIgnoreCase(serverName)).findFirst();
    }

    public int getOnlinePlayers(@NotNull GameMode gameMode) {
        return this.gameServerList.stream()
                .flatMap(gameServer -> gameServer.getMatchList().stream())
                .filter(gameServerMatch -> gameServerMatch.getGameMode() == gameMode)
                .collect(Collectors.toList())
                .stream()
                .mapToInt(GameServerMessage.GameServerMatch::getOnlinePlayers)
                .sum();
    }

}
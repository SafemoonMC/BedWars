package gg.mooncraft.minecraft.bedwars.game.managers;

import gg.mooncraft.minecraft.bedwars.common.messages.GameServerMessage;
import gg.mooncraft.minecraft.bedwars.common.messaging.RedisChannel;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;

import java.util.ArrayList;
import java.util.List;

public final class GameServerManager {

    /*
    Methods
     */
    public void sendGameServerMessage() {
        List<GameServerMessage.GameServerMatch> matchList = new ArrayList<>();
        for (GameMatch gameMatch : BedWarsPlugin.getInstance().getMatchManager().getMatchList()) {
            GameServerMessage.GameServerMatch gameServerMatch = new GameServerMessage.GameServerMatch(gameMatch.getUniqueId(), gameMatch.getGameMode(), gameMatch.getGameState(), gameMatch.getPlayersCount());
            matchList.add(gameServerMatch);
        }
        GameServerMessage gameServerMessage = new GameServerMessage(new GameServerMessage.GameServer(BedWarsPlugin.getInstance().getServerName(), matchList));
        BedWarsPlugin.getInstance().getMessenger().sendMessage(RedisChannel.LOBBY, gameServerMessage);
    }
}
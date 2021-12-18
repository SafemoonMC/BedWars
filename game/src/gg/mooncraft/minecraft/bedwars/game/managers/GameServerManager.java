package gg.mooncraft.minecraft.bedwars.game.managers;

import gg.mooncraft.minecraft.bedwars.common.messages.GameServerMessage;
import gg.mooncraft.minecraft.bedwars.common.messaging.RedisChannel;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;

import java.util.Collections;

public final class GameServerManager {

    /*
    Methods
     */
    public void sendGameServerMessage() {
        GameServerMessage gameServerMessage = new GameServerMessage(new GameServerMessage.GameServer(BedWarsPlugin.getInstance().getServerName(), Collections.emptyList()));
        BedWarsPlugin.getInstance().getMessenger().sendMessage(RedisChannel.LOBBY, gameServerMessage);
    }
}
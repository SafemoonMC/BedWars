package gg.mooncraft.minecraft.bedwars.common.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.common.messaging.RedisMessenger;
import gg.mooncraft.minecraft.bedwars.common.messaging.message.AbstractMessage;
import gg.mooncraft.minecraft.bedwars.data.GameMode;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public final class GameServerMessage extends AbstractMessage {

    /*
    Constants
     */
    public static final String TYPE = "gameserver";

    /*
    Fields
     */
    private final @NotNull GameServer gameServer;

    /*
    Constructor
     */
    public GameServerMessage(@NotNull GameServer gameServer) {
        super(Instant.now(), UUID.randomUUID());
        this.gameServer = gameServer;
    }

    public GameServerMessage(long timestamp, @NotNull UUID uniqueId, @NotNull JsonElement jsonElement) {
        super(timestamp, uniqueId, jsonElement);
        this.gameServer = GameServerSerializer.deserialize(jsonElement);
    }

    /*
    Override Methods
     */
    @Override
    public @NotNull String asJsonString() {
        return RedisMessenger.encodeMessageAsJson(getTimestamp().getEpochSecond(), TYPE, getUniqueId(), GameServerSerializer.serialize(this.gameServer));
    }

    /*
    Inner
     */
    @Getter
    @AllArgsConstructor
    public static class GameServer {
        private final @NotNull String serverName;
        private final @NotNull List<GameServerMatch> matchList;
    }

    @Getter
    @AllArgsConstructor
    public static class GameServerMatch {
        private final @NotNull GameMode gameMode;
        private final int matches;
        private final int onlinePlayers;
    }
}
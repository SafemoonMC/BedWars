package gg.mooncraft.minecraft.bedwars.common.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.common.messaging.RedisMessenger;
import gg.mooncraft.minecraft.bedwars.common.messaging.message.AbstractMessage;
import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.GameState;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
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
        private final @NotNull ServerStatus serverStatus;
        private final @NotNull List<GameServerMatch> matchList;

        /*
        Methods
         */
        public boolean isAvailableFor(@NotNull GameMode gameMode, int players) {
            if (serverStatus == ServerStatus.DISABLED) {
                return false;
            }
            return this.matchList.stream()
                    .filter(gameServerMatch -> gameServerMatch.getGameMode() == gameMode)
                    .anyMatch(gameServerMatch -> (gameServerMatch.getGameMode().getWeight() - gameServerMatch.getPlayers()) >= players);
        }

        /*
        Override Methods
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GameServer that = (GameServer) o;
            return getServerName().equals(that.getServerName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getServerName());
        }
    }

    @Getter
    @AllArgsConstructor
    public static class GameServerMatch {
        private final @NotNull UUID gameUniqueId;
        private final @NotNull GameMode gameMode;
        private final @NotNull GameState gameState;
        private final int players;

        /*
        Override Methods
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GameServerMatch that = (GameServerMatch) o;
            return getGameUniqueId().equals(that.getGameUniqueId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getGameUniqueId());
        }
    }

    public enum ServerStatus {
        ENABLED,
        DISABLED
    }
}
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
import java.util.Objects;
import java.util.UUID;

@Getter
public final class GameRequestMessage extends AbstractMessage {

    /*
    Constants
     */
    public static final String TYPE = "gamerequest";

    /*
    Fields
     */
    private final @NotNull GameRequest gameRequest;

    /*
    Constructor
     */
    public GameRequestMessage(@NotNull GameRequest gameRequest) {
        super(Instant.now(), UUID.randomUUID());
        this.gameRequest = gameRequest;
    }

    public GameRequestMessage(long timestamp, @NotNull UUID uniqueId, @NotNull JsonElement jsonElement) {
        super(timestamp, uniqueId, jsonElement);
        this.gameRequest = GameRequestSerializer.deserialize(jsonElement);
    }

    /*
    Override Methods
     */
    @Override
    public @NotNull String asJsonString() {
        return RedisMessenger.encodeMessageAsJson(getTimestamp().getEpochSecond(), TYPE, getUniqueId(), GameRequestSerializer.serialize(this.gameRequest));
    }

    /*
    Inner
     */
    @Getter
    @AllArgsConstructor
    public static class GameRequest {
        private final @NotNull String serverName;
        private final @NotNull GameMode gameMode;
        private final @NotNull List<UUID> playerList;

        /*
        Override Methods
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GameRequest that = (GameRequest) o;
            return getServerName().equals(that.getServerName()) && getGameMode() == that.getGameMode() && getPlayerList().equals(that.getPlayerList());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getServerName(), getGameMode(), getPlayerList());
        }
    }
}
package gg.mooncraft.minecraft.bedwars.common.messages;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.common.utilities.gson.JsonArrayWrapper;
import gg.mooncraft.minecraft.bedwars.common.utilities.gson.JsonObjectWrapper;
import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GameServerSerializer {

    public static @NotNull JsonObject serialize(@NotNull GameServerMessage.GameServer gameServer) {
        return new JsonObjectWrapper()
                .add("server-name", new JsonPrimitive(gameServer.getServerName()))
                .add("match-list", new JsonArrayWrapper()
                        .consume(jsonArrayWrapper -> {
                            for (GameServerMessage.GameServerMatch gameServerMatch : gameServer.getMatchList()) {
                                jsonArrayWrapper.add(new JsonObjectWrapper()
                                        .add("game-unique-id", new JsonPrimitive(gameServerMatch.getGameUniqueId().toString()))
                                        .add("game-mode", new JsonPrimitive(gameServerMatch.getGameMode().name()))
                                        .add("game-state", new JsonPrimitive(gameServerMatch.getGameState().name()))
                                        .add("players", new JsonPrimitive(gameServerMatch.getPlayers()))
                                        .toJson()
                                );
                            }
                        })
                ).toJson();
    }

    public static @NotNull GameServerMessage.GameServer deserialize(@NotNull JsonElement jsonElement) {
        Preconditions.checkArgument(jsonElement.isJsonObject());
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String serverName = jsonObject.get("server-name").getAsString();
        List<GameServerMessage.GameServerMatch> matchList = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray("match-list");
        if (jsonArray.size() != 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement jsonArrayElement = jsonArray.get(i);
                Preconditions.checkArgument(jsonArrayElement.isJsonObject());
                JsonObject jsonArrayObject = jsonArrayElement.getAsJsonObject();
                GameServerMessage.GameServerMatch gameServer = new GameServerMessage.GameServerMatch(UUID.fromString(jsonArrayObject.get("game-unique-id").getAsString()), GameMode.valueOf(jsonArrayObject.get("game-mode").getAsString()), GameState.valueOf(jsonArrayObject.get("game-state").getAsString()), jsonArrayObject.get("players").getAsInt());
                matchList.add(gameServer);
            }
        }

        return new GameServerMessage.GameServer(serverName, matchList);
    }
}
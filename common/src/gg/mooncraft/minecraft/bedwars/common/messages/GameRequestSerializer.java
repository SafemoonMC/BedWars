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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GameRequestSerializer {

    public static @NotNull JsonObject serialize(@NotNull GameRequestMessage.GameRequest gameRequest) {
        return new JsonObjectWrapper()
                .add("server-name", new JsonPrimitive(gameRequest.getServerName()))
                .add("game-mode", new JsonPrimitive(gameRequest.getGameMode().name()))
                .add("player-list", new JsonArrayWrapper()
                        .consume(jsonArrayWrapper -> {
                            for (UUID uniqueId : gameRequest.getPlayerList()) {
                                jsonArrayWrapper.add(new JsonPrimitive(uniqueId.toString()));
                            }
                        })
                ).toJson();
    }

    public static @NotNull GameRequestMessage.GameRequest deserialize(@NotNull JsonElement jsonElement) {
        Preconditions.checkArgument(jsonElement.isJsonObject());
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String serverName = jsonObject.get("server-name").getAsString();
        GameMode gameMode = GameMode.valueOf(jsonObject.get("game-mode").getAsString());
        List<UUID> playerList = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray("player-list");
        if (jsonArray.size() != 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement jsonArrayElement = jsonArray.get(i);
                Preconditions.checkArgument(jsonArrayElement.isJsonPrimitive());
                UUID uniqueId = UUID.fromString(jsonArrayElement.getAsJsonPrimitive().getAsString());
                playerList.add(uniqueId);
            }
        }

        return new GameRequestMessage.GameRequest(serverName, gameMode, playerList);
    }
}
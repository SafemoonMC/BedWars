package gg.mooncraft.minecraft.bedwars.game.messaging;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gg.mooncraft.minecraft.bedwars.common.messages.GameRequestMessage;
import gg.mooncraft.minecraft.bedwars.common.messaging.consumer.IncomingMessageConsumer;
import gg.mooncraft.minecraft.bedwars.common.messaging.message.Message;
import gg.mooncraft.minecraft.bedwars.common.utilities.CaffeineList;
import gg.mooncraft.minecraft.bedwars.common.utilities.TriFunction;
import gg.mooncraft.minecraft.bedwars.common.utilities.gson.GsonProvider;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.MessageType;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class GameRedisMessenger implements IncomingMessageConsumer {


    /*
    Fields
     */
    private final @NotNull BedWarsPlugin plugin;
    private final @NotNull CaffeineList<UUID> receivedMessageList;

    /*
    Constructor
     */
    public GameRedisMessenger(@NotNull BedWarsPlugin plugin) {
        this.plugin = plugin;
        this.receivedMessageList = new CaffeineList<>(10, TimeUnit.SECONDS);
    }

    /*
    Override Methods
     */
    @Override
    public boolean consumeIncomingMessageAsString(@NotNull String jsonMessage) {
        JsonObject jsonObject = GsonProvider.normal().fromJson(jsonMessage, JsonObject.class).getAsJsonObject();

        // Get the timestamp of the message if any
        JsonElement timestampElement = jsonObject.get("timestamp");
        if (timestampElement == null) {
            throw new IllegalStateException("The incoming message has no timestamp argument: " + jsonMessage);
        }
        long timestamp = timestampElement.getAsLong();

        // Get the unique-id of the message if any
        JsonElement uniqueIdElement = jsonObject.get("unique-id");
        if (uniqueIdElement == null) {
            throw new IllegalStateException("The incoming message has no unique-id argument: " + jsonMessage);
        }
        UUID uniqueId = UUID.fromString(uniqueIdElement.getAsString());

        // Check if a message with that unique-id has already been received, return if so
        if (!this.receivedMessageList.add(uniqueId)) return false;

        // Get the type of the message if any
        JsonElement typeElement = jsonObject.get("type");
        if (typeElement == null) {
            throw new IllegalStateException("The incoming message has no type argument: " + jsonMessage);
        }
        String type = typeElement.getAsString();

        // Check if the type can be recognised and return false if not
        @Nullable TriFunction<Long, UUID, JsonElement, Message> messageSupplier = MessageType.getSupplierFor(type);
        if (messageSupplier == null) return false;

        // Get the final message if any and return false if not
        JsonElement contentElement = jsonObject.get("content");
        if (contentElement == null) return false;

        // Process the incoming message
        Message message = messageSupplier.apply(timestamp, uniqueId, contentElement);
        processIncomingMessage(message);
        return false;
    }

    /*
    Methods
     */
    private void processIncomingMessage(@NotNull Message message) {
        BedWarsPlugin.getInstance().getLogger().info("[Redis] Received a new message with timestamp " + message.getTimestamp().getEpochSecond() + "s and UUID " + message.getUniqueId() + ".");
        if (message instanceof GameRequestMessage gameRequestMessage) {
            BedWarsPlugin.getInstance().getGameRequestManager().update(gameRequestMessage.getGameRequest());
        }
    }
}
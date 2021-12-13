package gg.mooncraft.minecraft.bedwars.common.messaging.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public abstract class AbstractMessage implements Message, OutgoingMessage {

    /*
    Fields
     */
    private final @NotNull Instant timestamp;
    private final @NotNull UUID uniqueId;

    /*
    Constructor
     */
    public AbstractMessage(long timestamp, @NotNull UUID uniqueId, @NotNull JsonElement jsonElement) {
        this.timestamp = Instant.ofEpochSecond(timestamp);
        this.uniqueId = uniqueId;
    }
}
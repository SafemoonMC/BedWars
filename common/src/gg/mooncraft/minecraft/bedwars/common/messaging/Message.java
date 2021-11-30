package gg.mooncraft.minecraft.bedwars.common.messaging;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents a message sent received via {@link RedisMessenger}.
 */
@ApiStatus.NonExtendable
public interface Message {

    /**
     * Gets the unique id associated with this message.
     */
    @NotNull UUID getUniqueId();
}
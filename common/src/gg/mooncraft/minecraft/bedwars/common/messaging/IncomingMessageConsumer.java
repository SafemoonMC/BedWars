package gg.mooncraft.minecraft.bedwars.common.messaging;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A simply functional interface called by {@link RedisSubscription} with the received message
 */
@ApiStatus.NonExtendable
@FunctionalInterface
public interface IncomingMessageConsumer {

    /**
     * @param jsonMessage the json message to consume
     * @return true if the message has been consumed else returns false
     */
    boolean consumeIncomingMessageAsString(@NotNull String jsonMessage);
}
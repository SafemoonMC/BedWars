package gg.mooncraft.minecraft.bedwars.common.messaging;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.NonExtendable
public interface IncomingMessageConsumer {

    boolean consumeIncomingMessage(@NotNull Message message);

    boolean consumeIncomingMessageAsString(@NotNull String jsonMessage);
}
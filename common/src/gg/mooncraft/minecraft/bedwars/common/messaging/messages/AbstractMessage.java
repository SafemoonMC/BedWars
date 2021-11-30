package gg.mooncraft.minecraft.bedwars.common.messaging.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@AllArgsConstructor
public abstract class AbstractMessage implements Message, OutgoingMessage {

    /*
    Fields
     */
    private final @NotNull UUID uniqueId;
}
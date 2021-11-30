package gg.mooncraft.minecraft.bedwars.common.messaging;

import org.jetbrains.annotations.NotNull;

public interface OutgoingMessage extends Message {

    @NotNull String asJsonString();
}
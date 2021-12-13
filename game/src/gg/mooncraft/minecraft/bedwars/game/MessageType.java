package gg.mooncraft.minecraft.bedwars.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gg.mooncraft.minecraft.bedwars.common.messaging.message.Message;
import gg.mooncraft.minecraft.bedwars.common.utilities.TriFunction;

import java.util.UUID;

@Getter
@AllArgsConstructor
public enum MessageType {

    TODO((timestamp, uuid, jsonElement) -> null);

    private final @NotNull TriFunction<Long, UUID, JsonElement, Message> supplier;

    /*
    Static Methods
     */
    public static @Nullable TriFunction<Long, UUID, JsonElement, Message> getSupplierFor(@NotNull String type) {
        try {
            return MessageType.valueOf(type.toUpperCase()).supplier;
        } catch (Exception ignored) {
            return null;
        }
    }
}
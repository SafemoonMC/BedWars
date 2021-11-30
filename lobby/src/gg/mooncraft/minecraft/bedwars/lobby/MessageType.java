package gg.mooncraft.minecraft.bedwars.lobby;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gg.mooncraft.minecraft.bedwars.common.messaging.message.Message;

import java.util.UUID;
import java.util.function.BiFunction;

@Getter
@AllArgsConstructor
public enum MessageType {

    TODO((uuid, jsonElement) -> null);

    private final @NotNull BiFunction<UUID, JsonElement, Message> supplier;

    /*
    Static Methods
     */
    public static @Nullable BiFunction<UUID, JsonElement, Message> getSupplierFor(@NotNull String type) {
        try {
            return MessageType.valueOf(type.toUpperCase()).supplier;
        } catch (Exception ignored) {
            return null;
        }
    }
}
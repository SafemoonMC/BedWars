package gg.mooncraft.minecraft.bedwars.common.messaging;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface Messenger {

    @NotNull CompletableFuture<Boolean> sendMessage(@NotNull RedisChannel redisChannel, @NotNull OutgoingMessage outgoingMessage);

    @NotNull CompletableFuture<Boolean> addKeyValue(@NotNull String key, @NotNull String value);

    @NotNull CompletableFuture<Boolean> delKeyValue(@NotNull String key);

    @NotNull CompletableFuture<String> getKeyValue(@NotNull String key);
}
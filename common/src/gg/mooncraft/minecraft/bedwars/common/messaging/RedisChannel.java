package gg.mooncraft.minecraft.bedwars.common.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public enum RedisChannel {

    LOBBY("bedwars:lobby"), GAME("bedwars:game");

    private final @NotNull String channel;
}
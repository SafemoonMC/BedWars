package gg.mooncraft.minecraft.bedwars.game.match.options;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public final class OptionEntry<K, V> {

    /*
    Fields
     */
    private final @NotNull K key;
    private final @NotNull V value;

}
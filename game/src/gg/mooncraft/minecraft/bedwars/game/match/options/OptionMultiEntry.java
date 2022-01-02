package gg.mooncraft.minecraft.bedwars.game.match.options;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public final class OptionMultiEntry<K, V> {

    /*
    Fields
     */
    public final @NotNull K key;
    public final @NotNull V[] values;

    /*
    Methods
     */
    public @NotNull V getValue(int index) {
        if (index < 0 || index >= values.length) {
            throw new IllegalArgumentException("The index must be between 0 and " + values.length + "! Value: " + index + ".");
        }
        return values[index];
    }

    public int size() {
        return values.length;
    }
}
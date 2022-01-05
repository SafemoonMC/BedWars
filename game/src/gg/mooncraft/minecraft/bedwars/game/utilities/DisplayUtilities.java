package gg.mooncraft.minecraft.bedwars.game.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import net.md_5.bungee.api.ChatColor;

import org.apache.commons.lang.WordUtils;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class DisplayUtilities {

    public static @NotNull String getLiteral(int number) {
        if (number == 1) return "I";
        else if (number == 2) return "II";
        else if (number == 3) return "III";
        else return "-";
    }

    public static @NotNull String getDisplay(long seconds) {
        int secs = (int) (seconds % 60);
        int mins = (int) (seconds / 60);
        return String.format("%01d:%02d", mins, secs);
    }

    public static @NotNull String getDisplay(@NotNull ItemStack itemStack) {
        if (itemStack.getType().isAir()) return "hand";
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            return itemStack.getItemMeta().displayName().toString();
        } else {
            return WordUtils.capitalizeFully(itemStack.getType().name());
        }
    }

    public static @NotNull String getColored(@NotNull String content) {
        return ChatColor.translateAlternateColorCodes('&', content);
    }
}
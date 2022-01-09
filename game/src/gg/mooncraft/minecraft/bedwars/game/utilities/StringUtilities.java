package gg.mooncraft.minecraft.bedwars.game.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtilities {

    public static String getColoredString(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> getColoredStringList(List<String> textLineList) {
        return textLineList.stream().map(StringUtilities::getColoredString).collect(Collectors.toList());
    }

}
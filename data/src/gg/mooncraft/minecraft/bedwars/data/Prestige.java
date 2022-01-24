package gg.mooncraft.minecraft.bedwars.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public final class Prestige {

    /*
    Fields
     */
    private final @NotNull String display;
    private final @NotNull ChatColor[] colors;

    /*
    Methods
     */
    public @NotNull String applyColors(int levels) {
        String levelText = String.valueOf(levels);

        StringBuilder finalText = new StringBuilder();
        if (levelText.length() == colors.length) {
            for (int i = 0; i < levelText.length(); i++) {
                char c = levelText.charAt(i);
                finalText.append(colors[i]).append(c);
            }
            return finalText.toString();
        }
        if (levelText.length() > colors.length) {
            int first = 0;
            int last = levelText.length() - 1;

            finalText.append(colors[first]).append(levelText.charAt(first));
            for (int i = first + 1; i < last; i++) {
                finalText.append(colors[i]).append(levelText.charAt(i));
            }
            finalText.append(colors[last]).append(levelText.charAt(last));
            return finalText.toString();
        }
        return levelText;
    }
}
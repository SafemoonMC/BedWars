package gg.mooncraft.minecraft.bedwars.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import net.md_5.bungee.api.ChatColor;

import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public enum GameTeam {

    GREEN('G', "Green", ChatColor.GREEN),
    AQUA('A', "Aqua", ChatColor.AQUA),
    RED('R', "Red", ChatColor.RED),
    GRAY('S', "Gray", ChatColor.DARK_GRAY),
    BLUE('B', "Blue", ChatColor.BLUE),
    PINK('P', "Pink", ChatColor.LIGHT_PURPLE),
    YELLOW('Y', "Yellow", ChatColor.YELLOW),
    WHITE('W', "White", ChatColor.WHITE);

    /*
    Fields
     */
    private final char letter;
    private final @NotNull String display;
    private final @NotNull ChatColor chatColor;
}
package gg.mooncraft.minecraft.bedwars.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import net.md_5.bungee.api.ChatColor;

import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public enum GameTeam {

    //    BLACK(ChatColor.BLACK),
    //    DARK_BLUE(ChatColor.DARK_BLUE),
    DARK_GREEN('G', "Green", ChatColor.DARK_GREEN),
    DARK_AQUA('A', "Aqua", ChatColor.DARK_AQUA),
    DARK_RED('R', "Red", ChatColor.DARK_RED),
    //    DARK_PURPLE(ChatColor.DARK_PURPLE),
    //    GOLD(ChatColor.GOLD),
    //    GRAY(ChatColor.GRAY),
    DARK_GRAY('S', "Gray", ChatColor.DARK_GRAY),
    BLUE('B', "Blue", ChatColor.BLUE),
    //    GREEN(ChatColor.GREEN),
    //    AQUA(ChatColor.AQUA),
    //    RED(ChatColor.RED),
    LIGHT_PURPLE('P', "Pink", ChatColor.LIGHT_PURPLE),
    YELLOW('Y', "Yellow", ChatColor.YELLOW),
    WHITE('W', "White", ChatColor.WHITE);

    /*
    Fields
     */
    private final char letter;
    private final @NotNull String display;
    private final @NotNull ChatColor chatColor;
}
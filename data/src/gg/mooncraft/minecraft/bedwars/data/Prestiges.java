package gg.mooncraft.minecraft.bedwars.data;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public final class Prestiges {

    /*
    Fields
     */
    private static final Prestige[] PRESTIGES = {
            new Prestige("STONE", new ChatColor[]{ChatColor.GRAY}),
            new Prestige("IRON", new ChatColor[]{ChatColor.WHITE}),
            new Prestige("GOLD", new ChatColor[]{ChatColor.GOLD}),
            new Prestige("DIAMOND", new ChatColor[]{ChatColor.AQUA}),
            new Prestige("EMERALD", new ChatColor[]{ChatColor.DARK_GREEN}),
            new Prestige("SAPPHIRE", new ChatColor[]{ChatColor.DARK_AQUA}),
            new Prestige("BLITZ", new ChatColor[]{ChatColor.DARK_RED}),
            new Prestige("CRYSTAL", new ChatColor[]{ChatColor.LIGHT_PURPLE}),
            new Prestige("WARRIOR", new ChatColor[]{ChatColor.DARK_BLUE}),
            new Prestige("ROYAL WARRIOR", new ChatColor[]{ChatColor.DARK_PURPLE}),
            new Prestige("RAINBOW", ChatColor.values()),
            new Prestige("IRON PRIME", new ChatColor[]{ChatColor.WHITE}),
            new Prestige("GOLD PRIME", new ChatColor[]{ChatColor.YELLOW}),
            new Prestige("DIAMOND PRIME", new ChatColor[]{ChatColor.AQUA}),
            new Prestige("EMERALD PRIME", new ChatColor[]{ChatColor.DARK_GREEN}),
            new Prestige("SAPPHIRE PRIME", new ChatColor[]{ChatColor.DARK_AQUA}),
            new Prestige("BLITZ PRIME", new ChatColor[]{ChatColor.DARK_RED}),
            new Prestige("CRYSTAL PRIME", new ChatColor[]{ChatColor.LIGHT_PURPLE}),
            new Prestige("WARRIOR PRIME", new ChatColor[]{ChatColor.DARK_BLUE}),
            new Prestige("ROYAL PRIME", new ChatColor[]{ChatColor.DARK_PURPLE}),
            new Prestige("TWILIGHT PRIME", new ChatColor[]{ChatColor.GRAY, ChatColor.DARK_GRAY}),
            new Prestige("DIMENSIONAL", new ChatColor[]{ChatColor.WHITE, ChatColor.YELLOW, ChatColor.GOLD}),
            new Prestige("DAWNBREAKER", new ChatColor[]{ChatColor.GOLD, ChatColor.WHITE, ChatColor.DARK_AQUA}),
            new Prestige("DUSTBREAKER", new ChatColor[]{ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE, ChatColor.GOLD}),
            new Prestige("CELESTIAL I", new ChatColor[]{ChatColor.DARK_AQUA, ChatColor.WHITE, ChatColor.GRAY}),
            new Prestige("CELESTIAL II", new ChatColor[]{ChatColor.WHITE, ChatColor.GREEN, ChatColor.DARK_GREEN}),
            new Prestige("CELESTIAL III", new ChatColor[]{ChatColor.DARK_RED, ChatColor.RED, ChatColor.LIGHT_PURPLE}),
            new Prestige("CELESTIAL IV", new ChatColor[]{ChatColor.YELLOW, ChatColor.WHITE, ChatColor.BLACK}),
            new Prestige("CELESTIAL V", new ChatColor[]{ChatColor.GREEN, ChatColor.DARK_GREEN, ChatColor.GOLD}),
            new Prestige("DRAGON MASTER", new ChatColor[]{ChatColor.AQUA, ChatColor.DARK_AQUA, ChatColor.DARK_BLUE}),
            new Prestige("DRAGON KING", new ChatColor[]{ChatColor.YELLOW, ChatColor.GOLD, ChatColor.RED}),
    };

    /*
    Methods
     */
    public static int getPrestigesCount() {
        return PRESTIGES.length - 1;
    }

    public static @NotNull Prestige getLowestPrestige() {
        return PRESTIGES[0];
    }

    public static @NotNull Prestige getHighestPrestige() {
        return PRESTIGES[PRESTIGES.length - 1];
    }

    public static @NotNull Prestige getPrestige(int prestige) {
        if (prestige < 0) return getLowestPrestige();
        if (prestige >= PRESTIGES.length) return getHighestPrestige();
        return PRESTIGES[prestige];
    }
}
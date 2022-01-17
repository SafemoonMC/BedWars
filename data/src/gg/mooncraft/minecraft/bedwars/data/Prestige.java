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

}
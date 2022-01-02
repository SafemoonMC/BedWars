package gg.mooncraft.minecraft.bedwars.game.match;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public enum GeneratorType {

    DIAMOND(ChatColor.AQUA + "Diamond", Material.DIAMOND_BLOCK, Material.DIAMOND),
    EMERALD(ChatColor.DARK_GREEN + "Emerald", Material.EMERALD_BLOCK, Material.EMERALD);


    /*
    Fields
     */
    private final @NotNull String display;
    private final @NotNull Material headMaterial;
    private final @NotNull Material dropMaterial;

}
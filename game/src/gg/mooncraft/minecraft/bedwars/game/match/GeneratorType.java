package gg.mooncraft.minecraft.bedwars.game.match;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public enum GeneratorType {

    DIAMOND(Material.DIAMOND_BLOCK, Material.DIAMOND),
    EMERALD(Material.EMERALD_BLOCK, Material.EMERALD);


    /*
    Fields
     */
    private final @NotNull Material headMaterial;
    private final @NotNull Material dropMaterial;

}
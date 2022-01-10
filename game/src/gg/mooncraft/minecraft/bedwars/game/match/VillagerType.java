package gg.mooncraft.minecraft.bedwars.game.match;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.utilities.DisplayUtilities;

@Getter
@AllArgsConstructor
public enum VillagerType {

    ITEM_SHOP(DisplayUtilities.getColored("&3&lITEM SHOP")),
    UPGRADE_SHOP(DisplayUtilities.getColored("&b&lUPGRADE SHOP"));

    /*
    Fields
     */
    private final @NotNull String display;
}
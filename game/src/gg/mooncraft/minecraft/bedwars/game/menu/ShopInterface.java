package gg.mooncraft.minecraft.bedwars.game.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchTeam;

public interface ShopInterface extends InventoryHolder {

    void onClick(int slot, @NotNull Player player, @NotNull GameMatchPlayer gameMatchPlayer, @NotNull GameMatchTeam gameMatchTeam);
}
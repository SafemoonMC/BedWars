package gg.mooncraft.minecraft.bedwars.game.traps;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.items.ItemStackCreator;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.options.OptionEntry;
import gg.mooncraft.minecraft.bedwars.game.utilities.ItemsUtilities;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TeamTrap {

    TRAP_1(Material.TRIPWIRE_HOOK, "It's a trap!", "&7Inflicts Blindness and Slowness\n&7for 8 seconds.", new OptionEntry<>(Material.DIAMOND, 1)),
    TRAP_2(Material.FEATHER, "Counter-Offensive Trap", "&7Grants Speed II and Jump Boost\n&7II for 15 seconds to allied\n&7players near your base.", new OptionEntry<>(Material.DIAMOND, 1)),
    TRAP_3(Material.REDSTONE_TORCH, "Alarm Trap", "&7Reveals invisible players as\n&7well as their name and team.", new OptionEntry<>(Material.DIAMOND, 1)),
    TRAP_4(Material.IRON_PICKAXE, "Miner Fatigue Trap", "&7Inflict Mining Fatigue for 10\n&7seconds.", new OptionEntry<>(Material.DIAMOND, 1));


    /*
    Fields
     */
    private final @NotNull Material material;
    private final @NotNull String display;
    private final @NotNull String description;
    private final @NotNull OptionEntry<Material, Integer> costEntry;

    /*
    Methods
     */
    public @NotNull ItemStack getIconItem(@NotNull Player player, @NotNull GameMatchPlayer gameMatchPlayer) {
        String color = player.getInventory().contains(this.costEntry.getKey(), this.costEntry.getValue()) ? "&a" : "&c";
        String lastLine = gameMatchPlayer.getParent().getTrapList().contains(this) ? "&6The trap is already in place!" : player.getInventory().contains(this.costEntry.getKey(), this.costEntry.getValue()) ? "&eClick to purchase!" : "&cYou can't afford this.";
        return ItemStackCreator.using(ItemsUtilities.createPureItem(getMaterial()))
                .meta()
                .display(color + display)
                .lore(Arrays.asList(getDescription(), "", "&7Cost: " + getCost(), "", lastLine))
                .stack().create();
    }

    private @NotNull String getCost() {
        String material = this.costEntry.getKey() == Material.IRON_INGOT ? "Iron" : this.costEntry.getKey() == Material.GOLD_INGOT ? "Gold" : this.costEntry.getKey() == Material.DIAMOND ? "Diamond" : "Emerald";
        String color = this.costEntry.getKey() == Material.IRON_INGOT ? "&f" : this.costEntry.getKey() == Material.GOLD_INGOT ? "&6" : this.costEntry.getKey() == Material.DIAMOND ? "&b" : "&2";
        return color + this.costEntry.getValue() + " " + material;
    }
}
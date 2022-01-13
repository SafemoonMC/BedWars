package gg.mooncraft.minecraft.bedwars.game.upgrades;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.match.options.OptionEntry;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class TeamUpgrades {

    /*
    Constants
     */
    public static final @NotNull UpgradeSet UPGRADE_SET = new UpgradeSet();

    /*
    Static Initialization
     */
    static {
        UPGRADE_SET.addItem("weapon",
                1,
                "",
                new OptionEntry<>(Material.DIAMOND, 4),
                Material.IRON_SWORD
        );
        UPGRADE_SET.addItem("armor",
                1,
                "Protection I",
                new OptionEntry<>(Material.DIAMOND, 2),
                Material.LEATHER_CHESTPLATE
        );
        UPGRADE_SET.addItem("armor",
                2,
                "Protection II",
                new OptionEntry<>(Material.DIAMOND, 4),
                Material.GOLDEN_CHESTPLATE
        );
        UPGRADE_SET.addItem("armor",
                3,
                "Protection III",
                new OptionEntry<>(Material.DIAMOND, 8),
                Material.IRON_CHESTPLATE
        );
        UPGRADE_SET.addItem("armor",
                4,
                "Protection IV",
                new OptionEntry<>(Material.DIAMOND, 16),
                Material.DIAMOND_CHESTPLATE
        );
        UPGRADE_SET.addItem("miner",
                1,
                "Haste I",
                new OptionEntry<>(Material.DIAMOND, 2),
                Material.IRON_PICKAXE
        );
        UPGRADE_SET.addItem("miner",
                2,
                "Haste II",
                new OptionEntry<>(Material.DIAMOND, 4),
                Material.GOLDEN_PICKAXE
        );
        UPGRADE_SET.addItem("furnace",
                1,
                "+50% Resources",
                new OptionEntry<>(Material.DIAMOND, 2),
                Material.IRON_INGOT
        );
        UPGRADE_SET.addItem("furnace",
                2,
                "+100% Resources",
                new OptionEntry<>(Material.DIAMOND, 4),
                Material.GOLD_INGOT
        );
        UPGRADE_SET.addItem("furnace",
                3,
                "Spawn emeralds",
                new OptionEntry<>(Material.DIAMOND, 6),
                Material.EMERALD
        );
        UPGRADE_SET.addItem("furnace",
                4,
                "+200% Resources",
                new OptionEntry<>(Material.DIAMOND, 8),
                Material.FURNACE
        );
        UPGRADE_SET.addItem("healpool",
                1,
                "",
                new OptionEntry<>(Material.DIAMOND, 1),
                Material.BEACON
        );
        UPGRADE_SET.addItem("dragon",
                1,
                "",
                new OptionEntry<>(Material.DIAMOND, 5),
                Material.IRON_SWORD
        );
    }
}
package gg.mooncraft.minecraft.bedwars.game.shop;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.game.items.ItemStackCreator;
import gg.mooncraft.minecraft.bedwars.game.match.options.OptionEntry;
import gg.mooncraft.minecraft.bedwars.game.utilities.ItemsUtilities;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class Shops {

    /*
    Constants
     */
    private final static @NotNull Shop SOLO_SHOP = new Shop();
    private final static @NotNull Shop DUOS_SHOP = new Shop();
    private final static @NotNull Shop TRIOS_SHOP = new Shop();
    private final static @NotNull Shop QUADS_SHOP = new Shop();

    /*
    Static Initialization
     */
    static {
        // Blocks SOLO
        SOLO_SHOP.addItem("blocks", new ShopElementItemDynamic(
                        "Wool",
                        "&7Great for bridging across\n&7islands. Turns into your team's\n&7color.",
                        new OptionEntry<>(Material.IRON_INGOT, 4),
                        Material.WHITE_WOOL,
                        gameMatchPlayer -> ItemStackCreator.using(ItemsUtilities.createWoolitem(gameMatchPlayer.getParent().getGameTeam())).amount(16).create()
                )
        );
        SOLO_SHOP.addItem("blocks", new ShopElementItem(
                        "Hardened Clay",
                        "&7Basic block to defend your bed.",
                        new OptionEntry<>(Material.IRON_INGOT, 12),
                        Material.TERRACOTTA,
                        ItemStackCreator.using(Material.TERRACOTTA).amount(16).create()
                )
        );
        SOLO_SHOP.addItem("blocks", new ShopElementItem(
                        "Blast-Proof Glass",
                        "&7Immune to explosions.",
                        new OptionEntry<>(Material.IRON_INGOT, 12),
                        Material.WHITE_STAINED_GLASS,
                        ItemStackCreator.using(Material.WHITE_STAINED_GLASS).amount(4).create()
                )
        );
        SOLO_SHOP.addItem("blocks", new ShopElementItem(
                        "End Stone",
                        "&7Solid block to defend your bed.",
                        new OptionEntry<>(Material.IRON_INGOT, 24),
                        Material.END_STONE,
                        ItemStackCreator.using(Material.END_STONE).amount(12).create()
                )
        );
        SOLO_SHOP.addItem("blocks", new ShopElementItem(
                        "Ladder",
                        "&7Useful to save cats stuck in\n&7trees.",
                        new OptionEntry<>(Material.IRON_INGOT, 4),
                        Material.LADDER,
                        ItemStackCreator.using(Material.LADDER).amount(8).create()
                )
        );
        SOLO_SHOP.addItem("blocks", new ShopElementItem(
                        "Oak Wood Planks",
                        "&7Good block to defend your bed.\n&7Strong agains pickaxes.",
                        new OptionEntry<>(Material.GOLD_INGOT, 4),
                        Material.OAK_PLANKS,
                        ItemStackCreator.using(Material.OAK_PLANKS).amount(16).create()
                )
        );
        SOLO_SHOP.addItem("blocks", new ShopElementItem(
                        "Obsidian",
                        "&7Extreme protection for your bed.",
                        new OptionEntry<>(Material.EMERALD, 4),
                        Material.OBSIDIAN,
                        ItemStackCreator.using(Material.OBSIDIAN).amount(4).create()
                )
        );
        // Blocks DUOS
        DUOS_SHOP.addItem("blocks", new ShopElementItemDynamic(
                        "Wool",
                        "&7Great for bridging across\n&7islands. Turns into your team's\n&7color.",
                        new OptionEntry<>(Material.IRON_INGOT, 4),
                        Material.WHITE_WOOL,
                        gameMatchPlayer -> ItemStackCreator.using(ItemsUtilities.createWoolitem(gameMatchPlayer.getParent().getGameTeam())).amount(16).create()
                )
        );
        DUOS_SHOP.addItem("blocks", new ShopElementItem(
                        "Hardened Clay",
                        "&7Basic block to defend your bed.",
                        new OptionEntry<>(Material.IRON_INGOT, 12),
                        Material.TERRACOTTA,
                        ItemStackCreator.using(Material.TERRACOTTA).amount(16).create()
                )
        );
        DUOS_SHOP.addItem("blocks", new ShopElementItem(
                        "Blast-Proof Glass",
                        "&7Immune to explosions.",
                        new OptionEntry<>(Material.IRON_INGOT, 12),
                        Material.WHITE_STAINED_GLASS,
                        ItemStackCreator.using(Material.WHITE_STAINED_GLASS).amount(4).create()
                )
        );
        DUOS_SHOP.addItem("blocks", new ShopElementItem(
                        "End Stone",
                        "&7Solid block to defend your bed.",
                        new OptionEntry<>(Material.IRON_INGOT, 24),
                        Material.END_STONE,
                        ItemStackCreator.using(Material.END_STONE).amount(12).create()
                )
        );
        DUOS_SHOP.addItem("blocks", new ShopElementItem(
                        "Ladder",
                        "&7Useful to save cats stuck in\n&7trees.",
                        new OptionEntry<>(Material.IRON_INGOT, 4),
                        Material.LADDER,
                        ItemStackCreator.using(Material.LADDER).amount(8).create()
                )
        );
        DUOS_SHOP.addItem("blocks", new ShopElementItem(
                        "Oak Wood Planks",
                        "&7Good block to defend your bed.\n&7Strong agains pickaxes.",
                        new OptionEntry<>(Material.GOLD_INGOT, 4),
                        Material.OAK_PLANKS,
                        ItemStackCreator.using(Material.OAK_PLANKS).amount(16).create()
                )
        );
        DUOS_SHOP.addItem("blocks", new ShopElementItem(
                        "Obsidian",
                        "&7Extreme protection for your bed.",
                        new OptionEntry<>(Material.EMERALD, 4),
                        Material.OBSIDIAN,
                        ItemStackCreator.using(Material.OBSIDIAN).amount(4).create()
                )
        );
        // Blocks TRIOS
        TRIOS_SHOP.addItem("blocks", new ShopElementItemDynamic(
                        "Wool",
                        "&7Great for bridging across\n&7islands. Turns into your team's\n&7color.",
                        new OptionEntry<>(Material.IRON_INGOT, 4),
                        Material.WHITE_WOOL,
                        gameMatchPlayer -> ItemStackCreator.using(ItemsUtilities.createWoolitem(gameMatchPlayer.getParent().getGameTeam())).amount(16).create()
                )
        );
        TRIOS_SHOP.addItem("blocks", new ShopElementItem(
                        "Hardened Clay",
                        "&7Basic block to defend your bed.",
                        new OptionEntry<>(Material.IRON_INGOT, 12),
                        Material.TERRACOTTA,
                        ItemStackCreator.using(Material.TERRACOTTA).amount(16).create()
                )
        );
        TRIOS_SHOP.addItem("blocks", new ShopElementItem(
                        "Blast-Proof Glass",
                        "&7Immune to explosions.",
                        new OptionEntry<>(Material.IRON_INGOT, 12),
                        Material.WHITE_STAINED_GLASS,
                        ItemStackCreator.using(Material.WHITE_STAINED_GLASS).amount(4).create()
                )
        );
        TRIOS_SHOP.addItem("blocks", new ShopElementItem(
                        "End Stone",
                        "&7Solid block to defend your bed.",
                        new OptionEntry<>(Material.IRON_INGOT, 24),
                        Material.END_STONE,
                        ItemStackCreator.using(Material.END_STONE).amount(12).create()
                )
        );
        TRIOS_SHOP.addItem("blocks", new ShopElementItem(
                        "Ladder",
                        "&7Useful to save cats stuck in\n&7trees.",
                        new OptionEntry<>(Material.IRON_INGOT, 4),
                        Material.LADDER,
                        ItemStackCreator.using(Material.LADDER).amount(8).create()
                )
        );
        TRIOS_SHOP.addItem("blocks", new ShopElementItem(
                        "Oak Wood Planks",
                        "&7Good block to defend your bed.\n&7Strong agains pickaxes.",
                        new OptionEntry<>(Material.GOLD_INGOT, 4),
                        Material.OAK_PLANKS,
                        ItemStackCreator.using(Material.OAK_PLANKS).amount(16).create()
                )
        );
        TRIOS_SHOP.addItem("blocks", new ShopElementItem(
                        "Obsidian",
                        "&7Extreme protection for your bed.",
                        new OptionEntry<>(Material.EMERALD, 4),
                        Material.OBSIDIAN,
                        ItemStackCreator.using(Material.OBSIDIAN).amount(4).create()
                )
        );
        // Blocks QUADS
        QUADS_SHOP.addItem("blocks", new ShopElementItemDynamic(
                        "Wool",
                        "&7Great for bridging across\n&7islands. Turns into your team's\n&7color.",
                        new OptionEntry<>(Material.IRON_INGOT, 4),
                        Material.WHITE_WOOL,
                        gameMatchPlayer -> ItemStackCreator.using(ItemsUtilities.createWoolitem(gameMatchPlayer.getParent().getGameTeam())).amount(16).create()
                )
        );
        QUADS_SHOP.addItem("blocks", new ShopElementItem(
                        "Hardened Clay",
                        "&7Basic block to defend your bed.",
                        new OptionEntry<>(Material.IRON_INGOT, 12),
                        Material.TERRACOTTA,
                        ItemStackCreator.using(Material.TERRACOTTA).amount(16).create()
                )
        );
        QUADS_SHOP.addItem("blocks", new ShopElementItem(
                        "Blast-Proof Glass",
                        "&7Immune to explosions.",
                        new OptionEntry<>(Material.IRON_INGOT, 12),
                        Material.WHITE_STAINED_GLASS,
                        ItemStackCreator.using(Material.WHITE_STAINED_GLASS).amount(4).create()
                )
        );
        QUADS_SHOP.addItem("blocks", new ShopElementItem(
                        "End Stone",
                        "&7Solid block to defend your bed.",
                        new OptionEntry<>(Material.IRON_INGOT, 24),
                        Material.END_STONE,
                        ItemStackCreator.using(Material.END_STONE).amount(12).create()
                )
        );
        QUADS_SHOP.addItem("blocks", new ShopElementItem(
                        "Ladder",
                        "&7Useful to save cats stuck in\n&7trees.",
                        new OptionEntry<>(Material.IRON_INGOT, 4),
                        Material.LADDER,
                        ItemStackCreator.using(Material.LADDER).amount(16).create()
                )
        );
        QUADS_SHOP.addItem("blocks", new ShopElementItem(
                        "Oak Wood Planks",
                        "&7Good block to defend your bed.\n&7Strong agains pickaxes.",
                        new OptionEntry<>(Material.GOLD_INGOT, 4),
                        Material.OAK_PLANKS,
                        ItemStackCreator.using(Material.OAK_PLANKS).amount(16).create()
                )
        );
        QUADS_SHOP.addItem("blocks", new ShopElementItem(
                        "Obsidian",
                        "&7Extreme protection for your bed.",
                        new OptionEntry<>(Material.EMERALD, 4),
                        Material.OBSIDIAN,
                        ItemStackCreator.using(Material.OBSIDIAN).amount(4).create()
                )
        );
        // Melee SOLO
        SOLO_SHOP.addItem("melee", new ShopElementItem(
                        "Stone Sword",
                        "",
                        new OptionEntry<>(Material.IRON_INGOT, 10),
                        Material.STONE_SWORD,
                        ItemStackCreator.using(Material.STONE_SWORD).amount(1).create()
                )
        );
        SOLO_SHOP.addItem("melee", new ShopElementItem(
                        "Iron Sword",
                        "",
                        new OptionEntry<>(Material.GOLD_INGOT, 7),
                        Material.IRON_SWORD,
                        ItemStackCreator.using(Material.IRON_SWORD).amount(1).create()
                )
        );
        SOLO_SHOP.addItem("melee", new ShopElementItem(
                        "Diamond Sword",
                        "",
                        new OptionEntry<>(Material.EMERALD, 4),
                        Material.DIAMOND_SWORD,
                        ItemStackCreator.using(Material.DIAMOND_SWORD).amount(1).create()
                )
        );
        SOLO_SHOP.addItem("melee", new ShopElementItem(
                        "Stick (Knockback I)",
                        "",
                        new OptionEntry<>(Material.GOLD_INGOT, 5),
                        Material.STICK,
                        ItemStackCreator.using(Material.STICK).amount(1).enchant(Enchantment.KNOCKBACK, 1).create()
                )
        );
        // Melee DUOS
        DUOS_SHOP.addItem("melee", new ShopElementItem(
                        "Stone Sword",
                        "",
                        new OptionEntry<>(Material.IRON_INGOT, 10),
                        Material.STONE_SWORD,
                        ItemStackCreator.using(Material.STONE_SWORD).amount(1).create()
                )
        );
        DUOS_SHOP.addItem("melee", new ShopElementItem(
                        "Iron Sword",
                        "",
                        new OptionEntry<>(Material.GOLD_INGOT, 7),
                        Material.IRON_SWORD,
                        ItemStackCreator.using(Material.IRON_SWORD).amount(1).create()
                )
        );
        DUOS_SHOP.addItem("melee", new ShopElementItem(
                        "Diamond Sword",
                        "",
                        new OptionEntry<>(Material.EMERALD, 4),
                        Material.DIAMOND_SWORD,
                        ItemStackCreator.using(Material.DIAMOND_SWORD).amount(1).create()
                )
        );
        DUOS_SHOP.addItem("melee", new ShopElementItem(
                        "Stick (Knockback I)",
                        "",
                        new OptionEntry<>(Material.GOLD_INGOT, 5),
                        Material.STICK,
                        ItemStackCreator.using(Material.STICK).amount(1).enchant(Enchantment.KNOCKBACK, 1).create()
                )
        );
        // Melee TRIOS
        TRIOS_SHOP.addItem("melee", new ShopElementItem(
                        "Stone Sword",
                        "",
                        new OptionEntry<>(Material.IRON_INGOT, 10),
                        Material.STONE_SWORD,
                        ItemStackCreator.using(Material.STONE_SWORD).amount(1).create()
                )
        );
        TRIOS_SHOP.addItem("melee", new ShopElementItem(
                        "Iron Sword",
                        "",
                        new OptionEntry<>(Material.GOLD_INGOT, 7),
                        Material.IRON_SWORD,
                        ItemStackCreator.using(Material.IRON_SWORD).amount(1).create()
                )
        );
        TRIOS_SHOP.addItem("melee", new ShopElementItem(
                        "Diamond Sword",
                        "",
                        new OptionEntry<>(Material.EMERALD, 3),
                        Material.DIAMOND_SWORD,
                        ItemStackCreator.using(Material.DIAMOND_SWORD).amount(1).create()
                )
        );
        TRIOS_SHOP.addItem("melee", new ShopElementItem(
                        "Stick (Knockback I)",
                        "",
                        new OptionEntry<>(Material.GOLD_INGOT, 5),
                        Material.STICK,
                        ItemStackCreator.using(Material.STICK).amount(1).enchant(Enchantment.KNOCKBACK, 1).create()
                )
        );
        // Melee QUADS
        QUADS_SHOP.addItem("melee", new ShopElementItem(
                        "Stone Sword",
                        "",
                        new OptionEntry<>(Material.IRON_INGOT, 10),
                        Material.STONE_SWORD,
                        ItemStackCreator.using(Material.STONE_SWORD).amount(1).create()
                )
        );
        QUADS_SHOP.addItem("melee", new ShopElementItem(
                        "Iron Sword",
                        "",
                        new OptionEntry<>(Material.GOLD_INGOT, 7),
                        Material.IRON_SWORD,
                        ItemStackCreator.using(Material.IRON_SWORD).amount(1).create()
                )
        );
        QUADS_SHOP.addItem("melee", new ShopElementItem(
                        "Diamond Sword",
                        "",
                        new OptionEntry<>(Material.EMERALD, 3),
                        Material.DIAMOND_SWORD,
                        ItemStackCreator.using(Material.DIAMOND_SWORD).amount(1).create()
                )
        );
        QUADS_SHOP.addItem("melee", new ShopElementItem(
                        "Stick (Knockback I)",
                        "",
                        new OptionEntry<>(Material.GOLD_INGOT, 5),
                        Material.STICK,
                        ItemStackCreator.using(Material.STICK).amount(1).enchant(Enchantment.KNOCKBACK, 1).create()
                )
        );
        // Armor SOLO
        SOLO_SHOP.addItem("armor", new ShopElementItem(
                        "Permanent Chainmail Armor",
                        "&7Chainmail leggings and boots\n&7which you will always spawn\n&7with.",
                        new OptionEntry<>(Material.IRON_INGOT, 30),
                        Material.CHAINMAIL_BOOTS,
                        ItemStackCreator.using(Material.CHAINMAIL_BOOTS).amount(1).create()
                )
        );
        SOLO_SHOP.addItem("armor", new ShopElementItem(
                        "Permanent Iron Armor",
                        "&7Iron leggings and boots which\n&7you will always spawn with.",
                        new OptionEntry<>(Material.GOLD_INGOT, 12),
                        Material.IRON_BOOTS,
                        ItemStackCreator.using(Material.IRON_BOOTS).amount(1).create()
                )
        );
        SOLO_SHOP.addItem("armor", new ShopElementItem(
                        "Permanent Diamond Armor",
                        "&7Diamond leggings and boots which\n&7you will always crush with.",
                        new OptionEntry<>(Material.EMERALD, 6),
                        Material.DIAMOND_BOOTS,
                        ItemStackCreator.using(Material.DIAMOND_BOOTS).amount(1).create()
                )
        );
        // Armor DUOS
        DUOS_SHOP.addItem("armor", new ShopElementItem(
                        "Permanent Chainmail Armor",
                        "&7Chainmail leggings and boots\n&7which you will always spawn\n&7with.",
                        new OptionEntry<>(Material.IRON_INGOT, 30),
                        Material.CHAINMAIL_BOOTS,
                        ItemStackCreator.using(Material.CHAINMAIL_BOOTS).amount(1).create()
                )
        );
        DUOS_SHOP.addItem("armor", new ShopElementItem(
                        "Permanent Iron Armor",
                        "&7Iron leggings and boots which\n&7you will always spawn with.",
                        new OptionEntry<>(Material.GOLD_INGOT, 12),
                        Material.IRON_BOOTS,
                        ItemStackCreator.using(Material.IRON_BOOTS).amount(1).create()
                )
        );
        DUOS_SHOP.addItem("armor", new ShopElementItem(
                        "Permanent Diamond Armor",
                        "&7Diamond leggings and boots which\n&7you will always crush with.",
                        new OptionEntry<>(Material.EMERALD, 6),
                        Material.DIAMOND_BOOTS,
                        ItemStackCreator.using(Material.DIAMOND_BOOTS).amount(1).create()
                )
        );
        // Armor TRIOS
        TRIOS_SHOP.addItem("armor", new ShopElementItem(
                        "Permanent Chainmail Armor",
                        "&7Chainmail leggings and boots\n&7which you will always spawn\n&7with.",
                        new OptionEntry<>(Material.IRON_INGOT, 30),
                        Material.CHAINMAIL_BOOTS,
                        ItemStackCreator.using(Material.CHAINMAIL_BOOTS).amount(1).create()
                )
        );
        TRIOS_SHOP.addItem("armor", new ShopElementItem(
                        "Permanent Iron Armor",
                        "&7Iron leggings and boots which\n&7you will always spawn with.",
                        new OptionEntry<>(Material.GOLD_INGOT, 12),
                        Material.IRON_BOOTS,
                        ItemStackCreator.using(Material.IRON_BOOTS).amount(1).create()
                )
        );
        TRIOS_SHOP.addItem("armor", new ShopElementItem(
                        "Permanent Diamond Armor",
                        "&7Diamond leggings and boots which\n&7you will always crush with.",
                        new OptionEntry<>(Material.EMERALD, 6),
                        Material.DIAMOND_BOOTS,
                        ItemStackCreator.using(Material.DIAMOND_BOOTS).amount(1).create()
                )
        );
        // Armor QUADS
        QUADS_SHOP.addItem("armor", new ShopElementItem(
                        "Permanent Chainmail Armor",
                        "&7Chainmail leggings and boots\n&7which you will always spawn\n&7with.",
                        new OptionEntry<>(Material.IRON_INGOT, 30),
                        Material.CHAINMAIL_BOOTS,
                        ItemStackCreator.using(Material.CHAINMAIL_BOOTS).amount(1).create()
                )
        );
        QUADS_SHOP.addItem("armor", new ShopElementItem(
                        "Permanent Iron Armor",
                        "&7Iron leggings and boots which\n&7you will always spawn with.",
                        new OptionEntry<>(Material.GOLD_INGOT, 12),
                        Material.IRON_BOOTS,
                        ItemStackCreator.using(Material.IRON_BOOTS).amount(1).create()
                )
        );
        QUADS_SHOP.addItem("armor", new ShopElementItem(
                        "Permanent Diamond Armor",
                        "&7Diamond leggings and boots which\n&7you will always crush with.",
                        new OptionEntry<>(Material.EMERALD, 6),
                        Material.DIAMOND_BOOTS,
                        ItemStackCreator.using(Material.DIAMOND_BOOTS).amount(1).create()
                )
        );
        // Tools SOLO
        SOLO_SHOP.addItem("tools", new ShopElementItem(
                        "Permanent Shears",
                        "&7Great job to get rid of wool. You\n&7will always spawn with these\n&7shears.",
                        new OptionEntry<>(Material.IRON_INGOT, 20),
                        Material.SHEARS,
                        ItemStackCreator.using(Material.SHEARS).amount(1).create()
                )
        );
        SOLO_SHOP.addItem("tools", new ShopElementItem(
                        "&6Wooden Pickaxe",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.WOODEN_PICKAXE,
                        ItemStackCreator.using(Material.WOODEN_PICKAXE).amount(1).enchant(Enchantment.DIG_SPEED, 1).create()
                )
        );
        SOLO_SHOP.addItem("tools", new ShopElementItem(
                        "&6Wooden Axe",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.WOODEN_AXE,
                        ItemStackCreator.using(Material.WOODEN_AXE).enchant(Enchantment.DIG_SPEED, 1).amount(1).create()
                )
        );
        // Tools DUOS
        DUOS_SHOP.addItem("tools", new ShopElementItem(
                        "Permanent Shears",
                        "&7Great job to get rid of wool. You\n&7will always spawn with these\n&7shears.",
                        new OptionEntry<>(Material.IRON_INGOT, 20),
                        Material.SHEARS,
                        ItemStackCreator.using(Material.SHEARS).amount(1).create()
                )
        );
        DUOS_SHOP.addItem("tools", new ShopElementItem(
                        "&6Wooden Pickaxe",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.WOODEN_PICKAXE,
                        ItemStackCreator.using(Material.WOODEN_PICKAXE).enchant(Enchantment.DIG_SPEED, 1).amount(1).create()
                )
        );
        DUOS_SHOP.addItem("tools", new ShopElementItem(
                        "&6Wooden Axe",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.WOODEN_AXE,
                        ItemStackCreator.using(Material.WOODEN_AXE).enchant(Enchantment.DIG_SPEED, 1).amount(1).create()
                )
        );
        // Tools TRIOS
        TRIOS_SHOP.addItem("tools", new ShopElementItem(
                        "Permanent Shears",
                        "&7Great job to get rid of wool. You\n&7will always spawn with these\n&7shears.",
                        new OptionEntry<>(Material.IRON_INGOT, 20),
                        Material.SHEARS,
                        ItemStackCreator.using(Material.SHEARS).amount(1).create()
                )
        );
        TRIOS_SHOP.addItem("tools", new ShopElementItem(
                        "&6Wooden Pickaxe",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.WOODEN_PICKAXE,
                        ItemStackCreator.using(Material.WOODEN_PICKAXE).enchant(Enchantment.DIG_SPEED, 1).amount(1).create()
                )
        );
        TRIOS_SHOP.addItem("tools", new ShopElementItem(
                        "&6Wooden Axe",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.WOODEN_AXE,
                        ItemStackCreator.using(Material.WOODEN_AXE).enchant(Enchantment.DIG_SPEED, 1).amount(1).create()
                )
        );
        // Tools QUADS
        QUADS_SHOP.addItem("tools", new ShopElementItem(
                        "Permanent Shears",
                        "&7Great job to get rid of wool. You\n&7will always spawn with these\n&7shears.",
                        new OptionEntry<>(Material.IRON_INGOT, 20),
                        Material.SHEARS,
                        ItemStackCreator.using(Material.SHEARS).amount(1).create()
                )
        );
        QUADS_SHOP.addItem("tools", new ShopElementItem(
                        "&6Wooden Pickaxe",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.WOODEN_PICKAXE,
                        ItemStackCreator.using(Material.WOODEN_PICKAXE).enchant(Enchantment.DIG_SPEED, 1).amount(1).create()
                )
        );
        QUADS_SHOP.addItem("tools", new ShopElementItem(
                        "&6Wooden Axe",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.WOODEN_AXE,
                        ItemStackCreator.using(Material.WOODEN_AXE).enchant(Enchantment.DIG_SPEED, 1).amount(1).create()
                )
        );
        // Ranged SOLO
        SOLO_SHOP.addItem("ranged", new ShopElementItem(
                        "Arrow",
                        "",
                        new OptionEntry<>(Material.GOLD_INGOT, 2),
                        Material.ARROW,
                        ItemStackCreator.using(Material.ARROW).amount(6).create()
                )
        );
        SOLO_SHOP.addItem("ranged", new ShopElementItem(
                        "Bow",
                        "",
                        new OptionEntry<>(Material.GOLD_INGOT, 12),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).create()
                )
        );
        SOLO_SHOP.addItem("ranged", new ShopElementItem(
                        "Bow (Power I)",
                        "",
                        new OptionEntry<>(Material.GOLD_INGOT, 20),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).enchant(Enchantment.ARROW_DAMAGE, 1).create()
                )
        );
        SOLO_SHOP.addItem("ranged", new ShopElementItem(
                        "Bow (Power I, Punch I)",
                        "",
                        new OptionEntry<>(Material.EMERALD, 6),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).enchant(Enchantment.ARROW_DAMAGE, 1).enchant(Enchantment.ARROW_KNOCKBACK, 1).create()
                )
        );
        // Ranged DUOS
        DUOS_SHOP.addItem("ranged", new ShopElementItem(
                        "Arrow",
                        "",
                        new OptionEntry<>(Material.GOLD_INGOT, 2),
                        Material.ARROW,
                        ItemStackCreator.using(Material.ARROW).amount(6).create()
                )
        );
        DUOS_SHOP.addItem("ranged", new ShopElementItem(
                        "Bow",
                        "",
                        new OptionEntry<>(Material.GOLD_INGOT, 12),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).create()
                )
        );
        DUOS_SHOP.addItem("ranged", new ShopElementItem(
                        "Bow (Power I)",
                        "",
                        new OptionEntry<>(Material.GOLD_INGOT, 20),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).enchant(Enchantment.ARROW_DAMAGE, 1).create()
                )
        );
        DUOS_SHOP.addItem("ranged", new ShopElementItem(
                        "Bow (Power I, Punch I)",
                        "",
                        new OptionEntry<>(Material.EMERALD, 6),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).enchant(Enchantment.ARROW_DAMAGE, 1).enchant(Enchantment.ARROW_KNOCKBACK, 1).create()
                )
        );
        // Ranged TRIOS
        TRIOS_SHOP.addItem("ranged", new ShopElementItem(
                        "Arrow",
                        "",
                        new OptionEntry<>(Material.GOLD_INGOT, 2),
                        Material.ARROW,
                        ItemStackCreator.using(Material.ARROW).amount(6).create()
                )
        );
        TRIOS_SHOP.addItem("ranged", new ShopElementItem(
                        "Bow",
                        "",
                        new OptionEntry<>(Material.GOLD_INGOT, 12),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).create()
                )
        );
        TRIOS_SHOP.addItem("ranged", new ShopElementItem(
                        "Bow (Power I)",
                        "",
                        new OptionEntry<>(Material.GOLD_INGOT, 20),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).enchant(Enchantment.ARROW_DAMAGE, 1).create()
                )
        );
        TRIOS_SHOP.addItem("ranged", new ShopElementItem(
                        "Bow (Power I, Punch I)",
                        "",
                        new OptionEntry<>(Material.EMERALD, 6),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).enchant(Enchantment.ARROW_DAMAGE, 1).enchant(Enchantment.ARROW_KNOCKBACK, 1).create()
                )
        );
        // Ranged QUADS
        QUADS_SHOP.addItem("ranged", new ShopElementItem(
                        "Arrow",
                        "",
                        new OptionEntry<>(Material.GOLD_INGOT, 2),
                        Material.ARROW,
                        ItemStackCreator.using(Material.ARROW).amount(6).create()
                )
        );
        QUADS_SHOP.addItem("ranged", new ShopElementItem(
                        "Bow",
                        "",
                        new OptionEntry<>(Material.GOLD_INGOT, 12),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).create()
                )
        );
        QUADS_SHOP.addItem("ranged", new ShopElementItem(
                        "Bow (Power I)",
                        "",
                        new OptionEntry<>(Material.GOLD_INGOT, 20),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).enchant(Enchantment.ARROW_DAMAGE, 1).create()
                )
        );
        QUADS_SHOP.addItem("ranged", new ShopElementItem(
                        "Bow (Power I, Punch I)",
                        "",
                        new OptionEntry<>(Material.EMERALD, 6),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).enchant(Enchantment.ARROW_DAMAGE, 1).enchant(Enchantment.ARROW_KNOCKBACK, 1).create()
                )
        );
    }

    /*
    Static Methods
     */
    public static @NotNull Shop getShop(@NotNull GameMode gameMode) {
        switch (gameMode) {
            case DUOS -> {
                return DUOS_SHOP;
            }
            case TRIOS -> {
                return TRIOS_SHOP;
            }
            case QUADS -> {
                return QUADS_SHOP;
            }
            default -> {
                return SOLO_SHOP;
            }
        }
    }
}
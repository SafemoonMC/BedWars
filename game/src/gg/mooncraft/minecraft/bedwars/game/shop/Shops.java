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
                        "&7Great for bridging across\n&7islands. Turns into your team's color.",
                        new OptionEntry<>(Material.IRON_INGOT, 4),
                        Material.WHITE_WOOL,
                        gameMatchPlayer -> ItemStackCreator.using(ItemsUtilities.createWoolitem(gameMatchPlayer.getParent().getGameTeam())).amount(16).create()
                )
        );
        SOLO_SHOP.addItem("blocks", new ShopElementItem(
                        "Hard Clay",
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
                        "Ladders",
                        "&7Useful to save cats stuck in\n&7trees.",
                        new OptionEntry<>(Material.IRON_INGOT, 4),
                        Material.LADDER,
                        ItemStackCreator.using(Material.LADDER).amount(16).create()
                )
        );
        SOLO_SHOP.addItem("blocks", new ShopElementItem(
                        "Wood",
                        "&7Good block to defend your bed.\n&7Strong agains pickaxes.",
                        new OptionEntry<>(Material.GOLD_INGOT, 4),
                        Material.OAK_WOOD,
                        ItemStackCreator.using(Material.OAK_WOOD).amount(16).create()
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
                        "&7Great for bridging across\n&7islands. Turns into your team's color.",
                        new OptionEntry<>(Material.IRON_INGOT, 4),
                        Material.WHITE_WOOL,
                        gameMatchPlayer -> ItemStackCreator.using(ItemsUtilities.createWoolitem(gameMatchPlayer.getParent().getGameTeam())).amount(16).create()
                )
        );
        DUOS_SHOP.addItem("blocks", new ShopElementItem(
                        "Hard Clay",
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
                        "Ladders",
                        "&7Useful to save cats stuck in\n&7trees.",
                        new OptionEntry<>(Material.IRON_INGOT, 4),
                        Material.LADDER,
                        ItemStackCreator.using(Material.LADDER).amount(16).create()
                )
        );
        DUOS_SHOP.addItem("blocks", new ShopElementItem(
                        "Wood",
                        "&7Good block to defend your bed.\n&7Strong agains pickaxes.",
                        new OptionEntry<>(Material.GOLD_INGOT, 4),
                        Material.OAK_WOOD,
                        ItemStackCreator.using(Material.OAK_WOOD).amount(16).create()
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
                        "&7Great for bridging across\n&7islands. Turns into your team's color.",
                        new OptionEntry<>(Material.IRON_INGOT, 4),
                        Material.WHITE_WOOL,
                        gameMatchPlayer -> ItemStackCreator.using(ItemsUtilities.createWoolitem(gameMatchPlayer.getParent().getGameTeam())).amount(16).create()
                )
        );
        TRIOS_SHOP.addItem("blocks", new ShopElementItem(
                        "Hard Clay",
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
                        "Ladders",
                        "&7Useful to save cats stuck in\n&7trees.",
                        new OptionEntry<>(Material.IRON_INGOT, 4),
                        Material.LADDER,
                        ItemStackCreator.using(Material.LADDER).amount(16).create()
                )
        );
        TRIOS_SHOP.addItem("blocks", new ShopElementItem(
                        "Wood",
                        "&7Good block to defend your bed.\n&7Strong agains pickaxes.",
                        new OptionEntry<>(Material.GOLD_INGOT, 4),
                        Material.OAK_WOOD,
                        ItemStackCreator.using(Material.OAK_WOOD).amount(16).create()
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
                        "&7Great for bridging across\n&7islands. Turns into your team's color.",
                        new OptionEntry<>(Material.IRON_INGOT, 4),
                        Material.WHITE_WOOL,
                        gameMatchPlayer -> ItemStackCreator.using(ItemsUtilities.createWoolitem(gameMatchPlayer.getParent().getGameTeam())).amount(16).create()
                )
        );
        QUADS_SHOP.addItem("blocks", new ShopElementItem(
                        "Hard Clay",
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
                        "Ladders",
                        "&7Useful to save cats stuck in\n&7trees.",
                        new OptionEntry<>(Material.IRON_INGOT, 4),
                        Material.LADDER,
                        ItemStackCreator.using(Material.LADDER).amount(16).create()
                )
        );
        QUADS_SHOP.addItem("blocks", new ShopElementItem(
                        "Wood",
                        "&7Good block to defend your bed.\n&7Strong agains pickaxes.",
                        new OptionEntry<>(Material.GOLD_INGOT, 4),
                        Material.OAK_WOOD,
                        ItemStackCreator.using(Material.OAK_WOOD).amount(16).create()
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
                        "&6Stone Sword",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.STONE_SWORD,
                        ItemStackCreator.using(Material.STONE_SWORD).amount(1).create()
                )
        );
        SOLO_SHOP.addItem("melee", new ShopElementItem(
                        "&6Iron Sword",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.IRON_SWORD,
                        ItemStackCreator.using(Material.IRON_SWORD).amount(1).create()
                )
        );
        SOLO_SHOP.addItem("melee", new ShopElementItem(
                        "&6Diamond Sword",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.DIAMOND_SWORD,
                        ItemStackCreator.using(Material.DIAMOND_SWORD).amount(1).create()
                )
        );
        SOLO_SHOP.addItem("melee", new ShopElementItem(
                        "&6Knockback Stick",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.STICK,
                        ItemStackCreator.using(Material.STICK).amount(1).enchant(Enchantment.KNOCKBACK, 1).create()
                )
        );
        // Melee DUOS
        DUOS_SHOP.addItem("melee", new ShopElementItem(
                        "&6Stone Sword",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.STONE_SWORD,
                        ItemStackCreator.using(Material.STONE_SWORD).amount(1).create()
                )
        );
        DUOS_SHOP.addItem("melee", new ShopElementItem(
                        "&6Iron Sword",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.IRON_SWORD,
                        ItemStackCreator.using(Material.IRON_SWORD).amount(1).create()
                )
        );
        DUOS_SHOP.addItem("melee", new ShopElementItem(
                        "&6Diamond Sword",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.DIAMOND_SWORD,
                        ItemStackCreator.using(Material.DIAMOND_SWORD).amount(1).create()
                )
        );
        DUOS_SHOP.addItem("melee", new ShopElementItem(
                        "&6Knockback Stick",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.STICK,
                        ItemStackCreator.using(Material.STICK).amount(1).enchant(Enchantment.KNOCKBACK, 1).create()
                )
        );
        // Melee TRIOS
        TRIOS_SHOP.addItem("melee", new ShopElementItem(
                        "&6Stone Sword",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.STONE_SWORD,
                        ItemStackCreator.using(Material.STONE_SWORD).amount(1).create()
                )
        );
        TRIOS_SHOP.addItem("melee", new ShopElementItem(
                        "&6Iron Sword",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.IRON_SWORD,
                        ItemStackCreator.using(Material.IRON_SWORD).amount(1).create()
                )
        );
        TRIOS_SHOP.addItem("melee", new ShopElementItem(
                        "&6Diamond Sword",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.DIAMOND_SWORD,
                        ItemStackCreator.using(Material.DIAMOND_SWORD).amount(1).create()
                )
        );
        TRIOS_SHOP.addItem("melee", new ShopElementItem(
                        "&6Knockback Stick",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.STICK,
                        ItemStackCreator.using(Material.STICK).amount(1).enchant(Enchantment.KNOCKBACK, 1).create()
                )
        );
        // Melee QUADS
        QUADS_SHOP.addItem("melee", new ShopElementItem(
                        "&6Stone Sword",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.STONE_SWORD,
                        ItemStackCreator.using(Material.STONE_SWORD).amount(1).create()
                )
        );
        QUADS_SHOP.addItem("melee", new ShopElementItem(
                        "&6Iron Sword",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.IRON_SWORD,
                        ItemStackCreator.using(Material.IRON_SWORD).amount(1).create()
                )
        );
        QUADS_SHOP.addItem("melee", new ShopElementItem(
                        "&6Diamond Sword",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.DIAMOND_SWORD,
                        ItemStackCreator.using(Material.DIAMOND_SWORD).amount(1).create()
                )
        );
        QUADS_SHOP.addItem("melee", new ShopElementItem(
                        "&6Knockback Stick",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.STICK,
                        ItemStackCreator.using(Material.STICK).amount(1).enchant(Enchantment.KNOCKBACK, 1).create()
                )
        );
        // Armor SOLO
        SOLO_SHOP.addItem("armor", new ShopElementItem(
                        "&6Chain Boots",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.CHAINMAIL_BOOTS,
                        ItemStackCreator.using(Material.CHAINMAIL_BOOTS).amount(1).create()
                )
        );
        SOLO_SHOP.addItem("armor", new ShopElementItem(
                        "&6Iron Boots",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.IRON_BOOTS,
                        ItemStackCreator.using(Material.IRON_BOOTS).amount(1).create()
                )
        );
        SOLO_SHOP.addItem("armor", new ShopElementItem(
                        "&6Diamond Boots",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.DIAMOND_BOOTS,
                        ItemStackCreator.using(Material.DIAMOND_BOOTS).amount(1).create()
                )
        );
        // Armor DUOS
        DUOS_SHOP.addItem("armor", new ShopElementItem(
                        "&6Chain Boots",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.CHAINMAIL_BOOTS,
                        ItemStackCreator.using(Material.CHAINMAIL_BOOTS).amount(1).create()
                )
        );
        DUOS_SHOP.addItem("armor", new ShopElementItem(
                        "&6Iron Boots",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.IRON_BOOTS,
                        ItemStackCreator.using(Material.IRON_BOOTS).amount(1).create()
                )
        );
        DUOS_SHOP.addItem("armor", new ShopElementItem(
                        "&6Diamond Boots",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.DIAMOND_BOOTS,
                        ItemStackCreator.using(Material.DIAMOND_BOOTS).amount(1).create()
                )
        );
        // Armor TRIOS
        TRIOS_SHOP.addItem("armor", new ShopElementItem(
                        "&6Chain Boots",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.CHAINMAIL_BOOTS,
                        ItemStackCreator.using(Material.CHAINMAIL_BOOTS).amount(1).create()
                )
        );
        TRIOS_SHOP.addItem("armor", new ShopElementItem(
                        "&6Iron Boots",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.IRON_BOOTS,
                        ItemStackCreator.using(Material.IRON_BOOTS).amount(1).create()
                )
        );
        TRIOS_SHOP.addItem("armor", new ShopElementItem(
                        "&6Diamond Boots",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.DIAMOND_BOOTS,
                        ItemStackCreator.using(Material.DIAMOND_BOOTS).amount(1).create()
                )
        );
        // Armor QUADS
        QUADS_SHOP.addItem("armor", new ShopElementItem(
                        "&6Chain Boots",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.CHAINMAIL_BOOTS,
                        ItemStackCreator.using(Material.CHAINMAIL_BOOTS).amount(1).create()
                )
        );
        QUADS_SHOP.addItem("armor", new ShopElementItem(
                        "&6Iron Boots",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.IRON_BOOTS,
                        ItemStackCreator.using(Material.IRON_BOOTS).amount(1).create()
                )
        );
        QUADS_SHOP.addItem("armor", new ShopElementItem(
                        "&6Diamond Boots",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.DIAMOND_BOOTS,
                        ItemStackCreator.using(Material.DIAMOND_BOOTS).amount(1).create()
                )
        );
        // Tools SOLO
        SOLO_SHOP.addItem("tools", new ShopElementItem(
                        "&6Shears",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
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
                        "&6Shears",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
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
                        "&6Shears",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
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
                        "&6Shears",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
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
                        "&6Arrows",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.ARROW,
                        ItemStackCreator.using(Material.ARROW).amount(6).create()
                )
        );
        SOLO_SHOP.addItem("ranged", new ShopElementItem(
                        "&6Bow",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).create()
                )
        );
        SOLO_SHOP.addItem("ranged", new ShopElementItem(
                        "&6Bow",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).enchant(Enchantment.ARROW_DAMAGE, 1).create()
                )
        );
        SOLO_SHOP.addItem("ranged", new ShopElementItem(
                        "&6Bow",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).enchant(Enchantment.ARROW_DAMAGE, 1).enchant(Enchantment.ARROW_KNOCKBACK, 1).create()
                )
        );
        // Ranged DUOS
        DUOS_SHOP.addItem("ranged", new ShopElementItem(
                        "&6Arrows",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.ARROW,
                        ItemStackCreator.using(Material.ARROW).amount(6).create()
                )
        );
        DUOS_SHOP.addItem("ranged", new ShopElementItem(
                        "&6Bow",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).create()
                )
        );
        DUOS_SHOP.addItem("ranged", new ShopElementItem(
                        "&6Bow",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).enchant(Enchantment.ARROW_DAMAGE, 1).create()
                )
        );
        DUOS_SHOP.addItem("ranged", new ShopElementItem(
                        "&6Bow",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).enchant(Enchantment.ARROW_DAMAGE, 1).enchant(Enchantment.ARROW_KNOCKBACK, 1).create()
                )
        );
        // Ranged TRIOS
        TRIOS_SHOP.addItem("ranged", new ShopElementItem(
                        "&6Arrows",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.ARROW,
                        ItemStackCreator.using(Material.ARROW).amount(6).create()
                )
        );
        TRIOS_SHOP.addItem("ranged", new ShopElementItem(
                        "&6Bow",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).create()
                )
        );
        TRIOS_SHOP.addItem("ranged", new ShopElementItem(
                        "&6Bow",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).enchant(Enchantment.ARROW_DAMAGE, 1).create()
                )
        );
        TRIOS_SHOP.addItem("ranged", new ShopElementItem(
                        "&6Bow",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).enchant(Enchantment.ARROW_DAMAGE, 1).enchant(Enchantment.ARROW_KNOCKBACK, 1).create()
                )
        );
        // Ranged QUADS
        QUADS_SHOP.addItem("ranged", new ShopElementItem(
                        "&6Arrows",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.ARROW,
                        ItemStackCreator.using(Material.ARROW).amount(6).create()
                )
        );
        QUADS_SHOP.addItem("ranged", new ShopElementItem(
                        "&6Bow",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).create()
                )
        );
        QUADS_SHOP.addItem("ranged", new ShopElementItem(
                        "&6Bow",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.BOW,
                        ItemStackCreator.using(Material.BOW).amount(1).enchant(Enchantment.ARROW_DAMAGE, 1).create()
                )
        );
        QUADS_SHOP.addItem("ranged", new ShopElementItem(
                        "&6Bow",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
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
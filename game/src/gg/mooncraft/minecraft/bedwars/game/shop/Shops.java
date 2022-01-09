package gg.mooncraft.minecraft.bedwars.game.shop;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.game.items.ItemStackCreator;
import gg.mooncraft.minecraft.bedwars.game.match.options.OptionEntry;

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
        SOLO_SHOP.addItem("blocks", new ShopElementItem(
                        "&6Wool",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.WHITE_WOOL,
                        ItemStackCreator.using(Material.WHITE_WOOL).amount(4).create()
                )
        );
        SOLO_SHOP.addItem("blocks", new ShopElementItem(
                        "&6Hardened Clay",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.CLAY,
                        ItemStackCreator.using(Material.CLAY).amount(4).create()
                )
        );
        SOLO_SHOP.addItem("blocks", new ShopElementItem(
                        "&6Blast Proof Glass",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.WHITE_STAINED_GLASS,
                        ItemStackCreator.using(Material.WHITE_STAINED_GLASS).amount(4).create()
                )
        );
        SOLO_SHOP.addItem("blocks", new ShopElementItem(
                        "&6End Stone",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.END_STONE,
                        ItemStackCreator.using(Material.END_STONE).amount(4).create()
                )
        );
        SOLO_SHOP.addItem("blocks", new ShopElementItem(
                        "&6Ladder",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.LADDER,
                        ItemStackCreator.using(Material.LADDER).amount(4).create()
                )
        );
        SOLO_SHOP.addItem("blocks", new ShopElementItem(
                        "&6Oak Wood",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.OAK_WOOD,
                        ItemStackCreator.using(Material.OAK_WOOD).amount(4).create()
                )
        );
        SOLO_SHOP.addItem("blocks", new ShopElementItem(
                        "&6Obsidian",
                        "Description",
                        new OptionEntry<>(Material.IRON_INGOT, 2),
                        Material.OBSIDIAN,
                        ItemStackCreator.using(Material.OBSIDIAN).create()
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
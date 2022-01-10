package gg.mooncraft.minecraft.bedwars.game.shop;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import gg.mooncraft.minecraft.bedwars.game.items.ItemStackCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class Shop {

    /*
    Fields
     */
    private final @NotNull List<ShopCategory> categoryList;

    /*
    Constructor
     */
    public Shop() {
        this.categoryList = new ArrayList<>();
        this.categoryList.add(new ShopCategory(
                        "blocks",
                        ItemStackCreator.using(Material.TERRACOTTA).meta()
                                .display("&bBlocks &8(&7Click&8)")
                                .lore(Arrays.asList(
                                        "&7Useful blocks you can",
                                        "&7place."
                                ))
                                .stack().create()
                )
        );
        this.categoryList.add(new ShopCategory(
                        "melee", ItemStackCreator.using(Material.GOLDEN_SWORD).meta()
                        .display("&bMelee &8(&7Click&8)")
                        .lore(Arrays.asList(
                                "&7Everything you need for",
                                "&7a face-to-face situation."
                        ))
                        .stack().create()
                )
        );
        this.categoryList.add(new ShopCategory(
                        "armor",
                        ItemStackCreator.using(Material.CHAINMAIL_BOOTS).meta()
                                .display("&bArmor &8(&7Click&8)")
                                .lore(Arrays.asList(
                                        "&7You need to defend yourself",
                                        "&7somehow!"
                                ))
                                .stack().create()
                )
        );
        this.categoryList.add(new ShopCategory(
                        "tools", ItemStackCreator.using(Material.STONE_PICKAXE).meta()
                        .display("&bTools &8(&7Click&8)")
                        .lore(Arrays.asList(
                                "&7The right tool can make",
                                "&7the difference."
                        ))
                        .stack().create()
                )
        );
        this.categoryList.add(new ShopCategory(
                        "ranged", ItemStackCreator.using(Material.BOW).meta()
                        .display("&bRanged &8(&7Click&8)")
                        .lore(Arrays.asList(
                                "&7You can fight others even at",
                                "&7long distances!"
                        ))
                        .stack().create()
                )
        );
        this.categoryList.add(new ShopCategory(
                        "potions", ItemStackCreator.using(Material.BREWING_STAND).meta()
                        .display("&bPotions &8(&7Click&8)")
                        .lore(Arrays.asList(
                                "&7Drink this and empower",
                                "&7yourself!"
                        ))
                        .stack().create()
                )
        );
        this.categoryList.add(new ShopCategory(
                        "utility", ItemStackCreator.using(Material.TNT).meta()
                        .display("&bUtility &8(&7Click&8)")
                        .lore(Arrays.asList(
                                "&7There are some utilities",
                                "&7that will change your game."
                        ))
                        .stack().create()
                )
        );
    }

    /*
    Methods
     */
    public void addItem(@NotNull String identifier, @NotNull ShopElement shopElement) {
        getCategory(identifier).ifPresent(shopCategory -> shopCategory.addElement(shopElement));
    }

    public @NotNull Optional<ShopCategory> getCategory(@NotNull String identifier) {
        return this.categoryList.stream().filter(shopCategory -> shopCategory.getIdentifier().equalsIgnoreCase(identifier)).findFirst();
    }

    @UnmodifiableView
    public @NotNull List<ShopCategory> getCategoryList() {
        return Collections.unmodifiableList(this.categoryList);
    }
}
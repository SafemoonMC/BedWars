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
                        ItemStackCreator.using(Material.STONE).meta()
                                .display("&6Blocks")
                                .lore(Arrays.asList(
                                        "&7Description",
                                        "&7Description"
                                ))
                                .stack().create()
                )
        );
        this.categoryList.add(new ShopCategory(
                        "melee", ItemStackCreator.using(Material.STONE).meta()
                        .display("&6Melee")
                        .lore(Arrays.asList(
                                "&7Description",
                                "&7Description"
                        ))
                        .stack().create()
                )
        );
        this.categoryList.add(new ShopCategory(
                        "armor",
                        ItemStackCreator.using(Material.STONE).meta()
                                .display("&6Armor")
                                .lore(Arrays.asList(
                                        "&7Description",
                                        "&7Description"
                                ))
                                .stack().create()
                )
        );
        this.categoryList.add(new ShopCategory(
                        "tools", ItemStackCreator.using(Material.STONE).meta()
                        .display("&6Tools")
                        .lore(Arrays.asList(
                                "&7Description",
                                "&7Description"
                        ))
                        .stack().create()
                )
        );
        this.categoryList.add(new ShopCategory(
                        "ranged", ItemStackCreator.using(Material.STONE).meta()
                        .display("&6Ranged")
                        .lore(Arrays.asList(
                                "&7Description",
                                "&7Description"
                        ))
                        .stack().create()
                )
        );
        this.categoryList.add(new ShopCategory(
                        "potions", ItemStackCreator.using(Material.STONE).meta()
                        .display("&6Potions")
                        .lore(Arrays.asList(
                                "&7Description",
                                "&7Description"
                        ))
                        .stack().create()
                )
        );
        this.categoryList.add(new ShopCategory(
                        "utility", ItemStackCreator.using(Material.STONE).meta()
                        .display("&6Utility")
                        .lore(Arrays.asList(
                                "&7Description",
                                "&7Description"
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
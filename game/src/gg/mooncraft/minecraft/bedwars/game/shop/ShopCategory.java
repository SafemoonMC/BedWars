package gg.mooncraft.minecraft.bedwars.game.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Getter
@AllArgsConstructor
public final class ShopCategory {

    /*
    Fields
     */
    private final @NotNull String identifier;
    private final @NotNull ItemStack iconItem;
    private final @NotNull List<ShopElement> elementList;

    /*
    Constructor
     */
    public ShopCategory(@NotNull String identifier, @NotNull ItemStack iconItem) {
        this.identifier = identifier;
        this.iconItem = iconItem;
        this.elementList = new LinkedList<>();
    }

    /*
    Methods
     */
    public void addElement(@NotNull ShopElement shopElement) {
        this.elementList.add(shopElement);
    }

    @UnmodifiableView
    public @NotNull List<ShopElement> getElementList() {
        return Collections.unmodifiableList(this.elementList);
    }
}
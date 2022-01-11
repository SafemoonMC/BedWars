package gg.mooncraft.minecraft.bedwars.game.shop;

import lombok.Getter;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.items.ItemStackCreator;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.options.OptionEntry;

import java.util.List;

@Getter
public final class ShopElementItem extends ShopElement {

    /*
    Fields
     */
    private final @NotNull ItemStack itemStack;

    /*
    Constructor
     */
    public ShopElementItem(@NotNull String display, @NotNull String description, @NotNull OptionEntry<Material, Integer> cost, @NotNull Material displayIcon, @NotNull ItemStack itemStack) {
        super(display, description, cost, displayIcon);
        this.itemStack = itemStack;
    }

    /*
    Methods
     */
    public @NotNull ItemStack getItemStack() {
        if (itemStack.getType() == Material.POTION) {
            ItemStack clone = this.itemStack.clone();
            clone.addItemFlags(ItemFlag.values());
            return ItemStackCreator.using(clone).meta().display(ChatColor.WHITE + getDisplay()).lore(List.of(getDescription())).stack().create();
        }
        return this.itemStack;
    }

    /*
    Override Methods
     */
    @Override
    public @NotNull ItemStack getIconItem(@NotNull Player player, @NotNull GameMatchPlayer gameMatchPlayer) {
        ItemStack itemStack = super.getIconItem(player, gameMatchPlayer);
        if (this.itemStack.getItemMeta().hasEnchants()) {
            itemStack.addUnsafeEnchantments(this.itemStack.getEnchantments());
        }
        if (this.itemStack.getItemMeta() instanceof PotionMeta originalPotionMeta) {
            PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
            potionMeta.setColor(originalPotionMeta.getColor());
            itemStack.setItemMeta(potionMeta);
            itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        }
        itemStack.setAmount(this.itemStack.getAmount());
        return itemStack;
    }
}
package gg.mooncraft.minecraft.bedwars.game.items;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter(value = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemStackCreator {
    
    /*
    Fields
     */
    @Setter(value = AccessLevel.PACKAGE)
    private  @NotNull ItemStack itemStack;
    
    /*
    Methods
     */
    public ItemStackCreator type(Material material) {
        itemStack.setType(material);
        return this;
    }
    
    public ItemStackCreator amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }
    
    public ItemStackCreator glowing(boolean glowing) {
        if (itemStack.getItemFlags().contains(ItemFlag.HIDE_ENCHANTS) && itemStack.getEnchantments().containsKey(Enchantment.DURABILITY) && !glowing) {
            itemStack.removeEnchantment(Enchantment.DURABILITY);
            itemStack.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            return this;
        } else if (glowing) {
            itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }
    
    
    public ItemMetaCreator meta() {
        if (itemStack.getItemMeta() == null) throw new IllegalArgumentException("This ItemStack doesn't have an ItemMeta instance!");
        return ItemMetaCreator.using(this, itemStack.getItemMeta());
    }
    
    public <T extends ItemMetaCreator> T meta(Class<T> metaClass) {
        if (itemStack.getItemMeta() == null) throw new IllegalArgumentException("This ItemStack doesn't have an ItemMeta instance!");
        return ItemMetaCreator.using(this, itemStack.getItemMeta(), metaClass);
    }
    
    public ItemStack create() {
        return itemStack;
    }
    
    /*
    Static Methods
     */
    public static ItemStackCreator using(@NotNull ItemStack itemStack) {
        return new ItemStackCreator(itemStack);
    }
    
    public static ItemStackCreator using(@NotNull Material material) {
        return new ItemStackCreator(new ItemStack(material));
    }
    
    public static ItemStackCreator using(@NotNull ConfigurationSection configurationSection) {
        ItemStack itemStack;
        if (configurationSection.contains("material")) {
            Material material = Material.matchMaterial(configurationSection.getString("material"));
            if (material == null) throw new IllegalArgumentException("There is no material called " + configurationSection.getString("material"));
            itemStack = new ItemStack(material);
        } else if (configurationSection.contains("head-texture")) {
            String headTexture = configurationSection.getString("head-texture");
            itemStack = new ItemStack(Material.PLAYER_HEAD);
            
            UUID hashAsUniqueId = new UUID(headTexture.hashCode(), headTexture.hashCode());
            Bukkit.getUnsafe().modifyItemStack(itemStack, "{SkullOwner:{Id:\"" + hashAsUniqueId + "\",Properties:{textures:[{Value:\"" + headTexture + "\"}]}}}");
        } else throw new IllegalArgumentException("No ItemStack can be created from that ConfigurationSection!");
        ItemStackCreator itemStackCreator = new ItemStackCreator(itemStack).amount(configurationSection.getInt("amount", 1)).glowing(configurationSection.getBoolean("glowing", false));
        
        if (itemStack.getItemMeta() == null) return itemStackCreator;
        else return ItemMetaCreator.using(itemStackCreator, itemStack.getItemMeta(), configurationSection).stack();
    }
}
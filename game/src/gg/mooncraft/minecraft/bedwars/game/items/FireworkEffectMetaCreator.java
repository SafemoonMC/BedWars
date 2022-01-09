package gg.mooncraft.minecraft.bedwars.game.items;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class FireworkEffectMetaCreator extends ItemMetaCreator {
    
    /*
    Constructor
     */
    FireworkEffectMetaCreator(@NotNull ItemStackCreator itemStackCreator, @NotNull ItemMeta itemMeta) {
        super(itemStackCreator, itemMeta);
    }
    
    /*
    Methods
     */
    private FireworkEffectMeta getFireworkEffectMeta() {
        return (FireworkEffectMeta) getItemMeta();
    }
    
    public FireworkEffectMetaCreator color(Color color) {
        FireworkEffectMeta fireworkEffectMeta = getFireworkEffectMeta();
        FireworkEffect fireworkEffect = FireworkEffect.builder().withColor(color).build();
        fireworkEffectMeta.setEffect(fireworkEffect);
        getItemStackCreator().getItemStack().setItemMeta(fireworkEffectMeta);
        return this;
    }
    
    /*
    Static Methods
     */
    static ItemMetaCreator using(@NotNull ItemMetaCreator itemMetaCreator, @NotNull ConfigurationSection configurationSection) {
        FireworkEffectMetaCreator fireworkEffectMetaCreator = new FireworkEffectMetaCreator(itemMetaCreator.getItemStackCreator(), itemMetaCreator.getItemMeta());
        if (configurationSection.contains("color") && configurationSection.isString("color")) {
            String[] args = configurationSection.getString("color").split(",");
            fireworkEffectMetaCreator.color(Color.fromRGB(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2])));
        }
        return fireworkEffectMetaCreator;
    }
}
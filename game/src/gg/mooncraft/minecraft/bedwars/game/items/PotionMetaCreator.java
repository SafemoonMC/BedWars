package gg.mooncraft.minecraft.bedwars.game.items;

import org.bukkit.Color;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

public class PotionMetaCreator extends ItemMetaCreator {

    /*
    Constructor
     */
    PotionMetaCreator(@NotNull ItemStackCreator itemStackCreator, @NotNull ItemMeta itemMeta) {
        super(itemStackCreator, itemMeta);
    }

    /*
    Methods
     */
    private @NotNull PotionMeta getPotionMeta() {
        return (PotionMeta) getItemMeta();
    }

    public PotionMetaCreator color(@NotNull Color color) {
        getPotionMeta().setColor(color);
        return this;
    }

    public PotionMetaCreator type(@NotNull PotionType potionType) {
        getPotionMeta().setBasePotionData(new PotionData(potionType, false, false));
        return this;
    }

    public PotionMetaCreator effect(@NotNull PotionEffectType potionEffectType, int duration, int amplifier, boolean ambient, boolean particles, boolean icon) {
        getPotionMeta().addCustomEffect(new PotionEffect(potionEffectType, duration, amplifier, ambient, particles, icon), true);
        return this;
    }
}
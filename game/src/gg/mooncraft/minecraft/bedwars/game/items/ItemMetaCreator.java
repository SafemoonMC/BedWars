package gg.mooncraft.minecraft.bedwars.game.items;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.utilities.StringUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter(AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ItemMetaCreator {

    /*
    Fields
     */
    @NotNull
    private final ItemStackCreator itemStackCreator;
    @NotNull
    private final ItemMeta itemMeta;

    /*
    Methods
     */
    public ItemMetaCreator display(String display) {
        itemMeta.setDisplayName(StringUtilities.getColoredString(display));
        return this;
    }

    public ItemMetaCreator lore(List<String> lore) {
        return lore(lore, false);
    }

    public ItemMetaCreator lore(List<String> lore, boolean append) {
        List<String> finalLore = new LinkedList<>();
        for (String line : lore) {
            if (line.contains("\n")) finalLore.addAll(Arrays.asList(line.split("\n")));
            else finalLore.add(line);
        }
        if (append && itemMeta.hasLore()) {
            List<String> currentLore = new ArrayList<>();
            currentLore.addAll(itemMeta.getLore());
            currentLore.addAll(StringUtilities.getColoredStringList(finalLore));
            itemMeta.setLore(currentLore);
        } else itemMeta.setLore(StringUtilities.getColoredStringList(finalLore));

        return this;
    }

    public ItemMetaCreator model(int customModelData) {
        itemMeta.setCustomModelData(customModelData);
        return this;
    }

    public ItemMetaCreator placeholders(Function<String, String> placeholdersReplacerFunction) {
        if (itemMeta.hasDisplayName())
            display(placeholdersReplacerFunction.apply(itemMeta.getDisplayName()));
        if (itemMeta.hasLore())
            lore(itemMeta.getLore().stream().map(placeholdersReplacerFunction).collect(Collectors.toList()));
        return this;
    }

    public ItemStackCreator stack() {
        itemStackCreator.getItemStack().setItemMeta(itemMeta);
        return itemStackCreator;
    }

    /*
    Static Methods
     */
    static ItemMetaCreator using(@NotNull ItemStackCreator itemStackCreator, @NotNull ItemMeta itemMeta) {
        return new ItemMetaCreator(itemStackCreator, itemMeta);
    }

    static ItemMetaCreator using(@NotNull ItemStackCreator itemStackCreator, @NotNull ItemMeta itemMeta, @NotNull ConfigurationSection configurationSection) {
        ItemMetaCreator itemMetaCreator = using(itemStackCreator, itemMeta);
        if (configurationSection.contains("display") && configurationSection.isString("display"))
            itemMetaCreator.display(configurationSection.getString("display"));
        if (configurationSection.contains("lore") && configurationSection.isList("lore"))
            itemMetaCreator.lore(configurationSection.getStringList("lore"));
        if (configurationSection.contains("model") && configurationSection.isInt("model"))
            itemMetaCreator.model(configurationSection.getInt("model"));

        if (itemMeta instanceof FireworkEffectMeta)
            return FireworkEffectMetaCreator.using(itemMetaCreator, configurationSection);
        else return itemMetaCreator;
    }

    @SuppressWarnings("unchecked")
    static <T extends ItemMetaCreator> @NotNull T using(@NotNull ItemStackCreator itemStackCreator, @NotNull ItemMeta itemMeta, @NotNull Class<T> metaCreatorClass) {
        if (ItemMetaCreator.class.equals(metaCreatorClass))
            return (T) new ItemMetaCreator(itemStackCreator, itemMeta);
        try {
            return metaCreatorClass.getDeclaredConstructor(new Class[]{ItemStackCreator.class, ItemMeta.class}).newInstance(itemStackCreator, itemMeta);
        } catch (Exception e) {
            throw new IllegalArgumentException("That class is not a valid ItemMetaCreator child! Exception: " + e.getMessage());
        }
    }
}
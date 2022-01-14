package gg.mooncraft.minecraft.bedwars.game.upgrades;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import gg.mooncraft.minecraft.bedwars.game.match.options.OptionEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class UpgradeSet {

    /*
    Fields
     */
    private final @NotNull List<UpgradeCategory> upgradeCategoryList;

    /*
    Constructor
     */
    public UpgradeSet() {
        this.upgradeCategoryList = new ArrayList<>();
        this.upgradeCategoryList.add(new UpgradeCategory(
                "weapon",
                "Sharpness Swords",
                "&7Your team permanentely gains\n&7Sharpness I on all swords and\n&7axes!"
        ));
        this.upgradeCategoryList.add(new UpgradeCategory(
                "armor",
                "Reinforced Armor",
                "&7Your team permanently gains\n&7Protection on all armor pieces!"
        ));
        this.upgradeCategoryList.add(new UpgradeCategory(
                "miner",
                "Maniac Miner",
                "&7All players on your team\n&7permanently gain Haste."
        ));
        this.upgradeCategoryList.add(new UpgradeCategory(
                "furnace",
                "Iron Forge",
                "&7Upgrade resource spawning on\n&7your island."
        ));
        this.upgradeCategoryList.add(new UpgradeCategory(
                "healpool",
                "Heal Pool",
                "&7Creates a Regeneration field\n&7around your bed in a\n&713 blocks radius."
        ));
        this.upgradeCategoryList.add(new UpgradeCategory(
                "dragon",
                "Dragon Buff",
                "&7Your team will have 2 dragons\n&7instead of 1 during deathmatch!"
        ));
    }

    /*
    Methods
     */
    public void addItem(@NotNull String identifier, int tier, @NotNull String description, @NotNull OptionEntry<Material, Integer> costEntry, @NotNull Material iconMaterial) {
        getGroup(identifier).ifPresent(upgradeCategory -> upgradeCategory.addUpgrade(new UpgradeElement(upgradeCategory, tier, description, costEntry, iconMaterial)));
    }

    public @NotNull Optional<UpgradeCategory> getGroup(@NotNull String identifier) {
        return this.upgradeCategoryList.stream().filter(upgradeCategory -> upgradeCategory.getIdentifier().equalsIgnoreCase(identifier)).findFirst();
    }

    @UnmodifiableView
    public @NotNull List<UpgradeCategory> getGroupList() {
        return Collections.unmodifiableList(this.upgradeCategoryList);
    }
}
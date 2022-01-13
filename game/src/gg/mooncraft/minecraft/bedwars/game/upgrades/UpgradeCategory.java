package gg.mooncraft.minecraft.bedwars.game.upgrades;

import lombok.Getter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public final class UpgradeCategory {

    /*
    Fields
     */
    private final @NotNull String identifier;
    private final @NotNull String display;
    private final @NotNull String description;
    private final @NotNull List<UpgradeElement> upgradeElementList;

    /*
    Constructor
     */
    public UpgradeCategory(@NotNull String identifier, @NotNull String display, @NotNull String description) {
        this.identifier = identifier;
        this.display = display;
        this.description = description;
        this.upgradeElementList = new ArrayList<>();
    }

    /*
    Methods
     */
    public void addUpgrade(@NotNull UpgradeElement upgradeElement) {
        this.upgradeElementList.add(upgradeElement);
    }

    public @NotNull Optional<UpgradeElement> getUpgrade(int tier) {
        return this.upgradeElementList.stream().filter(upgradeElement -> upgradeElement.getTier() == tier).findFirst();
    }

    public boolean isSingleTier() {
        return this.upgradeElementList.size() == 1;
    }
}
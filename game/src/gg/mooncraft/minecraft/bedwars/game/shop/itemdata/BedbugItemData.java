package gg.mooncraft.minecraft.bedwars.game.shop.itemdata;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameTeam;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.items.persistence.PersistentDataItem;

import java.util.UUID;

public class BedbugItemData extends PersistentDataItem<BedbugItem> {

    /*
    Constants
     */
    public static final BedbugItemData INSTANCE = new BedbugItemData();

    /*
    Constructor
     */
    protected BedbugItemData() {
        super(BedWarsPlugin.getInstance(), new NamespacedKey(BedWarsPlugin.getInstance(), "BEDWARS-BEDBUG"));
    }

    /*
    Override Methods
     */
    @Override
    public @NotNull PersistentDataContainer toContainer(@NotNull BedbugItem value, @NotNull PersistentDataContainer persistentDataContainer) {
        persistentDataContainer.set(key("unique-id"), PersistentDataType.STRING, value.getUniqueId().toString());
        persistentDataContainer.set(key("game-team"), PersistentDataType.STRING, value.getGameTeam().name());
        return persistentDataContainer;
    }

    @Override
    public BedbugItem fromContainer(@NotNull PersistentDataContainer persistentDataContainer) {
        UUID uniqueId = UUID.fromString(persistentDataContainer.get(key("unique-id"), PersistentDataType.STRING));
        GameTeam gameTeam = GameTeam.valueOf(persistentDataContainer.get(key("game-team"), PersistentDataType.STRING));
        return new BedbugItem(uniqueId, gameTeam);
    }
}

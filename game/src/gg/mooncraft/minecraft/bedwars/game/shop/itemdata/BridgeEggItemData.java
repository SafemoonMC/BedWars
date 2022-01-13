package gg.mooncraft.minecraft.bedwars.game.shop.itemdata;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameTeam;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.items.persistence.PersistentDataItem;

import java.util.UUID;

public class BridgeEggItemData extends PersistentDataItem<BridgeEggItem> {

    /*
    Constants
     */
    public static final BridgeEggItemData INSTANCE = new BridgeEggItemData();

    /*
    Constructor
     */
    protected BridgeEggItemData() {
        super(BedWarsPlugin.getInstance(), new NamespacedKey(BedWarsPlugin.getInstance(), "BEDWARS-BRIDGEEGG"));
    }

    /*
    Override Methods
     */
    @Override
    public @NotNull PersistentDataContainer toContainer(@NotNull BridgeEggItem value, @NotNull PersistentDataContainer persistentDataContainer) {
        persistentDataContainer.set(key("unique-id"), PersistentDataType.STRING, value.getUniqueId().toString());
        persistentDataContainer.set(key("game-team"), PersistentDataType.STRING, value.getGameTeam().name());
        return persistentDataContainer;
    }

    @Override
    public BridgeEggItem fromContainer(@NotNull PersistentDataContainer persistentDataContainer) {
        UUID uniqueId = UUID.fromString(persistentDataContainer.get(key("unique-id"), PersistentDataType.STRING));
        GameTeam gameTeam = GameTeam.valueOf(persistentDataContainer.get(key("game-team"), PersistentDataType.STRING));
        return new BridgeEggItem(uniqueId, gameTeam);
    }
}

package gg.mooncraft.minecraft.bedwars.game.items;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SkullMetaCreator extends ItemMetaCreator {
    
    /*
    Constructor
     */
    SkullMetaCreator(@NotNull ItemStackCreator itemStackCreator, @NotNull ItemMeta itemMeta) {
        super(itemStackCreator, itemMeta);
    }
    
    /*
    Methods
     */
    private SkullMeta getSkullMeta() {
        return (SkullMeta) getItemMeta();
    }
    
    public SkullMetaCreator owner(OfflinePlayer offlinePlayer) {
        getSkullMeta().setOwningPlayer(offlinePlayer);
        return this;
    }
    
    public SkullMetaCreator owner(String headTexture) {
        UUID hashAsUniqueId = new UUID(headTexture.hashCode(), headTexture.hashCode());
        getItemStackCreator().setItemStack(Bukkit.getUnsafe().modifyItemStack(getItemStackCreator().getItemStack(), "{SkullOwner:{Id:\"" + hashAsUniqueId + "\",Properties:{textures:[{Value:\"" + headTexture + "\"}]}}}"));
        return this;
    }
}
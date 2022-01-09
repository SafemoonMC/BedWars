package gg.mooncraft.minecraft.bedwars.game.items.persistence;

import lombok.Getter;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public abstract class PersistentDataItem<T> {
    
    /*
    Fields
     */
    private final Plugin plugin;
    @Getter
    private final NamespacedKey namespacedKey;
    @Getter
    private final PersistentDataType<PersistentDataContainer, T> persistentDataType;
    
    /*
    Constructor
     */
    public PersistentDataItem(Plugin plugin, NamespacedKey namespacedKey) {
        this.plugin = plugin;
        this.namespacedKey = namespacedKey;
        this.persistentDataType = new PersistentDataType<PersistentDataContainer, T>() {
            @NotNull
            @Override
            public Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }
            
            @NotNull
            @Override
            @SuppressWarnings("unchecked")
            public Class<T> getComplexType() {
                return (Class<T>) this.getClass();
            }
            
            @NotNull
            @Override
            public PersistentDataContainer toPrimitive(@NotNull T t, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
                PersistentDataContainer persistentDataContainer = persistentDataAdapterContext.newPersistentDataContainer();
                return toContainer(t, persistentDataContainer);
            }
            
            @NotNull
            @Override
            public T fromPrimitive(@NotNull PersistentDataContainer persistentDataContainer, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
                return fromContainer(persistentDataContainer);
            }
        };
    }
    
    /*
    Abstract Methods
     */
    public abstract PersistentDataContainer toContainer(T value, PersistentDataContainer persistentDataContainer);
    public abstract T fromContainer(PersistentDataContainer persistentDataContainer);
    
    /*
    Methods
     */
    protected NamespacedKey key(String key) {
        return new NamespacedKey(plugin, key);
    }
}
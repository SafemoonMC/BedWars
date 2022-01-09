package gg.mooncraft.minecraft.bedwars.game.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.google.common.base.Preconditions;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemUtilities {
    
    public static String serializeInventory(PlayerInventory playerInventory) {
        // This contains contents, armor and offhand (contents are indexes 0 - 35, armor 36 - 39, offhand - 40)
        return serializeItemStackArray(playerInventory.getContents());
    }
    
    public static String serializeItemStackArray(ItemStack... itemStackArray) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream)) {
            bukkitObjectOutputStream.writeInt(itemStackArray.length);
            
            for (ItemStack itemStack : itemStackArray) {
                if (itemStack != null) bukkitObjectOutputStream.writeObject(itemStack.serializeAsBytes());
                else bukkitObjectOutputStream.writeObject(null);
            }
            return Base64Coder.encodeLines(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("The array of items cannot be serialized!", e);
        }
    }
    
    public static String serializeItemStack(ItemStack itemStack) {
        Preconditions.checkNotNull(itemStack, "NULL item cannot be serialized.");
        Preconditions.checkArgument(itemStack.getType() != Material.AIR, "AIR item cannot be serialized.");
        
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream)) {
            bukkitObjectOutputStream.writeObject(itemStack.serializeAsBytes());
            return Base64Coder.encodeLines(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("The item cannot be serialized!", e);
        }
    }
    
    public static ItemStack[] deserializeItemStackArray(String serializedData) {
        if (serializedData == null || serializedData.isEmpty()) return null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(serializedData)); BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream)) {
            ItemStack[] itemStackArray = new ItemStack[bukkitObjectInputStream.readInt()];
            for (int i = 0; i < itemStackArray.length; i++) {
                byte[] itemStackBytes = (byte[]) bukkitObjectInputStream.readObject();
                if (itemStackBytes != null) itemStackArray[i] = ItemStack.deserializeBytes(itemStackBytes);
                else itemStackArray[i] = null;
            }
            return itemStackArray;
        } catch (Exception e) {
            throw new IllegalStateException("The serialized data cannot be deserialized into an ItemStack array!", e);
        }
    }
    
    public static ItemStack deserializeItemStack(String serializedData) {
        if (serializedData == null || serializedData.isEmpty()) return null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(serializedData)); BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream)) {
            byte[] itemStackBytes = (byte[]) bukkitObjectInputStream.readObject();
            if (itemStackBytes == null) return null;
            return ItemStack.deserializeBytes(itemStackBytes);
        } catch (Exception e) {
            throw new IllegalStateException("The serialized data cannot be deserialized into an ItemStack!", e);
        }
    }
    
    public static String serializeItemStackText(ItemStack itemStack) {
        if (itemStack == null) return null;
        if (itemStack.getType() == Material.AIR) return null;
        
        // ItemStack methods to get a net.minecraft.server.ItemStack object for serialization
        Class<?> craftItemStackClazz = ReflectionUtilities.getOBCClass("inventory.CraftItemStack");
        Method asNMSCopyMethod = ReflectionUtilities.getMethod(craftItemStackClazz, "asNMSCopy", ItemStack.class);
        
        // NMS Method to serialize a net.minecraft.server.ItemStack to a valid Json string
        Class<?> nmsItemStackClazz = ReflectionUtilities.getNMSClass("ItemStack");
        Class<?> nbtTagCompoundClazz = ReflectionUtilities.getNMSClass("NBTTagCompound");
        Method saveNmsItemStackMethod = ReflectionUtilities.getMethod(nmsItemStackClazz, "save", nbtTagCompoundClazz);
        
        Object nmsNbtTagCompoundObj; // This will just be an empty NBTTagCompound instance to invoke the saveNms method
        Object nmsItemStackObj; // This is the net.minecraft.server.ItemStack object received from the asNMSCopy method
        Object itemAsJsonObject; // This is the net.minecraft.server.ItemStack after being put through saveNmsItem method
        
        try {
            nmsNbtTagCompoundObj = Objects.requireNonNull(nbtTagCompoundClazz).newInstance();
            nmsItemStackObj = Objects.requireNonNull(asNMSCopyMethod).invoke(null, itemStack);
            itemAsJsonObject = Objects.requireNonNull(saveNmsItemStackMethod).invoke(nmsItemStackObj, nmsNbtTagCompoundObj);
        } catch (Exception e) {
            throw new IllegalStateException("The item cannot be serialized to an NMS item.", e);
        }
        
        // Return a BaseComponent array representation of the serialized object
        return itemAsJsonObject.toString();
    }
}
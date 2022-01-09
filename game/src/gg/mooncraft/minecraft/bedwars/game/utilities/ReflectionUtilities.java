package gg.mooncraft.minecraft.bedwars.game.utilities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReflectionUtilities {
    
    /*
    Fields
     */
    @Getter
    private static final String version;
    
    static {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        version = name.substring(name.lastIndexOf('.') + 1) + ".";
    }

    public static Class<?> getNMSClass(String nmsClassName) {
        try {
            String clazzName = "net.minecraft.server." + getVersion() + nmsClassName;
            return Class.forName(clazzName);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
    
    public synchronized static Class<?> getOBCClass(String obcClassName) {
        try {
            String clazzName = "org.bukkit.craftbukkit." + getVersion() + obcClassName;
            return Class.forName(clazzName);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
    
    public static Object getConnection(Player player) {
        Method getHandleMethod = getMethod(player.getClass(), "getHandle");
        
        if (getHandleMethod != null) {
            try {
                Object nmsPlayer = getHandleMethod.invoke(player);
                Field playerConField = getField(nmsPlayer.getClass(), "playerConnection");
                if (playerConField == null) return null;
                return playerConField.get(nmsPlayer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return null;
    }
    
    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... params) {
        try {
            return clazz.getConstructor(params);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
    
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) {
        try {
            return clazz.getMethod(methodName, params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getField(fieldName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
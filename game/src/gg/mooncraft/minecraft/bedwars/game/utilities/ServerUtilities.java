package gg.mooncraft.minecraft.bedwars.game.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class ServerUtilities {

    public static @Nullable String getProperty(@NotNull String key) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("server.properties"))) {
            Properties properties = new Properties();
            properties.load(bufferedReader);
            return properties.getProperty(key);
        } catch (Exception e) {
            return null;
        }
    }
}
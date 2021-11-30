package gg.mooncraft.minecraft.bedwars.common.utilities.gson;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class GsonProvider {

    /*
    Constants
     */
    private static final Gson NORMAL = new GsonBuilder().disableHtmlEscaping().create();
    private static final Gson PRETTY_PRINTING = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    /*
    Static Methods
     */
    public static @NotNull Gson normal() {
        return NORMAL;
    }

    public static @NotNull Gson prettyPrinting() {
        return PRETTY_PRINTING;
    }
}
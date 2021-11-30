package gg.mooncraft.minecraft.bedwars.common.utilities.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class JsonObjectWrapper implements JsonElementWrapper {

    /*
    Fields
     */
    private final @NotNull JsonObject object = new JsonObject();

    /*
    Override Methods
     */
    @Override
    public @NotNull JsonObject toJson() {
        return object;
    }

    /*
    Methods
     */
    public JsonObjectWrapper add(@NotNull String key, JsonElement value) {
        this.object.add(key, value);
        return this;
    }

    public JsonObjectWrapper add(@NotNull String key, String value) {
        if (value == null) {
            return add(key, JsonNull.INSTANCE);
        }
        return add(key, new JsonPrimitive(value));
    }

    public @NotNull JsonObjectWrapper add(@NotNull String key, Number value) {
        if (value == null) {
            return add(key, JsonNull.INSTANCE);
        }
        return add(key, new JsonPrimitive(value));
    }

    public @NotNull JsonObjectWrapper add(@NotNull String key, Boolean value) {
        if (value == null) {
            return add(key, JsonNull.INSTANCE);
        }
        return add(key, new JsonPrimitive(value));
    }

    public @NotNull JsonObjectWrapper add(@NotNull String key, JsonElementWrapper value) {
        if (value == null) {
            return add(key, JsonNull.INSTANCE);
        }
        return add(key, value.toJson());
    }

    public @NotNull JsonObjectWrapper add(@NotNull String key, @NotNull Supplier<? extends JsonElementWrapper> value) {
        return add(key, value.get().toJson());
    }

    @Contract("_ -> this")
    public @NotNull JsonObjectWrapper consume(@NotNull Consumer<? super JsonObjectWrapper> consumer) {
        consumer.accept(this);
        return this;
    }
}
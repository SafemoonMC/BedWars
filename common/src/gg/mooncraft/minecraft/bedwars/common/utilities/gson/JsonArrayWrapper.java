package gg.mooncraft.minecraft.bedwars.common.utilities.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class JsonArrayWrapper implements JsonElementWrapper {

    /*
    Fields
     */
    private final @NotNull JsonArray object = new JsonArray();

    /*
    Override Methods
     */
    @Override
    public @NotNull JsonArray toJson() {
        return object;
    }

    /*
    Methods
     */
    public JsonArrayWrapper add(@NotNull JsonElement value) {
        this.object.add(value);
        return this;
    }

    public JsonArrayWrapper add(@NotNull String value) {
        if (value == null) {
            return add(JsonNull.INSTANCE);
        }
        return add(new JsonPrimitive(value));
    }

    public @NotNull JsonArrayWrapper add(@NotNull Number value) {
        if (value == null) {
            return add(JsonNull.INSTANCE);
        }
        return add(new JsonPrimitive(value));
    }

    public @NotNull JsonArrayWrapper add(@NotNull Boolean value) {
        if (value == null) {
            return add(JsonNull.INSTANCE);
        }
        return add(new JsonPrimitive(value));
    }

    public @NotNull JsonArrayWrapper add(@NotNull JsonElementWrapper value) {
        if (value == null) {
            return add(JsonNull.INSTANCE);
        }
        return add(value.toJson());
    }

    public @NotNull JsonArrayWrapper add(@NotNull Supplier<? extends JsonElementWrapper> value) {
        return add(value.get().toJson());
    }

    @Contract("_ -> this")
    public @NotNull JsonArrayWrapper consume(@NotNull Consumer<? super JsonArrayWrapper> consumer) {
        consumer.accept(this);
        return this;
    }
}
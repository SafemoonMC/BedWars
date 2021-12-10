package gg.mooncraft.minecraft.bedwars.common.messaging;

import lombok.AccessLevel;
import lombok.Getter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gg.mooncraft.minecraft.bedwars.common.ComplexJavaPlugin;
import gg.mooncraft.minecraft.bedwars.common.messaging.consumer.IncomingMessageConsumer;
import gg.mooncraft.minecraft.bedwars.common.messaging.message.OutgoingMessage;
import gg.mooncraft.minecraft.bedwars.common.utilities.gson.GsonProvider;
import gg.mooncraft.minecraft.bedwars.common.utilities.gson.JsonObjectWrapper;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class RedisMessenger implements Messenger {

    /*
    Fields
     */
    @Getter(value = AccessLevel.PACKAGE)
    private final @NotNull ComplexJavaPlugin plugin;
    @Getter(value = AccessLevel.PACKAGE)
    private final @Nullable IncomingMessageConsumer consumer;

    @Getter(value = AccessLevel.PACKAGE)
    private @Nullable RedisChannel incomingChannel;


    @Getter(value = AccessLevel.PACKAGE)
    private @Nullable JedisPool jedisPool;
    private @Nullable RedisSubscription subscription;

    /*
    Constructor
     */
    public RedisMessenger(@NotNull ComplexJavaPlugin plugin, @Nullable IncomingMessageConsumer consumer) {
        this.plugin = plugin;
        this.consumer = consumer;
    }

    /*
    Methods
     */
    public void init(@NotNull RedisChannel incomingChannel, @NotNull JedisPoolConfig jedisPoolConfig, @NotNull HostAndPort hostAndPort, @NotNull String username, @NotNull String password) {
        this.incomingChannel = incomingChannel;
        this.jedisPool = new JedisPool(jedisPoolConfig, hostAndPort.getHost(), hostAndPort.getPort(), username, password);
        this.subscription = new RedisSubscription(this);
        this.plugin.getScheduler().executeAsync(this.subscription);
    }

    public void stop() {
        if (this.subscription != null) this.subscription.unsubscribe();
        if (this.jedisPool != null) this.jedisPool.destroy();
    }

    public boolean isClosed() {
        return jedisPool == null || this.subscription == null || this.jedisPool.isClosed();
    }

    /*
    Override Methods
     */
    @Override
    public @NotNull CompletableFuture<Boolean> sendMessage(@NotNull RedisChannel redisChannel, @NotNull OutgoingMessage outgoingMessage) {
        if (isClosed()) return CompletableFuture.completedFuture(false);
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.publish(redisChannel.getChannel(), outgoingMessage.asJsonString());
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    @Override
    public @NotNull CompletableFuture<Boolean> addKeyValue(@NotNull String key, @NotNull String value) {
        if (isClosed()) return CompletableFuture.completedFuture(false);
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.set(key, value);
                return true;
            }
        });
    }

    @Override
    public @NotNull CompletableFuture<Boolean> delKeyValue(@NotNull String key) {
        if (isClosed()) return CompletableFuture.completedFuture(false);
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.del(key);
                return true;
            }
        });
    }

    @Override
    public @NotNull CompletableFuture<String> getKeyValue(@NotNull String key) {
        if (isClosed()) return CompletableFuture.completedFuture(null);
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.get(key);
            }
        });
    }

    /*
    Static Methods
     */
    public static @NotNull String encodeMessageAsJson(@NotNull String type, @NotNull UUID uniqueId, @Nullable JsonElement content) {
        JsonObject json = new JsonObjectWrapper()
                .add("type", type)
                .add("unique-id", uniqueId.toString())
                .consume(jsonObjectWrapper -> {
                    if (jsonObjectWrapper == null) return;
                    jsonObjectWrapper.add("content", content);
                })
                .toJson();
        return GsonProvider.normal().toJson(json);
    }
}
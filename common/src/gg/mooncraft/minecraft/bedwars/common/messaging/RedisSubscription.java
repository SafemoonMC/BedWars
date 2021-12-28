package gg.mooncraft.minecraft.bedwars.common.messaging;

import org.jetbrains.annotations.NotNull;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public final class RedisSubscription extends JedisPubSub implements Runnable {

    /*
    Fields
     */
    private final @NotNull RedisMessenger redisMessenger;

    /*
    Constructor
     */
    public RedisSubscription(@NotNull RedisMessenger redisMessenger) {
        this.redisMessenger = redisMessenger;
    }

    /*
    Override Methods
     */
    @Override
    public void run() {
        boolean wasBroken = false;
        while (!Thread.interrupted() && !redisMessenger.isClosed()) {
            try (Jedis jedis = redisMessenger.getJedisPool().getResource()) {
                if (wasBroken) {
                    redisMessenger.getPlugin().getLogger().info("Redis pub/sub connection re-established.");
                    wasBroken = false;
                }
                jedis.subscribe(this, redisMessenger.getIncomingChannel().getChannel());
            } catch (Exception e) {
                wasBroken = true;
                redisMessenger.getPlugin().getLogger().warning("Redis pub/sub connection dropped, trying to re-open the connection. Error: " + e.getMessage());
                try {
                    unsubscribe();
                } catch (Exception ignored) {
                }

                // Sleep for 5 seconds to prevent spam in console
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override
    public void onMessage(@NotNull String channel, @NotNull String message) {
        if (redisMessenger.getConsumer() == null) return;
        if (redisMessenger.getIncomingChannel() == null) return;
        if (!channel.equals(redisMessenger.getIncomingChannel().getChannel())) return;
        try {
            redisMessenger.getConsumer().consumeIncomingMessageAsString(message);
        } catch (Exception e) {
            redisMessenger.getPlugin().getLogger().severe("[Redis Pub/Sub] Error onMessage: ");
            e.printStackTrace();
        }
    }
}
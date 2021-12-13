package gg.mooncraft.minecraft.bedwars.lobby.utilities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.lobby.BedWarsPlugin;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class BungeeUtilities {

    @SuppressWarnings("UnstableApiUsage")
    public static void teleportPlayer(@NotNull Player player, @NotNull String serverName) {
        if (!Bukkit.getMessenger().isOutgoingChannelRegistered(BedWarsPlugin.getInstance(), "BungeeCord")) {
            Bukkit.getMessenger().registerOutgoingPluginChannel(BedWarsPlugin.getInstance(), "BungeeCord");
            BedWarsPlugin.getInstance().getLogger().info("BungeeCord outgoing plugin channel has been registered now.");
        }

        ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
        byteArrayDataOutput.writeUTF("Connect");
        byteArrayDataOutput.writeUTF(serverName);

        player.sendPluginMessage(BedWarsPlugin.getInstance(), "BungeeCord", byteArrayDataOutput.toByteArray());
    }
}
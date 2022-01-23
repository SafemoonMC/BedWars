package gg.mooncraft.minecraft.bedwars.lobby.managers;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.common.messages.GameRequestMessage;
import gg.mooncraft.minecraft.bedwars.common.messaging.RedisChannel;
import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.lobby.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.lobby.utilities.BungeeUtilities;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public final class Matchmaking {

    /*
    Fields
     */
    private final @NotNull GameMode gameMode;
    private final @NotNull List<Player> playerList;

    /*
    Methods
     */
    public void process() {
        BedWarsPlugin.getInstance().getLogger().info("[Matchmaking] Processing " + this.gameMode.name() + " request for (" + this.playerList.stream().map(Player::getName).collect(Collectors.joining(", ")) + ").");
        BedWarsPlugin.getInstance().getGameServerManager().getGameServer(gameMode, this.playerList.size()).ifPresent(gameServer -> {
            GameRequestMessage gameRequestMessage = new GameRequestMessage(new GameRequestMessage.GameRequest(gameServer.getServerName(), gameMode, this.playerList.stream().map(Player::getUniqueId).collect(Collectors.toList())));
            BedWarsPlugin.getInstance().getMessenger().sendMessage(RedisChannel.GAME, gameRequestMessage).thenAccept(update -> {
                if (!update) return;
                String playerListText = this.playerList.stream().map(Player::getName).collect(Collectors.joining(", "));
                this.playerList.forEach(player -> {
                    BungeeUtilities.teleportPlayer(player, gameServer.getServerName());
                });
                BedWarsPlugin.getInstance().getLogger().info("Teleporting " + playerListText + " to " + gameServer.getServerName() + ". Request: " + gameRequestMessage.getUniqueId().toString());
            });
        });
    }
}
package gg.mooncraft.minecraft.bedwars.lobby.handlers.commands;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.mojang.brigadier.arguments.StringArgumentType;

import me.eduardwayland.mooncraft.waylander.command.LiteralCommand;
import me.eduardwayland.mooncraft.waylander.command.arguments.PlayerType;
import me.eduardwayland.mooncraft.waylander.command.builders.LiteralCommandBuilder;
import me.eduardwayland.mooncraft.waylander.command.builders.RequiredCommandBuilder;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import gg.mooncraft.minecraft.bedwars.common.messages.GameRequestMessage;
import gg.mooncraft.minecraft.bedwars.common.messaging.RedisChannel;
import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.lobby.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.lobby.utilities.BungeeUtilities;
import gg.mooncraft.minecraft.bedwars.lobby.utilities.PartyUtilities;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class Commands {

    public static void loadAll() {
        LiteralCommand<?> command = LiteralCommandBuilder
                .name("bedwars").aliases(new String[]{"bw"})
                .then(LiteralCommandBuilder
                        .name("play")
                        .then(RequiredCommandBuilder
                                .name("mode", StringArgumentType.word())
                                .then(RequiredCommandBuilder
                                        .name("player", new PlayerType())
                                        .executes(((sender, arguments) -> {
                                            Player player = Bukkit.getPlayer(arguments.getArgument("player", String.class));
                                            if (player == null) return;
                                            GameMode gameMode = GameMode.valueOf(arguments.getArgument("mode", String.class));
                                            PartyUtilities.getParty(player).handle((partyData, throwable) -> {
                                                if (throwable != null) return null;
                                                return partyData;
                                            }).thenAccept(partyData -> {
                                                if (partyData == null) {
                                                    BedWarsPlugin.getInstance().getGameServerManager().getGameServer(gameMode, 1).ifPresent(gameServer -> {
                                                        GameRequestMessage gameRequestMessage = new GameRequestMessage(new GameRequestMessage.GameRequest(gameServer.getServerName(), gameMode, List.of(player.getUniqueId())));
                                                        BedWarsPlugin.getInstance().getMessenger().sendMessage(RedisChannel.GAME, gameRequestMessage).thenAccept(update -> {
                                                            if (!update) return;
                                                            BungeeUtilities.teleportPlayer(player, gameServer.getServerName());
                                                        });
                                                    });
                                                    return;
                                                }
                                                List<UUID> playerList = PartyUtilities.getPartyMembers(partyData, true).stream().map(Player::getUniqueId).collect(Collectors.toList());
                                                BedWarsPlugin.getInstance().getGameServerManager().getGameServer(gameMode, playerList.size()).ifPresent(gameServer -> {
                                                    GameRequestMessage gameRequestMessage = new GameRequestMessage(new GameRequestMessage.GameRequest(gameServer.getServerName(), gameMode, playerList));
                                                    BedWarsPlugin.getInstance().getMessenger().sendMessage(RedisChannel.GAME, gameRequestMessage).thenAccept(update -> {
                                                        if (!update) return;
                                                        playerList.forEach(uniqueId -> {
                                                            Player target = Bukkit.getPlayer(uniqueId);
                                                            if (target == null) return;
                                                            BungeeUtilities.teleportPlayer(target, gameServer.getServerName());
                                                        });
                                                    });
                                                });
                                            });
                                        }))
                                )
                        )
                )
                .build();

        BedWarsPlugin.getInstance().registerCommand(command);
        BedWarsPlugin.getInstance().getLogger().info("Commands have been loaded.");
    }
}
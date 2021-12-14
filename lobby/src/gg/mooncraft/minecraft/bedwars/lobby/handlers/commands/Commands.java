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

import gg.mooncraft.minecraft.bedwars.lobby.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.lobby.utilities.PartyUtilities;

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
                                            PartyUtilities.getParty(player).handle((partyData, throwable) -> {
                                                if (throwable == null) return null;
                                                return partyData;
                                            }).thenAccept(partyData -> {
                                                if (partyData == null) {
                                                    //TODO teleport only player
                                                    return;
                                                }
                                                //TODO teleport player and members;
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
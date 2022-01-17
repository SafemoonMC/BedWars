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

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.lobby.BedWarsPlugin;

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
                                            BedWarsPlugin.getInstance().getMatchmakingManager().playMatchmaking(gameMode, player);
                                        }))
                                )
                        )
                )
                .build();

        BedWarsPlugin.getInstance().registerCommand(command);
        BedWarsPlugin.getInstance().getLogger().info("Commands have been loaded.");
    }
}
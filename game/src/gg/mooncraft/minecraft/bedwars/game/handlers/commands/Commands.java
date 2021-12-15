package gg.mooncraft.minecraft.bedwars.game.handlers.commands;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.mojang.brigadier.arguments.StringArgumentType;

import me.eduardwayland.mooncraft.waylander.command.LiteralCommand;
import me.eduardwayland.mooncraft.waylander.command.builders.LiteralCommandBuilder;
import me.eduardwayland.mooncraft.waylander.command.builders.RequiredCommandBuilder;

import org.bukkit.entity.Player;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class Commands {

    public static void loadAll() {
        LiteralCommand<?> command = LiteralCommandBuilder
                .name("bedwars").aliases("bw")
                .then(LiteralCommandBuilder.name("create")
                        .then(RequiredCommandBuilder
                                .<Player, String>name("map-name", StringArgumentType.word())
                                .executes((sender, arguments) -> {
                                    String mapName = arguments.getArgument("map-name", String.class);
                                    if (!BedWarsPlugin.getInstance().getMapManager().getWorldsMap().containsKey(mapName)) {
                                        sender.sendMessage("There is already a map with that name.");
                                        return;
                                    }
                                    BedWarsPlugin.getInstance().getSetupManager().startSetup(sender, mapName);
                                    sender.sendMessage("Setup mode has been started for " + mapName + ".");
                                })
                        )
                )
                .build();

        BedWarsPlugin.getInstance().registerCommand(command);
        BedWarsPlugin.getInstance().getLogger().info("Commands have been loaded.");
    }
}
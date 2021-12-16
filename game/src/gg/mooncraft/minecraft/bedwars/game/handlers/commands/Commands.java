package gg.mooncraft.minecraft.bedwars.game.handlers.commands;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.mojang.brigadier.arguments.StringArgumentType;

import me.eduardwayland.mooncraft.waylander.command.LiteralCommand;
import me.eduardwayland.mooncraft.waylander.command.builders.LiteralCommandBuilder;
import me.eduardwayland.mooncraft.waylander.command.builders.RequiredCommandBuilder;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class Commands {

    public static void loadAll() {
        LiteralCommand<?> command = LiteralCommandBuilder
                .name("bedwars").aliases("bw").permission(new Permission("bedwars.admin", PermissionDefault.OP))
                .then(LiteralCommandBuilder.name("create")
                        .then(RequiredCommandBuilder
                                .<Player, String>name("map-name", StringArgumentType.word())
                                .executes((sender, arguments) -> {
                                    if (BedWarsPlugin.getInstance().getSetupManager().getMapBuilder(sender).isPresent()) {
                                        sender.sendMessage("You are already in setup mode. You can cancel the current one with /bw cancel.");
                                        return;
                                    }

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
                .then(LiteralCommandBuilder.name("setup")
                        .then(LiteralCommandBuilder
                                .<Player>name("cancel")
                                .executes(sender -> {
                                    BedWarsPlugin.getInstance().getSetupManager().getMapBuilder(sender).ifPresentOrElse(mapBuilder -> {
                                                mapBuilder.cancel();
                                                sender.sendMessage("The setup has been cancelled.");
                                            },
                                            () -> sender.sendMessage("You are not in a setup mode."));
                                })
                        )
                        .then(LiteralCommandBuilder
                                .<Player>name("complete")
                                .executes(sender -> {
                                    BedWarsPlugin.getInstance().getSetupManager().getMapBuilder(sender).ifPresentOrElse(mapBuilder -> {
                                                mapBuilder.complete();
                                                sender.sendMessage("The setup has been completed.");
                                            },
                                            () -> sender.sendMessage("You are not in a setup mode."));
                                })
                        )
                )
                .build();

        BedWarsPlugin.getInstance().registerCommand(command);
        BedWarsPlugin.getInstance().getLogger().info("Commands have been loaded.");
    }
}
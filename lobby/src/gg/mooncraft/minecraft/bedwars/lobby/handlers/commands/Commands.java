package gg.mooncraft.minecraft.bedwars.lobby.handlers.commands;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import me.eduardwayland.mooncraft.waylander.command.LiteralCommand;
import me.eduardwayland.mooncraft.waylander.command.builders.LiteralCommandBuilder;

import org.bukkit.entity.Player;

import gg.mooncraft.minecraft.bedwars.lobby.BedWarsPlugin;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class Commands {

    public static void loadAll() {
        LiteralCommand<?> command = LiteralCommandBuilder
                .<Player>name("bedwars").aliases(new String[]{"bw"})
                .executes(sender -> {

                })
                .build();

        BedWarsPlugin.getInstance().registerCommand(command);
        BedWarsPlugin.getInstance().getLogger().info("Commands have been loaded.");
    }
}
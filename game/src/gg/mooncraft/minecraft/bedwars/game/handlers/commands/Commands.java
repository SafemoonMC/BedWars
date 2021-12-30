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

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.GameState;
import gg.mooncraft.minecraft.bedwars.data.GameTeam;
import gg.mooncraft.minecraft.bedwars.data.MapDAO;
import gg.mooncraft.minecraft.bedwars.data.map.MapPointsContainer;
import gg.mooncraft.minecraft.bedwars.data.map.point.GameMapPoint;
import gg.mooncraft.minecraft.bedwars.data.map.point.PointTypes;
import gg.mooncraft.minecraft.bedwars.data.map.point.TeamMapPoint;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;

/**
 * Commands:
 * /bw create
 * /bw setup <complete or cancel>
 * /bw setup display <display>
 * /bw setup gamemode <SOLO/DUOS/TRIOS/QUADS>
 * /bw setup gamepoint <gamemode> <point>
 * /bw setup teampoint <gamemode> <point> <team>
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class Commands {

    public static void loadAll() {
        LiteralCommand<?> command = LiteralCommandBuilder
                .name("bedwars").aliases("bw").permission(new Permission("bedwars.admin", PermissionDefault.OP))
                .then(LiteralCommandBuilder
                        .<Player>name("start")
                        .executes(player -> {
                            BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player).ifPresentOrElse(gameMatch -> {
                                if (gameMatch.getGameState() == GameState.WAITING) {
                                    if (!gameMatch.getGameTicker().getGameStartTask().isRunning()) {
                                        gameMatch.getGameTicker().getGameStartTask().play();
                                        player.sendMessage("The GameStartTask has been started!");
                                    } else {
                                        player.sendMessage("The GameStartTask is already playing!");
                                    }
                                } else {
                                    player.sendMessage("This game has already been started!");
                                }
                            }, () -> player.sendMessage("You're not in a game match."));
                        })
                )
                .then(LiteralCommandBuilder.name("create")
                        .then(RequiredCommandBuilder
                                .<Player, String>name("map-name", StringArgumentType.word())
                                .executes((sender, arguments) -> {
                                    if (BedWarsPlugin.getInstance().getSetupManager().getMapBuilder(sender).isPresent()) {
                                        sender.sendMessage("You are already in setup mode. You can cancel the current one with /bw cancel.");
                                        return;
                                    }

                                    String mapName = arguments.getArgument("map-name", String.class);
                                    if (BedWarsPlugin.getInstance().getMapManager().getWorldsMap().containsKey(mapName)) {
                                        sender.sendMessage("There is already a map with that name.");
                                        return;
                                    }
                                    BedWarsPlugin.getInstance().getSetupManager().startSetup(sender, mapName);
                                    sender.sendMessage("Setup mode has been started for " + mapName + ".");
                                })
                        )
                )
                .then(LiteralCommandBuilder.name("delete")
                        .then(RequiredCommandBuilder
                                .<Player, String>name("map-name", StringArgumentType.word())
                                .executes((sender, arguments) -> {
                                    if (BedWarsPlugin.getInstance().getSetupManager().getMapBuilder(sender).isPresent()) {
                                        sender.sendMessage("You are in a setup mode. You can cancel the current one with /bw cancel.");
                                        return;
                                    }

                                    String mapName = arguments.getArgument("map-name", String.class);
                                    if (!BedWarsPlugin.getInstance().getMapManager().getWorldsMap().containsKey(mapName)) {
                                        sender.sendMessage("There is no map with that name.");
                                        return;
                                    }

                                    BedWarsPlugin.getInstance().getMapManager().deleteMap(mapName).thenAccept(deleted -> {
                                        if (deleted) {
                                            sender.sendMessage("The map has been removed.");
                                        } else {
                                            sender.sendMessage("The map cannot be removed.");
                                        }
                                    });

                                })
                        )
                )
                .then(LiteralCommandBuilder.name("load")
                        .then(RequiredCommandBuilder
                                .<Player, String>name("map-name", StringArgumentType.word())
                                .executes((sender, arguments) -> {
                                    if (BedWarsPlugin.getInstance().getSetupManager().getMapBuilder(sender).isPresent()) {
                                        sender.sendMessage("You are already in setup mode. You can cancel the current one with /bw cancel.");
                                        return;
                                    }

                                    String mapName = arguments.getArgument("map-name", String.class);
                                    if (!BedWarsPlugin.getInstance().getMapManager().getWorldsMap().containsKey(mapName)) {
                                        sender.sendMessage("There is no map with that name.");
                                        return;
                                    }
                                    BedWarsPlugin.getInstance().getSetupManager().startSetup(sender, mapName);
                                    sender.sendMessage("Setup mode has been started for " + mapName + ".");
                                })
                        )
                )
                .then(LiteralCommandBuilder.name("setup")
                        .then(LiteralCommandBuilder
                                .<Player>name("display")
                                .then(RequiredCommandBuilder
                                        .<Player, String>name("display", StringArgumentType.greedyString())
                                        .executes(((sender, arguments) -> {
                                            String display = arguments.getArgument("display", String.class);
                                            BedWarsPlugin.getInstance().getSetupManager().getMapBuilder(sender).ifPresentOrElse(mapBuilder -> {
                                                        mapBuilder.getBedWarsMap().getInformation().setDisplay(display);
                                                        MapDAO.update(mapBuilder.getBedWarsMap()).thenAccept(bedWarsMap -> sender.sendMessage("The display of the map has been updated!"));
                                                    },
                                                    () -> sender.sendMessage("You are not in a setup mode."));
                                        }))
                                )
                        )
                        .then(LiteralCommandBuilder
                                .<Player>name("gamemode")
                                .then(RequiredCommandBuilder
                                        .<Player, String>name("game-mode", StringArgumentType.word())
                                        .executes(((sender, arguments) -> {
                                            GameMode gameMode = GameMode.valueOf(arguments.getArgument("game-mode", String.class).toUpperCase());
                                            BedWarsPlugin.getInstance().getSetupManager().getMapBuilder(sender).ifPresentOrElse(mapBuilder -> {
                                                        if (mapBuilder.getBedWarsMap().getGameModeSet().contains(gameMode)) {
                                                            mapBuilder.getBedWarsMap().delGameMode(gameMode);
                                                            sender.sendMessage("The allowed game modes of the map have been updated! " + gameMode.name() + " has been removed.");
                                                            return;
                                                        }
                                                        mapBuilder.getBedWarsMap().addGameMode(gameMode);
                                                        sender.sendMessage("The allowed game modes of the map have been updated! " + gameMode.name() + " has been added.");
                                                    },
                                                    () -> sender.sendMessage("You are not in a setup mode."));
                                        }))
                                )
                        )
                        .then(LiteralCommandBuilder
                                .<Player>name("gamepoint")
                                .then(RequiredCommandBuilder
                                        .<Player, String>name("game-mode", StringArgumentType.word())
                                        .then(RequiredCommandBuilder
                                                .<Player, String>name("type", StringArgumentType.word())
                                                .executes(((sender, arguments) -> {
                                                    GameMode gameMode = GameMode.valueOf(arguments.getArgument("game-mode", String.class).toUpperCase());
                                                    PointTypes.MAP pointType = PointTypes.MAP.valueOf(arguments.getArgument("type", String.class).toUpperCase());

                                                    BedWarsPlugin.getInstance().getSetupManager().getMapBuilder(sender).ifPresentOrElse(mapBuilder -> {
                                                                MapPointsContainer mapPointsContainer = mapBuilder.getBedWarsMap().getPointsContainer();
                                                                mapPointsContainer.addPoint(new GameMapPoint(mapPointsContainer, -1, gameMode, pointType, sender.getLocation().getX(), sender.getLocation().getY(), sender.getLocation().getZ(), sender.getLocation().getYaw(), sender.getLocation().getPitch()));
                                                                sender.sendMessage("A new game point has been added to the map.");
                                                            },
                                                            () -> sender.sendMessage("You are not in a setup mode."));
                                                }))
                                        )
                                )
                        )
                        .then(LiteralCommandBuilder
                                .<Player>name("teampoint")
                                .then(RequiredCommandBuilder
                                        .<Player, String>name("game-mode", StringArgumentType.word())
                                        .then(RequiredCommandBuilder
                                                .<Player, String>name("type", StringArgumentType.word())
                                                .then(RequiredCommandBuilder
                                                        .<Player, String>name("game-team", StringArgumentType.word())
                                                        .executes(((sender, arguments) -> {
                                                            GameMode gameMode = GameMode.valueOf(arguments.getArgument("game-mode", String.class).toUpperCase());
                                                            PointTypes.TEAM pointType = PointTypes.TEAM.valueOf(arguments.getArgument("type", String.class).toUpperCase());
                                                            GameTeam gameTeam = GameTeam.valueOf(arguments.getArgument("game-team", String.class).toUpperCase());
                                                            BedWarsPlugin.getInstance().getSetupManager().getMapBuilder(sender).ifPresentOrElse(mapBuilder -> {
                                                                        MapPointsContainer mapPointsContainer = mapBuilder.getBedWarsMap().getPointsContainer();
                                                                        mapPointsContainer.addPoint(new TeamMapPoint(mapPointsContainer, -1, gameMode, gameTeam, pointType, sender.getLocation().getX(), sender.getLocation().getY(), sender.getLocation().getZ(), sender.getLocation().getYaw(), sender.getLocation().getPitch()));
                                                                        sender.sendMessage("A new team point has been added to the map.");
                                                                    },
                                                                    () -> sender.sendMessage("You are not in a setup mode."));
                                                        }))
                                                )
                                        )
                                )
                        )
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
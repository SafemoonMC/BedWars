package gg.mooncraft.minecraft.bedwars.game.managers;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.placeholder.Placeholder;
import me.neznamy.tab.api.scoreboard.Scoreboard;
import me.neznamy.tab.shared.features.PlaceholderManagerImpl;
import me.neznamy.tab.shared.placeholders.PlayerPlaceholderImpl;
import me.neznamy.tab.shared.placeholders.ServerPlaceholderImpl;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.GameState;
import gg.mooncraft.minecraft.bedwars.data.GameTeam;
import gg.mooncraft.minecraft.bedwars.data.map.BedWarsMap;
import gg.mooncraft.minecraft.bedwars.data.map.MapInfo;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.GameConstants;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchTeam;
import gg.mooncraft.minecraft.bedwars.game.match.PlayerStatus;
import gg.mooncraft.minecraft.bedwars.game.match.TeamStatus;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.GameMatchEvent;
import gg.mooncraft.minecraft.bedwars.game.utilities.DisplayUtilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public final class BoardManager {

    /*
    Fields
     */
    private final @NotNull List<String> updatablePlaceholderList = new ArrayList<>();

    /*
    Constructor
     */
    public BoardManager() {
        // Register %date%
        TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%date%", -1, () -> new SimpleDateFormat("MM/dd/yy").format(new Date()));
        // Register %server-name%
        TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%server-name%", -1, () -> {
            String[] args = BedWarsPlugin.getInstance().getServerName().split("-");
            return args[0].substring(0, 2) + "-" + args[1].substring(0, 2) + "-" + args[2];
        });
        // Register %plugin-version%
        TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%plugin-version%", -1, () -> BedWarsPlugin.getInstance().getDescription().getVersion());
        // Register %game-map%
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%game-map%", -1, tabPlayer -> {
            Player player = (Player) tabPlayer.getPlayer();
            Optional<GameMatch> matchOptional = BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player);

            return matchOptional.flatMap(GameMatch::getBedWarsMap).map(BedWarsMap::getInformation).map(MapInfo::getDisplay).orElse("Unknown");
        });
        // Register %game-mode%
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%game-mode%", -1, tabPlayer -> {
            Player player = (Player) tabPlayer.getPlayer();
            Optional<GameMatch> matchOptional = BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player);

            return matchOptional.map(GameMatch::getGameMode).map(GameMode::name).orElse("Unknown");
        });
        // Register %game-status%
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%game-status%", -1, tabPlayer -> {
            Player player = (Player) tabPlayer.getPlayer();
            Optional<GameMatch> matchOptional = BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player);
            if (matchOptional.isEmpty()) return "Unknown";
            GameMatch gameMatch = matchOptional.get();
            if (gameMatch.getGameState() == GameState.STARTING) {
                return gameMatch.getGameState().getDisplay() + gameMatch.getGameTicker().getGameStartTask().getTimeColor() + gameMatch.getGameTicker().getGameStartTask().getTimeLeft() + "s";
            }
            return gameMatch.getGameState().getDisplay();
        });
        // Register %game-event%
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%game-event%", -1, tabPlayer -> {
            Player player = (Player) tabPlayer.getPlayer();
            Optional<GameMatch> matchOptional = BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player);
            if (matchOptional.isEmpty()) return "Unknown";
            GameMatch gameMatch = matchOptional.get();
            if (gameMatch.getGameState() != GameState.PLAYING) return "Unknown";
            Optional<GameMatchEvent> matchEventOptional = gameMatch.getEventSystem().getNextEvent();
            if (matchEventOptional.isEmpty()) return "Unknown";
            GameMatchEvent gameMatchEvent = matchEventOptional.get();

            switch (gameMatchEvent.getGameEvent()) {
                case DIAMOND -> {
                    return ChatColor.WHITE + gameMatchEvent.getGameEvent().getDisplay() + " " + DisplayUtilities.getLiteral(gameMatch.getGeneratorSystem().getNextDiamondTier()) + " in " + ChatColor.GREEN + DisplayUtilities.getDisplay(gameMatchEvent.getTimeLeft());
                }
                case EMERALD -> {
                    return ChatColor.WHITE + gameMatchEvent.getGameEvent().getDisplay() + " " + DisplayUtilities.getLiteral(gameMatch.getGeneratorSystem().getNextEmeraldTier()) + " in " + ChatColor.GREEN + DisplayUtilities.getDisplay(gameMatchEvent.getTimeLeft());
                }
                default -> {
                    return ChatColor.WHITE + gameMatchEvent.getGameEvent().getDisplay() + " in " + ChatColor.GREEN + DisplayUtilities.getDisplay(gameMatchEvent.getTimeLeft());
                }
            }
        });
        // Register %game-players-count%
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%game-players-count%", -1, tabPlayer -> {
            Player player = (Player) tabPlayer.getPlayer();
            Optional<GameMatch> matchOptional = BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player);

            return matchOptional.map(GameMatch::getPlayersCount).map(String::valueOf).orElse("0");
        });
        // Register %game-players-capacity%
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%game-players-capacity%", -1, tabPlayer -> {
            Player player = (Player) tabPlayer.getPlayer();
            Optional<GameMatch> matchOptional = BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player);

            return matchOptional.map(GameMatch::getPlayersCapacity).map(String::valueOf).orElse("0");
        });
        // Register %game-team-status-[color]%
        for (GameTeam gameTeam : GameTeam.values()) {
            TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%game-team-status-" + gameTeam.name() + "%", -1, tabPlayer -> {
                Player player = (Player) tabPlayer.getPlayer();
                Optional<GameMatch> matchOptional = BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player);

                return matchOptional.flatMap(gameMatch -> gameMatch.getTeam(gameTeam)).map(gameMatchTeam -> {
                    if (gameMatchTeam.getTeamStatus() == TeamStatus.NOT_ALIVE) {
                        long playersAlive = gameMatchTeam.getMatchPlayerList().stream().filter(gameMatchPlayer -> gameMatchPlayer.getPlayerStatus() == PlayerStatus.RESPAWNING || gameMatchPlayer.getPlayerStatus() == PlayerStatus.ALIVE).count();
                        if (playersAlive == 0) {
                            return TeamStatus.NOT_ALIVE.getSymbol();
                        } else {
                            return String.valueOf(playersAlive);
                        }
                    } else {
                        return TeamStatus.ALIVE.getSymbol();
                    }
                }).orElse("-");
            });

            updatablePlaceholderList.add("%game-team-status-" + gameTeam.name() + "%");
        }

        updatablePlaceholderList.add("%game-status%");
        updatablePlaceholderList.add("%game-event%");
        updatablePlaceholderList.add("%game-players-count%");
        updatablePlaceholderList.add("%game-players-capacity%");
    }

    /*
    Methods
     */
    public @NotNull Scoreboard createScoreboard(@NotNull GameMatch gameMatch) {
        return TabAPI.getInstance().getScoreboardManager().createScoreboard(String.format("bw-map-%s", gameMatch.getUniqueId()), GameConstants.SCOREBOARD_TITLE, GameConstants.SCOREBOARD_LINES_LOBBY);
    }

    public @NotNull Scoreboard createScoreboard(@NotNull GameMatch gameMatch, @NotNull GameMatchTeam gameMatchTeam) {
        List<String> lines = new ArrayList<>();
        for (String line : GameConstants.SCOREBOARD_LINES_PLAY) {
            if (!line.equalsIgnoreCase("{teams}")) {
                lines.add(line);
                continue;
            }

            for (GameMatchTeam matchTeam : gameMatch.getTeamList()) {
                String team;
                if (matchTeam.getId() != gameMatchTeam.getId()) {
                    team = String.format("%c &f%s: %%game-team-status-%s%%", matchTeam.getGameTeam().getLetter(), matchTeam.getGameTeam().getDisplay(), matchTeam.getGameTeam().name());
                } else {
                    team = String.format("%c &f%s: %%game-team-status-%s%% &7You", matchTeam.getGameTeam().getLetter(), matchTeam.getGameTeam().getDisplay(), matchTeam.getGameTeam().name());
                }
                lines.add(matchTeam.getGameTeam().getChatColor() + team);
            }
        }

        return TabAPI.getInstance().getScoreboardManager().createScoreboard(String.format("bw-map-%s-%s", gameMatch.getUniqueId(), gameMatchTeam.getGameTeam().name()), GameConstants.SCOREBOARD_TITLE, lines);
    }

    public void updateScoreboard(@NotNull TabPlayer tabPlayer) {
        if (!tabPlayer.isLoaded()) return;
        PlaceholderManagerImpl placeholderManager = (PlaceholderManagerImpl) TabAPI.getInstance().getPlaceholderManager();

        for (Placeholder placeholder : placeholderManager.getUsedPlaceholders()) {
            if (!this.updatablePlaceholderList.contains(placeholder.getIdentifier())) continue;

            boolean updated = false;
            if (placeholder instanceof ServerPlaceholderImpl serverPlaceholder) {
                updated = serverPlaceholder.update();
            }
            if (placeholder instanceof PlayerPlaceholderImpl playerPlaceholder) {
                updated = playerPlaceholder.update(tabPlayer);
            }

            if (updated) {
                placeholderManager.getPlaceholderUsage().get(placeholder.getIdentifier()).forEach(tabFeature -> tabFeature.refresh(tabPlayer, true));
            }
        }
    }
}
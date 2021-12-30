package gg.mooncraft.minecraft.bedwars.game.managers;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.scoreboard.Scoreboard;

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
import gg.mooncraft.minecraft.bedwars.game.match.TeamStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public final class BoardManager {

    /*
    Constructor
     */
    public BoardManager() {
        // Register %date%
        TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%date%", -1, () -> new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        // Register %server-name%
        TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%server-name%", -1, () -> BedWarsPlugin.getInstance().getServerName());
        // Register %plugin-version%
        TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%plugin-version%", -1, () -> BedWarsPlugin.getInstance().getDescription().getVersion());
        // Register %game-status%
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%game-status%", 50, tabPlayer -> {
            Player player = (Player) tabPlayer.getPlayer();
            Optional<GameMatch> matchOptional = BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player);
            if (matchOptional.isEmpty()) return "Unknown";
            GameMatch gameMatch = matchOptional.get();
            if (gameMatch.getGameState() == GameState.WAITING) {
                if (gameMatch.getGameTicker().getGameStartTask().isRunning()) {
                    return gameMatch.getGameState().getDisplay() + gameMatch.getGameTicker().getGameStartTask().getTimeColor() + gameMatch.getGameTicker().getGameStartTask().getTimeLeft() + "s";
                }
            }
            return gameMatch.getGameState().getDisplay();
        });
        // Register %game-event%
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%game-event%", 50, tabPlayer -> {
            Player player = (Player) tabPlayer.getPlayer();
            Optional<GameMatch> matchOptional = BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player);

            return matchOptional.map(GameMatch::getGameState).map(GameState::getDisplay).orElse("Unknown");
        });
        // Register %game-mode%
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%game-mode%", -1, tabPlayer -> {
            Player player = (Player) tabPlayer.getPlayer();
            Optional<GameMatch> matchOptional = BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player);

            return matchOptional.map(GameMatch::getGameMode).map(GameMode::name).orElse("Unknown");
        });
        // Register %game-map%
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%game-map%", -1, tabPlayer -> {
            Player player = (Player) tabPlayer.getPlayer();
            Optional<GameMatch> matchOptional = BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player);

            return matchOptional.flatMap(GameMatch::getBedWarsMap).map(BedWarsMap::getInformation).map(MapInfo::getDisplay).orElse("Unknown");
        });
        // Register %game-players-count%
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%game-players-count%", 50, tabPlayer -> {
            Player player = (Player) tabPlayer.getPlayer();
            Optional<GameMatch> matchOptional = BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player);

            return matchOptional.map(GameMatch::getPlayersCount).map(String::valueOf).orElse("0");
        });
        // Register %game-players-capacity%
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%game-players-capacity%", 50, tabPlayer -> {
            Player player = (Player) tabPlayer.getPlayer();
            Optional<GameMatch> matchOptional = BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player);

            return matchOptional.map(GameMatch::getPlayersCapacity).map(String::valueOf).orElse("0");
        });

        // Register %game-team-status-[color]%
        for (GameTeam gameTeam : GameTeam.values()) {
            TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%game-team-status-" + gameTeam.name() + "%", 50, tabPlayer -> {
                Player player = (Player) tabPlayer.getPlayer();
                Optional<GameMatch> matchOptional = BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player);

                return matchOptional.flatMap(gameMatch -> gameMatch.getTeam(gameTeam)).map(GameMatchTeam::getTeamStatus).map(TeamStatus::getSymbol).orElse("-");
            });
        }
    }

    /*
    Methods
     */
    public @NotNull Scoreboard createScoreboard(@NotNull GameMatch gameMatch) {
        return TabAPI.getInstance().getScoreboardManager().createScoreboard(String.format("bw-map-%s", gameMatch.getId()), GameConstants.SCOREBOARD_TITLE, GameConstants.SCOREBOARD_LINES_LOBBY);
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

        return TabAPI.getInstance().getScoreboardManager().createScoreboard(String.format("bw-map-%s-%s", gameMatch.getId(), gameMatchTeam.getGameTeam().name()), GameConstants.SCOREBOARD_TITLE, lines);
    }
}
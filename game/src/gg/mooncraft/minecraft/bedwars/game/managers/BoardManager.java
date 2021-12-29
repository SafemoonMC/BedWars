package gg.mooncraft.minecraft.bedwars.game.managers;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.scoreboard.Scoreboard;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameState;
import gg.mooncraft.minecraft.bedwars.data.GameTeam;
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
        TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%date%", 50, () -> {
            return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        });
        // Register %server-name%
        TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%server-name%", 50, () -> {
            return BedWarsPlugin.getInstance().getServerName();
        });
        // Register %game-status%
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%game-status%", 50, tabPlayer -> {
            Player player = (Player) tabPlayer.getPlayer();
            Optional<GameMatch> matchOptional = BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player);

            return matchOptional.map(GameMatch::getGameState).map(GameState::name).orElse("Unknown");
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
    public @NotNull Scoreboard createScoreboard(@NotNull GameMatch gameMatch, @NotNull GameMatchTeam gameMatchTeam) {
        List<String> lines = new ArrayList<>();
        for (String line : GameConstants.DEFAULT_SCOREBOARD_LINES) {
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

        return TabAPI.getInstance().getScoreboardManager().createScoreboard(String.format("bw-map-%s-%s", gameMatch.getId(), gameMatchTeam.getGameTeam().name()), GameConstants.DEFAULT_SCOREBOARD_TITLE, lines);
    }
}
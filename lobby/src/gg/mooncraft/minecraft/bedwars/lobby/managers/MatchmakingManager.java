package gg.mooncraft.minecraft.bedwars.lobby.managers;

import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.scheduler.SchedulerTask;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.lobby.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.lobby.utilities.PartyUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Getter
public final class MatchmakingManager {

    /*
    Fields
     */
    private final @NotNull SchedulerTask schedulerTask;
    private final @NotNull BlockingQueue<Matchmaking> matchmakingQueue = new ArrayBlockingQueue<>(512);

    /*
    Constructor
     */
    public MatchmakingManager() {
        this.schedulerTask = BedWarsPlugin.getInstance().getScheduler().asyncRepeating(() -> {
            if (!BedWarsPlugin.getInstance().getGameServerManager().areGameServersRunning()) return;
            if (this.matchmakingQueue.isEmpty()) return;
            Matchmaking matchmaking = this.matchmakingQueue.poll();
            if (matchmaking == null) return;
            matchmaking.process();
        }, 500, TimeUnit.MILLISECONDS);
    }

    /*
    Methods
     */
    public void playMatchmaking(@NotNull GameMode gameMode, @NotNull Player player) {
        if (this.matchmakingQueue.stream().anyMatch(matchmaking -> matchmaking.getPlayerList().contains(player))) {
            return;
        }
        PartyUtilities.getParty(player).handle((partyData, throwable) -> {
            if (throwable != null) return null;
            return partyData;
        }).thenApply(partyData -> {
            if (partyData == null) {
                return new Matchmaking(gameMode, List.of(player));
            }
            List<Player> playerList = new ArrayList<>(PartyUtilities.getPartyMembers(partyData, true));
            if (gameMode.getPlayersPerTeam() < playerList.size()) {
                player.sendMessage(ChatColor.RED + "Your party is too large for this mode!");
                return null;
            }
            return new Matchmaking(gameMode, playerList);
        }).thenAccept(matchmaking -> {
            if (matchmaking == null) return;
            try {
                if (this.matchmakingQueue.stream().anyMatch(streamMatchmaking -> matchmaking.getPlayerList().containsAll(streamMatchmaking.getPlayerList()))) {
                    return;
                }
                this.matchmakingQueue.put(matchmaking);
            } catch (InterruptedException e) {
                BedWarsPlugin.getInstance().getLogger().severe(e.getMessage());
                player.sendMessage(ChatColor.RED + "Matchmaking system outage!");
            }
        });
    }
}
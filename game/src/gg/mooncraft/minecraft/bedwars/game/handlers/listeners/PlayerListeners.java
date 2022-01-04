package gg.mooncraft.minecraft.bedwars.game.handlers.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.events.EventsAPI;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerJoinEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerQuitEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchVillagerInteractEvent;

public class PlayerListeners implements Listener {

    /*
    Constructor
     */
    public PlayerListeners() {
        Bukkit.getPluginManager().registerEvents(this, BedWarsPlugin.getInstance());
    }

    /*
    Handlers
     */
    @EventHandler
    public void on(@NotNull PlayerJoinEvent e) {
        Player player = e.getPlayer();
        BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player)
                .ifPresent(gameMatch -> {
                    gameMatch.getTeamOf(player)
                            .ifPresent(gameMatchTeam -> gameMatchTeam.getPlayer(player.getUniqueId())
                                    .ifPresent(gameMatchPlayer -> EventsAPI.callEventSync(new MatchPlayerJoinEvent(player, gameMatch, gameMatchTeam, gameMatchPlayer))));
                });
        e.joinMessage(null);
    }

    @EventHandler
    public void on(@NotNull PlayerQuitEvent e) {
        Player player = e.getPlayer();
        BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player)
                .ifPresent(gameMatch -> {
                    gameMatch.getTeamOf(player)
                            .ifPresent(gameMatchTeam -> gameMatchTeam.getPlayer(player.getUniqueId())
                                    .ifPresent(gameMatchPlayer -> EventsAPI.callEventSync(new MatchPlayerQuitEvent(player, gameMatch, gameMatchTeam, gameMatchPlayer))));
                });
        e.quitMessage(null);
    }

    @EventHandler
    public void on(@NotNull PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        if (e.getRightClicked() instanceof Villager villager) {
            BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player)
                    .ifPresent(gameMatch -> gameMatch.getVillagersSystem().getMatchVillager(villager)
                            .ifPresent(matchVillager -> gameMatch.getTeamOf(player)
                                    .ifPresent(gameMatchTeam -> gameMatchTeam.getPlayer(player.getUniqueId())
                                            .ifPresent(gameMatchPlayer -> {
                                                e.setCancelled(true);
                                                EventsAPI.callEventSync(new MatchVillagerInteractEvent(player, gameMatchTeam, gameMatchPlayer, matchVillager));
                                            }))));
        }
    }
}
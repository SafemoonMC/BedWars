package gg.mooncraft.minecraft.bedwars.game.handlers.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.user.stats.StatisticTypes;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.events.MatchBedBreakEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerDeathEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerPickupItemEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchTeamWinEvent;
import gg.mooncraft.minecraft.bedwars.game.match.damage.PlayerDamage;

public class StatsListeners implements Listener {

    /*
    Constructor
     */
    public StatsListeners() {
        Bukkit.getPluginManager().registerEvents(this, BedWarsPlugin.getInstance());
    }

    /*
    Handlers
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void on(@NotNull MatchPlayerDeathEvent e) {
        Player player = e.getPlayer();
        BedWarsPlugin.getInstance().getUserFactory().getUser(player).ifPresent(bedWarsUser -> {
            if (!e.getMatchTeam().isAnyAlive()) {
                bedWarsUser.getStatisticContainer().updateGameStatistic(e.getMatch().getGameMode(), StatisticTypes.GAME.LOSSES, 1);
                bedWarsUser.getStatisticContainer().updateGameStatistic(e.getMatch().getGameMode(), StatisticTypes.GAME.FINAL_DEATHS, 1);
            } else {
                bedWarsUser.getStatisticContainer().updateGameStatistic(e.getMatch().getGameMode(), StatisticTypes.GAME.NORMAL_DEATHS, 1);
            }
        });
        if (e.getLastPlayerDamage() != null) {
            PlayerDamage playerDamage = e.getLastPlayerDamage();
            BedWarsPlugin.getInstance().getUserFactory().getUser(playerDamage.getPlayer()).ifPresent(bedWarsUser -> {
                if (!e.getMatchTeam().isAnyAlive()) {
                    bedWarsUser.getStatisticContainer().updateGameStatistic(e.getMatch().getGameMode(), StatisticTypes.GAME.FINAL_KILLS, 1);
                } else {
                    bedWarsUser.getStatisticContainer().updateGameStatistic(e.getMatch().getGameMode(), StatisticTypes.GAME.NORMAL_KILLS, 1);
                }
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void on(@NotNull MatchPlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        BedWarsPlugin.getInstance().getUserFactory().getUser(player).ifPresent(bedWarsUser -> {
            ItemStack itemStack = e.getItem().getItemStack();
            switch (itemStack.getType()) {
                case IRON_INGOT -> {
                    bedWarsUser.getStatisticContainer().updateOverallStatistic(StatisticTypes.OVERALL.IRON, itemStack.getAmount());
                }
                case GOLD_INGOT -> {
                    bedWarsUser.getStatisticContainer().updateOverallStatistic(StatisticTypes.OVERALL.GOLD, itemStack.getAmount());
                }
                case DIAMOND -> {
                    bedWarsUser.getStatisticContainer().updateOverallStatistic(StatisticTypes.OVERALL.DIAMOND, itemStack.getAmount());
                }
                case EMERALD -> {
                    bedWarsUser.getStatisticContainer().updateOverallStatistic(StatisticTypes.OVERALL.EMERALD, itemStack.getAmount());
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void on(@NotNull MatchBedBreakEvent e) {
        Player player = e.getPlayer();
        BedWarsPlugin.getInstance().getUserFactory().getUser(player).ifPresent(bedWarsUser -> {
            bedWarsUser.getStatisticContainer().updateGameStatistic(e.getMatch().getGameMode(), StatisticTypes.GAME.BEDS_BROKEN, 1);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void on(@NotNull MatchTeamWinEvent e) {
        e.getMatchTeam().broadcastAction(gameMatchPlayer -> {
            gameMatchPlayer.getPlayer().ifPresent(player -> {
                BedWarsPlugin.getInstance().getUserFactory().getUser(player).ifPresent(bedWarsUser -> {
                    bedWarsUser.getStatisticContainer().updateGameStatistic(e.getMatch().getGameMode(), StatisticTypes.GAME.WINS, 1);
                });
            });
        });
    }
}
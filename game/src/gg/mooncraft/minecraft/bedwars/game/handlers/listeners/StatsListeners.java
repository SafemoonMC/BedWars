package gg.mooncraft.minecraft.bedwars.game.handlers.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.user.stats.StatisticTypes;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.GameConstants;
import gg.mooncraft.minecraft.bedwars.game.events.MatchBedBreakEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerDeathEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchPlayerPickupItemEvent;
import gg.mooncraft.minecraft.bedwars.game.events.MatchTeamWinEvent;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.PlayerStatus;
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
        GameMatch gameMatch = e.getMatch();
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
                    bedWarsUser.addExperience(GameConstants.EXPERIENCE_FINALKILL);
                    playerDamage.getPlayer().sendMessage(GameConstants.MESSAGE_EXPERIENCE_RECEIVED
                            .replaceAll("%amount%", String.valueOf(GameConstants.EXPERIENCE_FINALKILL))
                            .replaceAll("%action%", "Final Kill")
                    );
                    gameMatch.getDataOf(playerDamage.getPlayer()).ifPresent(matchPlayer -> matchPlayer.updateExperience(GameConstants.EXPERIENCE_FINALKILL));
                } else {
                    bedWarsUser.getStatisticContainer().updateGameStatistic(e.getMatch().getGameMode(), StatisticTypes.GAME.NORMAL_KILLS, 1);
                }
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void on(@NotNull MatchPlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        e.getMatch().getDataOf(player).ifPresent(matchPlayer -> {
            ItemStack itemStack = e.getItem().getItemStack();
            switch (itemStack.getType()) {
                case IRON_INGOT -> {
                    matchPlayer.updateOverallStatistic(StatisticTypes.OVERALL.IRON, itemStack.getAmount());
                }
                case GOLD_INGOT -> {
                    matchPlayer.updateOverallStatistic(StatisticTypes.OVERALL.GOLD, itemStack.getAmount());
                }
                case DIAMOND -> {
                    matchPlayer.updateOverallStatistic(StatisticTypes.OVERALL.DIAMOND, itemStack.getAmount());
                }
                case EMERALD -> {
                    matchPlayer.updateOverallStatistic(StatisticTypes.OVERALL.EMERALD, itemStack.getAmount());
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void on(@NotNull MatchBedBreakEvent e) {
        Player player = e.getPlayer();
        GameMatchPlayer matchPlayer = e.getMatchPlayer();
        BedWarsPlugin.getInstance().getUserFactory().getUser(player).ifPresent(bedWarsUser -> {
            bedWarsUser.getStatisticContainer().updateGameStatistic(e.getMatch().getGameMode(), StatisticTypes.GAME.BEDS_BROKEN, 1);
            bedWarsUser.addExperience(GameConstants.EXPERIENCE_BEDDESTRUCTION);
            player.sendMessage(GameConstants.MESSAGE_EXPERIENCE_RECEIVED
                    .replaceAll("%amount%", String.valueOf(GameConstants.EXPERIENCE_BEDDESTRUCTION))
                    .replaceAll("%action%", "Bed Broken")
            );
        });
        matchPlayer.updateExperience(GameConstants.EXPERIENCE_BEDDESTRUCTION);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void on(@NotNull MatchTeamWinEvent e) {
        e.getMatchTeam().broadcastAction(gameMatchPlayer -> {
            gameMatchPlayer.getPlayer().ifPresent(player -> {
                BedWarsPlugin.getInstance().getUserFactory().getUser(player).ifPresent(bedWarsUser -> {
                    bedWarsUser.getStatisticContainer().updateGameStatistic(e.getMatch().getGameMode(), StatisticTypes.GAME.WINS, 1);
                    bedWarsUser.addExperience(e.getMatch().getGameMode() == GameMode.SOLO || e.getMatch().getGameMode() == GameMode.DUOS ? GameConstants.EXPERIENCE_WIN_SOLODUOS : GameConstants.EXPERIENCE_WIN_TRIOSQUADS);
                    player.sendMessage(GameConstants.MESSAGE_EXPERIENCE_RECEIVED
                            .replaceAll("%amount%", String.valueOf(e.getMatch().getGameMode() == GameMode.SOLO || e.getMatch().getGameMode() == GameMode.DUOS ? GameConstants.EXPERIENCE_WIN_SOLODUOS : GameConstants.EXPERIENCE_WIN_TRIOSQUADS))
                            .replaceAll("%action%", "Game Won")
                    );

                    if (gameMatchPlayer.getPlayerStatus() != PlayerStatus.SPECTATING) {
                        gameMatchPlayer.getGameStatistics().forEach((k, v) -> {
                            bedWarsUser.getStatisticContainer().updateGameStatistic(e.getMatch().getGameMode(), k, v.get());
                        });
                        gameMatchPlayer.getOverallStatistics().forEach((k, v) -> {
                            bedWarsUser.getStatisticContainer().updateOverallStatistic(k, v.get());
                        });
                    }
                });
            });
            gameMatchPlayer.updateExperience(e.getMatch().getGameMode() == GameMode.SOLO || e.getMatch().getGameMode() == GameMode.DUOS ? GameConstants.EXPERIENCE_WIN_SOLODUOS : GameConstants.EXPERIENCE_WIN_TRIOSQUADS);
        });
    }
}
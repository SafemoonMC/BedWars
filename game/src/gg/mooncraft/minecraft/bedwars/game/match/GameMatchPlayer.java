package gg.mooncraft.minecraft.bedwars.game.match;

import lombok.Getter;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gg.mooncraft.minecraft.bedwars.data.user.stats.StatisticTypes;
import gg.mooncraft.minecraft.bedwars.game.events.EventsAPI;
import gg.mooncraft.minecraft.bedwars.game.events.MatchUpdatePlayerEvent;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.PlaytimeTask;
import gg.mooncraft.minecraft.bedwars.game.shop.PermanentElement;
import gg.mooncraft.minecraft.bedwars.game.utilities.ItemsUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public final class GameMatchPlayer {

    /*
    Fields
     */
    private final @NotNull GameMatchTeam parent;

    private final @NotNull UUID uniqueId;
    private @Nullable Player player;
    private @Nullable TabPlayer tabPlayer;

    private @NotNull PlayerStatus playerStatus;
    private @NotNull PlaytimeTask playtimeTask;

    private final @NotNull AtomicInteger experienceReward;
    private final @NotNull Map<StatisticTypes.GAME, AtomicInteger> gameStatistics;
    private final @NotNull Map<StatisticTypes.OVERALL, AtomicInteger> overallStatistics;

    private final @NotNull ItemStack weapon;
    private final @NotNull ItemStack utility;
    private final @NotNull ItemStack[] armor;
    private final @NotNull List<PermanentElement> permanentElementList;

    private final @NotNull AtomicLong lastMagicMilk;

    /*
    Constructor
     */
    public GameMatchPlayer(@NotNull GameMatchTeam gameMatchTeam, @NotNull UUID uniqueId) {
        this.parent = gameMatchTeam;
        this.uniqueId = uniqueId;
        this.playerStatus = PlayerStatus.ALIVE;
        this.playtimeTask = new PlaytimeTask(this);

        this.experienceReward = new AtomicInteger();
        this.gameStatistics = new HashMap<>();
        this.overallStatistics = new HashMap<>();

        this.weapon = ItemsUtilities.createPureItem(Material.WOODEN_SWORD);
        this.weapon.removeItemFlags(ItemFlag.HIDE_ENCHANTS);

        this.utility = new ItemStack(Material.AIR);

        this.armor = new ItemStack[4];
        this.armor[0] = ItemsUtilities.createArmorItem(gameMatchTeam.getGameTeam(), Material.LEATHER_BOOTS);
        this.armor[1] = ItemsUtilities.createArmorItem(gameMatchTeam.getGameTeam(), Material.LEATHER_LEGGINGS);
        this.armor[2] = ItemsUtilities.createArmorItem(gameMatchTeam.getGameTeam(), Material.LEATHER_CHESTPLATE);
        this.armor[3] = ItemsUtilities.createArmorItem(gameMatchTeam.getGameTeam(), Material.LEATHER_HELMET);
        this.permanentElementList = new ArrayList<>();

        this.lastMagicMilk = new AtomicLong();
    }

    /*
    Methods
     */
    public void updateEffect() {
        if (player != null) {
            if (getParent().getUpgradeTier("miner") != 0) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, getParent().getUpgradeTier("miner") - 1, true, true, true));
            }
            if (getParent().getUpgradeTier("healpool") == 1 && getParent().isBedArea(player.getLocation())) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, true, true, true));
            }
        }
    }

    public void updateWeapon() {
        if (getParent().getUpgradeTier("weapon") == 1) {
            this.weapon.addEnchantment(Enchantment.DAMAGE_ALL, 1);
            if (player != null) {
                for (int i = 0; i < player.getInventory().getSize(); i++) {
                    ItemStack itemStack = player.getInventory().getItem(i);
                    if (itemStack != null && (itemStack.getType().name().contains("SWORD") || itemStack.getType().name().contains("AXE"))) {
                        itemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                    }
                }
            }
        }
    }

    public void updateArmor() {
        int level = getParent().getUpgradeTier("armor");
        if (level != 0) {
            if (!this.armor[0].getType().isAir()) {
                this.armor[0].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, level);
            }
            if (!this.armor[1].getType().isAir()) {
                this.armor[1].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, level);
            }
            this.armor[2].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, level);
            this.armor[3].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, level);
            if (player != null) {
                player.getInventory().setArmorContents(getArmor());
            }
        }
    }

    public void updateMagicMilk() {
        this.lastMagicMilk.set(System.currentTimeMillis());
    }

    public void updateStatus(@NotNull PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
        EventsAPI.callEventSync(new MatchUpdatePlayerEvent(this));
    }

    public void updateExperience(int amount) {
        this.experienceReward.addAndGet(amount);
    }

    public void updateGameStatistic(StatisticTypes.GAME type, int amount) {
        this.gameStatistics.putIfAbsent(type, new AtomicInteger());
        this.gameStatistics.get(type).addAndGet(amount);
    }

    public void updateOverallStatistic(StatisticTypes.OVERALL type, int amount) {
        this.overallStatistics.putIfAbsent(type, new AtomicInteger());
        this.overallStatistics.get(type).addAndGet(amount);
    }

    public @NotNull Optional<Player> getPlayer() {
        if (this.player == null || !this.player.isOnline()) {
            this.player = Bukkit.getPlayer(this.uniqueId);
        }
        return Optional.ofNullable(this.player);
    }

    public @NotNull Optional<TabPlayer> getTabPlayer() {
        if (this.tabPlayer == null || !this.tabPlayer.isOnline()) {
            this.tabPlayer = TabAPI.getInstance().getPlayer(this.uniqueId);
        }
        return Optional.ofNullable(this.tabPlayer);
    }

    /*
    Override Methods
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameMatchPlayer that = (GameMatchPlayer) o;
        return getUniqueId().equals(that.getUniqueId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUniqueId());
    }
}
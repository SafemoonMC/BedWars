package gg.mooncraft.minecraft.bedwars.game.handlers.listeners.items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameTeam;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.GameConstants;
import gg.mooncraft.minecraft.bedwars.game.events.MatchBlockPlaceEvent;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatchPlayer;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.BedbugTask;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.BridgeEggTask;
import gg.mooncraft.minecraft.bedwars.game.match.tasks.DreamDefenderTask;
import gg.mooncraft.minecraft.bedwars.game.shop.itemdata.BedbugItem;
import gg.mooncraft.minecraft.bedwars.game.shop.itemdata.BridgeEggItem;
import gg.mooncraft.minecraft.bedwars.game.shop.itemdata.CompactTowerItem;
import gg.mooncraft.minecraft.bedwars.game.shop.itemdata.DreamDefenderItem;
import gg.mooncraft.minecraft.bedwars.game.utilities.SchematicUtilities;

import java.io.IOException;

public class UtilityListeners implements Listener {

    /*
    Constructor
     */
    public UtilityListeners() {
        Bukkit.getPluginManager().registerEvents(this, BedWarsPlugin.getInstance());
    }

    /*
    Handlers
     */
    @EventHandler
    public void on(@NotNull PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        if (e.getItem().getType() == Material.MILK_BUCKET) {
            BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player)
                    .flatMap(gameMatch -> gameMatch.getDataOf(player))
                    .ifPresent(GameMatchPlayer::updateMagicMilk);
            e.setReplacement(new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void on(@NotNull PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction().isRightClick() && e.hasItem()) {
            ItemStack itemStack = e.getItem();
            BedbugItem.getFrom(itemStack).ifPresent(data -> {
                e.setCancelled(true);
                Snowball snowball = player.launchProjectile(Snowball.class);
                snowball.setGlowing(true);
                BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player)
                        .flatMap(gameMatch -> gameMatch.getDataOf(player))
                        .ifPresent(gameMatchPlayer -> {
                            new BedbugTask(gameMatchPlayer, snowball);
                        });

                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
            });
            BridgeEggItem.getFrom(itemStack).ifPresent(data -> {
                e.setCancelled(true);
                if (player.getLocation().getPitch() < 35 && player.getLocation().getPitch() > -35) {
                    Egg egg = player.launchProjectile(Egg.class);
                    egg.setBounce(false);
                    egg.setVelocity(egg.getVelocity().normalize().multiply(1.5));
                    egg.setGlowing(true);
                    BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player)
                            .flatMap(gameMatch -> gameMatch.getDataOf(player))
                            .ifPresent(gameMatchPlayer -> {
                                Bukkit.getScheduler().runTaskLater(BedWarsPlugin.getInstance(), () -> new BridgeEggTask(gameMatchPlayer, egg), 3);
                            });

                    player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                } else {
                    player.sendMessage(GameConstants.MESSAGE_BRIDGEEGG_WRONG_ANGLE);
                }
            });
            DreamDefenderItem.getFrom(itemStack).ifPresent(data -> {
                e.setCancelled(true);
                BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player)
                        .flatMap(gameMatch -> gameMatch.getDataOf(player))
                        .ifPresent(gameMatchPlayer -> {
                            IronGolem ironGolem = (IronGolem) player.getWorld().spawnEntity(player.getLocation(), EntityType.IRON_GOLEM);
                            ironGolem.setMetadata("owner", new FixedMetadataValue(BedWarsPlugin.getInstance(), gameMatchPlayer.getUniqueId().toString()));
                            ironGolem.setMetadata("owner-team", new FixedMetadataValue(BedWarsPlugin.getInstance(), gameMatchPlayer.getParent().getGameTeam().name()));
                            ironGolem.setCustomName(gameMatchPlayer.getParent().getGameTeam().getChatColor() + gameMatchPlayer.getParent().getGameTeam().getDisplay() + "'s Dream Defender");
                            ironGolem.setCustomNameVisible(true);
                            ironGolem.setPlayerCreated(true);

                            new DreamDefenderTask(gameMatchPlayer, ironGolem);
                        });
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
            });
            if (e.hasBlock()) {
                CompactTowerItem.getFrom(itemStack).ifPresent(data -> {
                    e.setCancelled(true);
                    BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player)
                            .flatMap(gameMatch -> gameMatch.getDataOf(player))
                            .ifPresent(gameMatchPlayer -> {
                                try {
                                    SchematicUtilities.placeBlocks(player, e.getClickedBlock().getLocation(), gameMatchPlayer.getParent());
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            });
                    player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                });
            }
            if (e.getItem().getType() == Material.FIRE_CHARGE) {
                e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getItem().getAmount() - 1);
                Fireball fireball = e.getPlayer().launchProjectile(Fireball.class);
                fireball.setShooter(e.getPlayer());
                fireball.setIsIncendiary(false);
            }
        }
    }

    @EventHandler
    public void on(@NotNull EntityTargetLivingEntityEvent e) {
        if (!(e.getTarget() instanceof Player player)) return;
        Entity entity = e.getEntity();
        if (entity.hasMetadata("owner") && entity.hasMetadata("owner-team")) {
            GameTeam gameTeam = GameTeam.valueOf(entity.getMetadata("owner-team").get(0).asString());
            BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player)
                    .flatMap(gameMatch -> gameMatch.getTeam(gameTeam))
                    .flatMap(gameMatchTeam -> entity.getWorld().getNearbyEntitiesByType(Player.class, entity.getLocation(), 3, 3, 3)
                            .stream()
                            .filter(possibleTarget -> !gameMatchTeam.hasPlayer(possibleTarget.getUniqueId()))
                            .findAny())
                    .ifPresentOrElse(e::setTarget, () -> e.setCancelled(true));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(@NotNull EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player player) {
            Entity entity = e.getEntity();
            if (entity.hasMetadata("owner") && entity.hasMetadata("owner-team")) {
                GameTeam gameTeam = GameTeam.valueOf(entity.getMetadata("owner-team").get(0).asString());
                BedWarsPlugin.getInstance().getMatchManager().getGameMatch(player)
                        .flatMap(gameMatch -> gameMatch.getTeam(gameTeam))
                        .ifPresent(gameMatchTeam -> {
                            if (gameMatchTeam.hasPlayer(player.getUniqueId())) {
                                e.setCancelled(true);
                            }
                        });
            }
        }
        if (e.getDamager() instanceof IronGolem) {
            e.setDamage(e.getDamage() / 2);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void on(@NotNull MatchBlockPlaceEvent e) {
        Block block = e.getLocation().getBlock();
        if (block.getType() == Material.TNT) {
            block.setType(Material.AIR);
            block.getWorld().spawnEntity(block.getLocation(), EntityType.PRIMED_TNT);
        }
    }

    @EventHandler
    public void on(@NotNull EntityExplodeEvent e) {
        if (e.getEntity() instanceof Fireball fireball && fireball.getShooter() instanceof Player) {
            e.setYield(e.getYield() * 2);
        }
        BedWarsPlugin.getInstance().getMatchManager().getGameMatch(e.getLocation().getWorld()).ifPresent(gameMatch -> {
            if (e.getEntity() instanceof TNTPrimed || e.getEntity() instanceof Fireball) {
                e.blockList().removeIf(block -> !gameMatch.getBlocksSystem().canBreak(block.getLocation()) || block.getType().name().contains("STAINED_GLASS"));

                e.blockList().forEach(block -> {
                    float x = -1.0F + (float) (Math.random() * 2.0D + 0.0D);
                    float y = -1.5F + (float) (Math.random() * 3.0D + 0.0D);
                    float z = -1.0F + (float) (Math.random() * 2.0D + 0.0D);

                    FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(block.getLocation(), block.getBlockData());
                    fallingBlock.setDropItem(false);
                    fallingBlock.setHurtEntities(true);
                    fallingBlock.setVelocity(new Vector(x, y, z));
                });
            }
        });
    }

    @EventHandler
    public void on(@NotNull EntityChangeBlockEvent e) {
        Block block = e.getBlock();
        BedWarsPlugin.getInstance().getMatchManager().getGameMatch(block.getWorld()).ifPresent(gameMatch -> {
            Location location = block.getLocation();
            if (gameMatch.getBlocksSystem().canPlace(location)) {
                gameMatch.getBlocksSystem().placeBlock(location);
            } else {
                e.setCancelled(true);
            }
        });
    }
}
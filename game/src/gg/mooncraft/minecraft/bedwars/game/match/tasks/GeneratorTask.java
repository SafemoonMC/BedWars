package gg.mooncraft.minecraft.bedwars.game.match.tasks;

import lombok.Getter;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.map.point.AbstractMapPoint;
import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;
import gg.mooncraft.minecraft.bedwars.game.GameConstants;
import gg.mooncraft.minecraft.bedwars.game.match.GameMatch;
import gg.mooncraft.minecraft.bedwars.game.match.GeneratorType;
import gg.mooncraft.minecraft.bedwars.game.match.options.MatchOptions;
import gg.mooncraft.minecraft.bedwars.game.match.options.OptionEntry;
import gg.mooncraft.minecraft.bedwars.game.utilities.DisplayUtilities;
import gg.mooncraft.minecraft.bedwars.game.utilities.EntityUtilities;
import gg.mooncraft.minecraft.bedwars.game.utilities.ItemsUtilities;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Getter
public class GeneratorTask extends GameRunnable {

    /*
    Fields
     */
    private final @NotNull GameMatch gameMatch;
    private final @NotNull AbstractMapPoint mapPoint;
    private final @NotNull Location location;
    private final @NotNull GeneratorType type;
    private final @NotNull Function<String, String> placeholders;

    private ArmorStand armorStand;
    private Hologram armorStandHologram;
    private boolean spawnedArmorStand;

    private int countdown;

    /*
    Constructor
     */
    public GeneratorTask(@NotNull GameMatch gameMatch, @NotNull AbstractMapPoint mapPoint, @NotNull GeneratorType type) {
        super();
        this.gameMatch = gameMatch;
        this.mapPoint = mapPoint;
        this.location = gameMatch.getDimension().getLocation(mapPoint.getX(), mapPoint.getY(), mapPoint.getZ(), mapPoint.getYaw(), mapPoint.getPitch());
        this.type = type;
        this.placeholders = line -> line
                .replaceAll("%generator-type%", type.getDisplay())
                .replaceAll("%tier%", type == GeneratorType.DIAMOND ? DisplayUtilities.getLiteral(gameMatch.getGeneratorSystem().getDiamondTier()) : DisplayUtilities.getLiteral(gameMatch.getGeneratorSystem().getEmeraldTier()))
                .replaceAll("%time-left%", String.valueOf(countdown))
                .replaceAll("%time-unit%", countdown != 1 ? "seconds" : "second");
    }

    /*
    Override Methods
     */
    @Override
    public void tick() {
        int tick = getTick();
        if (this.armorStand == null && !spawnedArmorStand) {
            this.spawnedArmorStand = true;

            BedWarsPlugin.getInstance().getScheduler().executeSync(() -> {
                this.armorStand = EntityUtilities.createGeneratorStand(this.location.clone().add(0, 1.5, 0), type.getHeadMaterial());
                this.armorStandHologram = HologramsAPI.createHologram(BedWarsPlugin.getInstance(), this.location.clone().add(0, 4, 0));
                this.armorStandHologram.setAllowPlaceholders(false);

                GameConstants.GENERATOR_HOLOGRAM_LINES.forEach(line -> this.armorStandHologram.appendTextLine(placeholders.apply(line)));
            });
            int countdown = type == GeneratorType.DIAMOND ? MatchOptions.getMatchOption(gameMatch.getGameMode()).getGeneratorDiamondDropRates()[gameMatch.getGeneratorSystem().getDiamondTier() - 1] : MatchOptions.getMatchOption(gameMatch.getGameMode()).getGeneratorEmeraldDropRates()[gameMatch.getGeneratorSystem().getEmeraldTier() - 1];
            OptionEntry<TimeUnit, Integer> optionEntry = type == GeneratorType.DIAMOND ? MatchOptions.getMatchOption(gameMatch.getGameMode()).getGeneratorDiamondStartDelay() : MatchOptions.getMatchOption(gameMatch.getGameMode()).getGeneratorEmeraldStartDelay();
            this.countdown = (int) (countdown + optionEntry.getKey().toSeconds(optionEntry.getValue()));
            return;
        }
        if (this.armorStand == null || this.armorStandHologram == null) return;

        // Update head pose
        this.armorStand.setHeadPose(this.armorStand.getHeadPose().add(0, 0.2, 0));

        // Update hologram
        if (tick == 20) {
            this.countdown--;
            if (this.countdown == 0) {
                this.countdown = type == GeneratorType.DIAMOND ? MatchOptions.getMatchOption(gameMatch.getGameMode()).getGeneratorDiamondDropRates()[gameMatch.getGeneratorSystem().getDiamondTier() - 1] : MatchOptions.getMatchOption(gameMatch.getGameMode()).getGeneratorEmeraldDropRates()[gameMatch.getGeneratorSystem().getEmeraldTier() - 1];

                ItemStack itemStack = ItemsUtilities.createPureItem(type.getDropMaterial());
                EntityUtilities.spawnItemStack(this.location, itemStack);
            }
            forceUpdateHologram();
        }
    }

    /*
    Methods
     */
    public void forceUpdateHologram() {
        if (this.armorStandHologram == null) return;
        for (int i = 0; i < armorStandHologram.size(); i++) {
            TextLine textLine = (TextLine) armorStandHologram.getLine(i);
            textLine.setText(placeholders.apply(GameConstants.GENERATOR_HOLOGRAM_LINES.get(i)));
        }
    }
}
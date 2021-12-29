package gg.mooncraft.minecraft.bedwars.game.events;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import gg.mooncraft.minecraft.bedwars.game.BedWarsPlugin;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class EventsAPI {

    public static void callEventSync(Event event) {
        if (Bukkit.isPrimaryThread()) {
            event.callEvent();
            return;
        }
        BedWarsPlugin.getInstance().getScheduler().executeSync(event::callEvent);
    }
}
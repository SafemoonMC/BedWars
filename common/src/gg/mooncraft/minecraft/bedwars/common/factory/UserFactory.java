package gg.mooncraft.minecraft.bedwars.common.factory;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.UserDAO;
import gg.mooncraft.minecraft.bedwars.data.user.BedWarsUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class UserFactory {

    /*
    Fields
     */
    private final @NotNull List<BedWarsUser> userList = new ArrayList<>();

    /*
    Methods
     */
    public void loadUser(@NotNull Player player) {
        UserDAO.read(player.getUniqueId()).thenAccept(this.userList::add);
    }

    public void unloadUser(@NotNull Player player) {
        this.userList.removeIf(bedWarsUser -> bedWarsUser.getUniqueId().equals(player.getUniqueId()));
    }

    public @NotNull Optional<BedWarsUser> getUser(@NotNull Player player) {
        return this.userList.stream().filter(bedWarsUser -> bedWarsUser.getUniqueId().equals(player.getUniqueId())).findFirst();
    }
}
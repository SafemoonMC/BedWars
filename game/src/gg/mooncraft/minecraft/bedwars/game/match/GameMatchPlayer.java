package gg.mooncraft.minecraft.bedwars.game.match;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Getter
public final class GameMatchPlayer {

    /*
    Fields
     */
    private final @NotNull UUID uniqueId;
    private @Nullable Player player;
    private @NotNull PlayerStatus playerStatus;

    /*
    Constructor
     */
    public GameMatchPlayer(@NotNull UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.playerStatus = PlayerStatus.ALIVE;
    }

    /*
    Methods
     */
    public void setStatus(@NotNull PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }

    public @NotNull Optional<Player> getPlayer() {
        if (this.player == null || !this.player.isOnline()) {
            this.player = Bukkit.getPlayer(uniqueId);
        }
        return Optional.ofNullable(this.player);
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
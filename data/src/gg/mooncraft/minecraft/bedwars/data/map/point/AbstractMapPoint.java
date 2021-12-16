package gg.mooncraft.minecraft.bedwars.data.map.point;

import lombok.AllArgsConstructor;
import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.database.entities.EntityChild;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.minecraft.bedwars.data.GameMode;
import gg.mooncraft.minecraft.bedwars.data.map.MapPointsContainer;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class AbstractMapPoint implements EntityChild<MapPointsContainer> {

    /*
    Fields
     */
    private final @NotNull MapPointsContainer parent;
    private final long id;
    private final @NotNull GameMode gameMode;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    /*
    Override Methods
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractMapPoint that = (AbstractMapPoint) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
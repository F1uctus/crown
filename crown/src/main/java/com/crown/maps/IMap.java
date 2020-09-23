package com.crown.maps;

import org.jetbrains.annotations.NotNull;

public interface IMap {
    boolean inBounds(int x, int y);

    boolean inBounds(int x, int y, int z);

    boolean inBounds(Point3D point);

    boolean isWalkable(@NotNull Point3D point);

    boolean isTransparent(@NotNull Point3D point);
}

package com.crown.maps.pathfinding;

import com.crown.maps.Point3D;

public interface IPathfinder {
    Point3D[] findPath(
        final Point3D startPt,
        final Point3D endPt,
        final int radius
    );
}

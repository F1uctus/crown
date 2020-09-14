package com.crown.maps.pathfinding;

import com.crown.maps.Map;
import com.crown.maps.Point3D;
import com.crown.maps.pathfinding.heuristics.IAStarHeuristic;

import java.util.ArrayList;

public abstract class IPathfinder {
    protected final Map map;
    protected final IAStarHeuristic heuristic;
    protected final boolean includeDiagonals;
    protected final Point3D mapMaxPoint;

    IPathfinder(
        final Map map,
        final IAStarHeuristic heuristic,
        final boolean includeDiagonals
    ) {
        this.map = map;
        this.heuristic = heuristic;
        this.includeDiagonals = includeDiagonals;
        mapMaxPoint = new Point3D(map.xSize, map.ySize, map.zSize).minus(1);
    }

//    Point3D[] findPath(
//        final Point3D startPt,
//        final Point3D endPt,
//        final int radius
//    );

    protected static Point3D[] createPath(PathNode end) {
        if (end == null)
            return null;

        final var v = new ArrayList<Point3D>();
        while (end != null) {
            v.add(end.point);
            end = end.prev;
        }
        final var result = new Point3D[v.size()];
        for (int i = v.size() - 1; i >= 0; i--) {
            result[i] = v.get(i);
        }
        return result;
    }
}

package com.crown.maps.pathfinding.heuristics;

import com.crown.maps.Point3D;

/**
 * 3D Manhattan heuristic for A*.
 */
public class ManhattanHeuristic implements IAStarHeuristic {
    private static final ManhattanHeuristic instance = new ManhattanHeuristic();

    public static ManhattanHeuristic get() {
        return instance;
    }

    private ManhattanHeuristic() {
    }

    @Override
    public double apply(Point3D startPoint, Point3D endPoint) {
        var d = startPoint.minus(endPoint).abs();
        return d.x + d.y + d.z;
    }
}

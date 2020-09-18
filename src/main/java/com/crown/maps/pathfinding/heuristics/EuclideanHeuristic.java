package com.crown.maps.pathfinding.heuristics;

import com.crown.maps.Point3D;

/**
 * 3D Euclidean heuristic for A*.
 */
public class EuclideanHeuristic implements IAStarHeuristic {
    private static final EuclideanHeuristic instance = new EuclideanHeuristic();

    public static EuclideanHeuristic get() {
        return instance;
    }

    private EuclideanHeuristic() {
    }

    @Override
    public double apply(Point3D startPoint, Point3D endPoint) {
        Point3D d = startPoint.minus(endPoint).abs();
        return Math.sqrt(Math.pow(d.x, 2) + Math.pow(d.y, 2) + Math.pow(d.z, 2));
    }
}

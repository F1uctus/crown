package com.crown.maps.pathfinding.heuristics;

import com.crown.maps.Point3D;

public class EuclideanHeuristic implements IAStarHeuristic {
    @Override
    public double apply(Point3D startPoint, Point3D endPoint) {
        var d = startPoint.minus(endPoint).abs();
        return Math.sqrt(Math.pow(d.x, 2) + Math.pow(d.y, 2) + Math.pow(d.z, 2));
    }
}

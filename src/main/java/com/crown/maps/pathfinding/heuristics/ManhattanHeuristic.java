package com.crown.maps.pathfinding.heuristics;

import com.crown.maps.Point3D;

public class ManhattanHeuristic implements IAStarHeuristic {
    @Override
    public double apply(Point3D startPoint, Point3D endPoint) {
        var d = startPoint.minus(endPoint).abs();
        return d.x + d.y + d.z;
    }
}

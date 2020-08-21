package com.crown.maps.pathfinding.heuristics;

import com.crown.maps.Point3D;

public class OctileHeuristic implements IAStarHeuristic {
    @Override
    public double apply(Point3D startPoint, Point3D endPoint) {
        var d = startPoint.minus(endPoint).abs();
        // sort deltas
        int d1 = Math.min(d.x, Math.min(d.y, d.z));
        int d3 = Math.max(d.x, Math.max(d.y, d.z));
        int d2 = d.x + d.y + d.z - d1 - d3;
        // Reference: Real-Time, 3D Path Planning for UAVs, by Jay Lanzafane
        return d.x + d.y + d.z - (3 - Math.sqrt(3)) * d1 - (2 - Math.sqrt(2)) * d2;
    }
}

package com.crown.maps.pathfinding.heuristics;

import com.crown.maps.Point3D;

public interface IAStarHeuristic {
    double apply(Point3D startPoint, Point3D endPoint);
}

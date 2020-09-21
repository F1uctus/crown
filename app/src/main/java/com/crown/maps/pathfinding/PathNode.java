package com.crown.maps.pathfinding;

import com.crown.maps.Point3D;
import org.jetbrains.annotations.NotNull;

public class PathNode implements HeapNode {
    double g;
    double h;
    Point3D point;
    double cost;
    PathNode prev;
    int heapIndex;

    public PathNode(final Point3D point) {
        this(point, 0.0);
    }

    public PathNode(final Point3D point, final double g) {
        this.point = point;
        this.g = g;
    }

    public void computeCost() {
        this.cost = this.h + this.g;
    }

    public int compareTo(final @NotNull HeapNode node) {
        return (int) Math.signum(this.cost - ((PathNode) node).cost);
    }

    public int getHeapIndex() {
        return this.heapIndex;
    }

    public void setHeapIndex(final int heapIndex) {
        this.heapIndex = heapIndex;
    }
}
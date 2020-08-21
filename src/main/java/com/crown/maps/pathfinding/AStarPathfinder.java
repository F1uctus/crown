package com.crown.maps.pathfinding;

import com.crown.maps.Map;
import com.crown.maps.Point3D;
import com.crown.maps.pathfinding.heuristics.IAStarHeuristic;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AStarPathfinder implements IPathfinder {
    private final Map map;
    private final Point3D mapMaxPoint;
    private final boolean includeDiagonals;

    private final IAStarHeuristic heuristic;

    public AStarPathfinder(
        final Map map,
        IAStarHeuristic heuristic
    ) {
        this(map, heuristic, true);
    }

    public AStarPathfinder(
        final Map map,
        final IAStarHeuristic heuristic,
        final boolean includeDiagonals
    ) {
        this.map = map;
        mapMaxPoint = new Point3D(map.xSize, map.ySize, map.zSize).minus(1);
        this.heuristic = heuristic;
        this.includeDiagonals = includeDiagonals;
    }

    public Point3D[] findPath(final Point3D startPt, final Point3D endPt) {
        return findPath(startPt, endPt, -1);
    }

    public Point3D[] findPath(
        final Point3D startPt,
        final Point3D endPt,
        final int radius
    ) {
        if (!this.map.contains(startPt) || !this.map.contains(endPt)) {
            return null;
        }
        if (radius == 0) {
            return new Point3D[] { startPt };
        }

        final Point3D minPt, maxPt, hashSize;
        if (radius < 0) {
            minPt = Point3D.ZERO;
            maxPt = mapMaxPoint;
            hashSize = mapMaxPoint.plus(1);
        } else {
            minPt = Point3D.max(Point3D.ZERO, startPt.minus(radius));
            maxPt = Point3D.min(mapMaxPoint, startPt.plus(radius));
            hashSize = maxPt.minus(minPt).plus(1);
        }

        final PathNode[][][] nodeHash = new PathNode
            [hashSize.x]
            [hashSize.y]
            [hashSize.z];
        final SimpleHeap<HeapNode> open = new SimpleHeap<>(1000);
        final PathNode startNode = new PathNode(startPt, 0.0);
        startNode.h = heuristic.apply(startPt, endPt);
        startNode.computeCost();
        open.add(startNode);
        nodeHash
            [startPt.x - minPt.x]
            [startPt.y - minPt.y]
            [startPt.z - minPt.z] = startNode;
        while (open.size() > 0) {
            final PathNode step = (PathNode) open.poll();
            if (step.point.equals(endPt)) {
                return this.createPath(step);
            }

            // @formatter:off
            // trying to move in every possible direction
            for (int dx = -1; dx <= 1; dx++)
            for (int dy = -1; dy <= 1; dy++)
            for (int dz = -1; dz <= 1; dz++) {
                // @formatter:on
                boolean isMoveDiagonal = dx != 0 && dy != 0
                    || dx != 0 && dz != 0
                    || dy != 0 && dz != 0;

                // skip 0-moves & diagonals if not allowed
                if (dx == 0 && dy == 0 && dz == 0
                    || isMoveDiagonal && !this.includeDiagonals) {
                    continue;
                }

                final Point3D target = step.point.plus(dx, dy, dz);

                if (target.isInsideOf(minPt, maxPt) && this.map.contains(target)) {
                    // the only allowed obstacle is the end point
                    if (!target.equals(endPt) && this.map.isObstacle(target)) {
                        continue;
                    }

                    final double thisCost = isMoveDiagonal ? 1.1 : 1.0;
                    final Point3D delta = target.minus(minPt);
                    if (nodeHash[delta.x][delta.y][delta.z] == null) {
                        final PathNode n1 = new PathNode(target, step.g + thisCost);
                        n1.prev = step;
                        n1.h = heuristic.apply(target, endPt);
                        n1.computeCost();
                        open.add(n1);
                        nodeHash[delta.x][delta.y][delta.z] = n1;
                    } else {
                        final PathNode n1 = nodeHash[delta.x][delta.y][delta.z];
                        if (n1.g > step.g + thisCost) {
                            n1.g = step.g + thisCost;
                            n1.computeCost();
                            n1.prev = step;
                            if (open.contains(n1)) {
                                open.adjust(n1);
                            } else {
                                open.add(n1);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private Point3D[] createPath(PathNode end) {
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

    private static class PathNode implements HeapNode {
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
}

package com.crown.maps.pathfinding;

import com.crown.maps.Map;
import com.crown.maps.Point3D;
import com.crown.maps.pathfinding.heuristics.IAStarHeuristic;
import com.crown.maps.pathfinding.heuristics.OctileHeuristic;

import java.util.ArrayList;

/**
 * Famous A* pathfinding implementation for the 3D grid map.
 */
public class AStarPathfinder extends IPathfinder {
    /**
     * Initialize a new A* pathfinder for given map.
     * Uses 3D-Octile heuristic by default.
     */
    public AStarPathfinder(final Map map) {
        this(map, OctileHeuristic.get(), true);
    }

    /**
     * Initialize a new A* pathfinder for given map.
     * You can specify any heuristic from [heuristics] package.
     * Diagonal steps are enabled.
     */
    public AStarPathfinder(final Map map, IAStarHeuristic heuristic) {
        this(map, heuristic, true);
    }

    /**
     * Initialize a new A* pathfinder for given map.
     * You can specify any heuristic from [heuristics] package,
     * and allow/restrict pathfinder to use diagonal steps.
     */
    public AStarPathfinder(
        final Map map,
        final IAStarHeuristic heuristic,
        final boolean includeDiagonals
    ) {
        super(map, heuristic, includeDiagonals);
    }

    /**
     * Finds an optimal path between given points.
     */
    public Point3D[] findPath(final Point3D startPt, final Point3D endPt) {
        return findPath(startPt, endPt, -1);
    }

    /**
     * Finds an optimal path between given points.
     */
    public Point3D[] findPath(
        final Point3D startPt,
        final Point3D endPt,
        final int radius
    ) {
        if (!this.map.inBounds(startPt) || !this.map.inBounds(endPt)) {
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
        final SimpleHeap<PathNode> open = new SimpleHeap<>(1000);
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
                return createPath(step);
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

                if (target.isInsideOf(minPt, maxPt) && this.map.inBounds(target)) {
                    // the only allowed obstacle is the end point
                    if (!target.equals(endPt) && this.map.isWalkable(target)) {
                        continue;
                    }

                    final double defaultCost = isMoveDiagonal ? 1.1 : 1.0;
                    final Point3D delta = target.minus(minPt);
                    if (nodeHash[delta.x][delta.y][delta.z] == null) {
                        final PathNode n1 = new PathNode(target, step.g + defaultCost);
                        n1.prev = step;
                        n1.h = heuristic.apply(target, endPt);
                        n1.computeCost();
                        open.add(n1);
                        nodeHash[delta.x][delta.y][delta.z] = n1;
                    } else {
                        final PathNode n1 = nodeHash[delta.x][delta.y][delta.z];
                        if (n1.g > step.g + defaultCost) {
                            n1.g = step.g + defaultCost;
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
}

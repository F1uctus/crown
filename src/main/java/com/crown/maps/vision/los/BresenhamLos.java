package com.crown.maps.vision.los;

import com.crown.maps.IMap;
import com.crown.maps.Point3D;
import com.crown.maps.vision.ILineOfSight;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Bresenham LOS class.
 * Checks if a bresenham line can be drawn from
 * source to destination. If symmetric, also checks
 * the alternate Bresenham line from destination to source.
 */
public class BresenhamLos implements ILineOfSight {
    private final boolean isSymmetric;

    public static final BresenhamLos regular = new BresenhamLos(false);
    public static final BresenhamLos symmetric = new BresenhamLos(true);

    private BresenhamLos(final boolean isSymmetric) {
        this.isSymmetric = isSymmetric;
    }

    public Pair<Boolean, Point3D[]> exists(
        final IMap map,
        Point3D start,
        Point3D end
    ) {
        final var forwardPath = BresenhamLine.getFor(start, end);
        boolean los = false;
        for (Point3D point : forwardPath) {
            if (point.equals(start)) {
                continue;
            } else if (point.equals(end)) {
                los = true;
                break;
            }
            if (!map.isTransparent(point)) {
                break;
            }
        }
        Point3D[] path = forwardPath;
        if (!los && isSymmetric) {
            // Direct path failed, try alternate path
            final var backwardPath = BresenhamLine.getFor(end, start);
            for (int i = backwardPath.length - 1; i >= 0; i--) {
                var point = backwardPath[i];
                if (point.equals(start)) {
                    continue;
                } else if (point.equals(end)) {
                    los = true;
                    break;
                }
                if (!map.isTransparent(point)) {
                    break;
                }
            }
            if (backwardPath.length > forwardPath.length) {
                path = backwardPath;
            }
        }
        return Pair.of(los, path);
    }
}

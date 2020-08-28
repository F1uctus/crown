package com.crown.maps.vision.los;

import com.crown.maps.IMap;
import com.crown.maps.Point3D;
import com.crown.maps.vision.ILineOfSight;

import java.util.List;
import java.util.Vector;

/**
 * Bresenham LOS class.
 * Checks if a bresenham line can be drawn from
 * source to destination. If symmetric, also checks
 * the alternate Bresenham line from destination to source.
 */
public class BresenhamLos implements ILineOfSight {
    private final boolean symmetric;
    private Vector<Point3D> path;

    public BresenhamLos(final boolean symmetric) {
        this.symmetric = symmetric;
    }

    public boolean exists(
        final IMap map,
        Point3D start,
        Point3D end,
        final boolean savePath
    ) {
        if (savePath) {
            path = new Vector<>();
        }
        final var forwardPath = BresenhamLine.getFor(start, end);
        boolean los = false;
        for (Point3D point : forwardPath) {
            if (savePath) {
                path.add(point);
            }
            if (point.equals(end)) {
                los = true;
                break;
            }
            if (!map.isTransparent(point)) {
                break;
            }
        }
        if (!los && symmetric) {
            // Direct path failed, try alternate path
            final var p1 = BresenhamLine.getFor(end, start);
            final Vector<Point3D> oldPath = path;
            path = new Vector<>();
            for (int i = p1.length - 1; i >= 0; i--) {
                var point = p1[i];
                if (savePath) {
                    path.add(point);
                }
                if (point.equals(end)) {
                    los = true;
                    break;
                }
                if (!map.isTransparent(point)) {
                    break;
                }
            }
            if (savePath) {
                path = oldPath.size() > path.size() ? oldPath : path;
            }
        }
        return los;
    }

    public List<Point3D> getPath() {
        return path;
    }
}

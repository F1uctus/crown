package com.crown.maps.vision.los;

import com.crown.maps.IMap;
import com.crown.maps.Point3D;
import com.crown.maps.vision.ILineOfSight;

import java.util.List;
import java.util.Vector;

/**
 * Bresenham LOS.
 * Tries to reach destination along first path. If blocked,
 * shifts to alternate path. If that is blocked, shift to first
 * path again. Fails only if both are blocked at a point.
 */
public class BresenhamOpportunisticLos implements ILineOfSight {
    private Vector<Point3D> path;

    public boolean exists(
        final IMap map,
        Point3D start,
        Point3D end,
        final boolean savePath
    ) {
        if (savePath) {
            path = new Vector<>();
        }

        Point3D[] lineForward = BresenhamLine.getFor(start, end);
        Point3D[] lineBackward = BresenhamLine.getFor(end, start);
        int len = Math.min(lineForward.length, lineBackward.length);

        boolean los = false;
        boolean alternatePath = false;
        for (int i = 0; i < len; i++) {
            Point3D pointForward = lineForward[i];
            Point3D pointBackward = lineBackward[len - i - 1];
            if (pointForward.equals(end)) {
                if (savePath) {
                    path.add(pointForward);
                }
                los = true;
                break;
            }

            boolean backwardVisible = map.isTransparent(pointBackward);
            boolean forwardVisible = map.isTransparent(pointForward);

            if (alternatePath && backwardVisible) {
                if (savePath) {
                    path.add(pointBackward);
                }
                continue;
            } else {
                alternatePath = false;
            }
            if (forwardVisible) {
                if (savePath) {
                    path.add(pointForward);
                }
                continue;
            }
            if (backwardVisible) {
                if (savePath) {
                    path.add(pointBackward);
                }
                alternatePath = true;
                continue;
            }
            if (savePath) {
                path.add(pointBackward);
            }
            break;
        }

        return los;
    }

    public List<Point3D> getPath() {
        return path;
    }
}

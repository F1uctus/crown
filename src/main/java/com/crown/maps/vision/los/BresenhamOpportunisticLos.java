package com.crown.maps.vision.los;

import com.crown.maps.IMap;
import com.crown.maps.Point3D;
import com.crown.maps.vision.ILineOfSight;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Vector;

/**
 * Bresenham LOS.
 * Tries to reach destination along first path. If blocked,
 * shifts to alternate path. If that is blocked, shift to first
 * path again. Fails only if both are blocked at a point.
 */
public class BresenhamOpportunisticLos implements ILineOfSight {
    public Pair<Boolean, Point3D[]> exists(
        final IMap map,
        Point3D start,
        Point3D end
    ) {
        var path = new Vector<Point3D>();

        Point3D[] lineForward = BresenhamLine.getFor(start, end);
        Point3D[] lineBackward = BresenhamLine.getFor(end, start);
        int len = Math.min(lineForward.length, lineBackward.length);

        boolean los = false;
        boolean alternatePath = false;
        for (int i = 0; i < len; i++) {
            Point3D pointForward = lineForward[i];
            Point3D pointBackward = lineBackward[len - i - 1];
            if (pointForward.equals(end)) {
                path.add(pointForward);
                los = true;
                break;
            }

            boolean backwardVisible = map.isTransparent(pointBackward);
            boolean forwardVisible = map.isTransparent(pointForward);

            if (alternatePath && backwardVisible) {
                path.add(pointBackward);
                continue;
            } else {
                alternatePath = false;
            }
            if (forwardVisible) {
                path.add(pointForward);
                continue;
            }
            if (backwardVisible) {
                path.add(pointBackward);
                alternatePath = true;
                continue;
            }
            path.add(pointBackward);
            break;
        }

        return Pair.of(los, path.toArray(new Point3D[0]));
    }
}

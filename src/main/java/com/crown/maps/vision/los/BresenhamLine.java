package com.crown.maps.vision.los;

import com.crown.maps.Point3D;

import java.util.ArrayList;

/**
 * Bresenham's line drawing algorithm for 3D.
 */
public final class BresenhamLine {
    public static Point3D[] getFor(Point3D start, Point3D end) {
        int x = start.x;
        int y = start.y;
        int z = start.z;

        var points = new ArrayList<Point3D>();
        points.add(start);

        final Point3D d = end.minus(start);
        final Point3D dSign = d.signum();
        final Point3D dAbs = d.abs();

        final int px, py, pz, d0, d1, d2, jx1, jy1, jz1, jx2, jy2, jz2;
        if (dAbs.x > dAbs.y && dAbs.x > dAbs.z) {
            px = dSign.x;
            py = 0;
            pz = 0;
            jx1 = 0;
            jy1 = dSign.y;
            jz1 = 0;
            jx2 = 0;
            jy2 = 0;
            jz2 = dSign.z;
            d0 = dAbs.x;
            d1 = dAbs.y;
            d2 = dAbs.z;
        } else if (dAbs.y > dAbs.x && dAbs.y > dAbs.z) {
            px = 0;
            py = dSign.y;
            pz = 0;
            jx1 = dSign.x;
            jy1 = 0;
            jz1 = 0;
            jx2 = 0;
            jy2 = 0;
            jz2 = dSign.z;
            d0 = dAbs.y;
            d1 = dAbs.x;
            d2 = dAbs.z;
        } else {
            px = 0;
            py = 0;
            pz = dSign.z;
            jx1 = dSign.x;
            jy1 = 0;
            jz1 = 0;
            jx2 = 0;
            jy2 = dSign.y;
            jz2 = 0;
            d0 = dAbs.z;
            d1 = dAbs.x;
            d2 = dAbs.y;
        }

        int delta1 = d0 / 2;
        int delta2 = delta1;

        for (int p = 0; p < d0; p++) {
            delta1 -= d1;
            delta2 -= d2;

            if (delta1 < 0) {
                delta1 += d0;
                x += jx1;
                y += jy1;
                z += jz1;
            }

            if (delta2 < 0) {
                delta2 += d0;
                x += jx2;
                y += jy2;
                z += jz2;
            }

            x += px;
            y += py;
            z += pz;

            points.add(new Point3D(x, y, z));
        }

        return points.toArray(new Point3D[0]);
    }
}
package com.crown.maps.vision.los;

import com.crown.maps.Point3D;

import java.util.ArrayList;

/**
 * Bresenham's line drawing algorithm for 3D.
 */
public final class BresenhamLine {
    public static Point3D[] getFor(Point3D start, Point3D end) {
        int pointX = start.x;
        int pointY = start.y;
        int pointZ = start.z;
        final Point3D d = end.minus(start);
        final Point3D inc = d.positiveSign();
        final Point3D abs = d.abs();
        int dx2 = abs.x << 1;
        int dy2 = abs.y << 1;
        int dz2 = abs.z << 1;

        var points = new ArrayList<Point3D>();

        if (abs.x >= abs.y && abs.x >= abs.z) {
            int err1 = dy2 - abs.x;
            int err2 = dz2 - abs.x;
            for (int i = 0; i < abs.x; i++) {
                points.add(new Point3D(pointX, pointY, pointZ));
                if (err1 > 0) {
                    pointY += inc.y;
                    err1 -= dx2;
                }
                if (err2 > 0) {
                    pointZ += inc.z;
                    err2 -= dx2;
                }
                err1 += dy2;
                err2 += dz2;
                pointX += inc.x;
            }
        } else if (abs.y >= abs.x && abs.y >= abs.z) {
            int err1 = dx2 - abs.y;
            int err2 = dz2 - abs.y;
            for (int i = 0; i < abs.y; i++) {
                points.add(new Point3D(pointX, pointY, pointZ));
                if (err1 > 0) {
                    pointX += inc.x;
                    err1 -= dy2;
                }
                if (err2 > 0) {
                    pointZ += inc.z;
                    err2 -= dy2;
                }
                err1 += dx2;
                err2 += dz2;
                pointY += inc.y;
            }
        } else {
            int err1 = dy2 - abs.z;
            int err2 = dx2 - abs.z;
            for (int i = 0; i < abs.z; i++) {
                points.add(new Point3D(pointX, pointY, pointZ));
                if (err1 > 0) {
                    pointY += inc.y;
                    err1 -= dz2;
                }
                if (err2 > 0) {
                    pointX += inc.x;
                    err2 -= dz2;
                }
                err1 += dy2;
                err2 += dx2;
                pointZ += inc.z;
            }
        }
        points.add(new Point3D(pointX, pointY, pointZ));
        return points.toArray(new Point3D[0]);
    }
}
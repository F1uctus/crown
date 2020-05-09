package com.crown.common.utils;

import com.crown.maps.Map;
import com.crown.maps.MapObject;
import com.crown.maps.MapWeight;
import com.crown.maps.Point3D;

import java.util.HashSet;

/**
 * Handy extensions for {@link java.util.Random}.
 */
public class Random {
    private static final HashSet<Integer> ids = new HashSet<>();
    public static final java.util.Random rnd = new java.util.Random();

    /**
     * Returns a random integer in range [min, max).
     */
    public static int getInt(int min, int max) {
        return rnd.nextInt(max - min) + min;
    }

    /**
     * Returns a random id for game object
     * (in bounds of {@link Integer} type - should be unique).
     */
    public static int getId() {
        int id = rnd.nextInt(Integer.MAX_VALUE);
        while (ids.contains(id)) {
            id = rnd.nextInt(Integer.MAX_VALUE);
        }
        ids.add(id);
        return id;
    }

    /**
     * Returns a [truly] random point on given map.
     */
    public static Point3D getPoint(Map m) {
        return new Point3D(
            rnd.nextInt(m.xSize),
            rnd.nextInt(m.ySize),
            rnd.nextInt(m.zSize)
        );
    }

    /**
     * Returns any point of map not occupied by obstacle.
     * If failed attempts count reaches map volume, null is returned.
     */
    public static Point3D getFreePoint(Map m) {
        return getFreePoint(m, new Point3D(-1, -1, -1));
    }

    /**
     * Returns any point of map not occupied by obstacle.
     * If failed attempts count reaches map volume, null is returned.
     *
     * @param fixedPt Specifies coordinates that should be fixed, not random.
     *                -1 for each coordinate means that it's random.
     *                (e.g to get point with z = 1 exactly,
     *                invoke it with Point3D(-1, -1, 1)).
     */
    public static Point3D getFreePoint(Map m, Point3D fixedPt) {
        return getFreePoint(
            m,
            fixedPt,
            m.xSize * m.ySize * m.zSize
        );
    }

    /**
     * Returns any point of map not occupied by obstacle.
     * If failed attempts count reaches max value, null is returned.
     *
     * @param fixedPt Specifies coordinates that should be fixed, not random.
     *                -1 for each coordinate means that it's random.
     *                (e.g to get point with z = 1 exactly,
     *                invoke it with Point3D(-1, -1, 1)).
     */
    public static Point3D getFreePoint(
        Map m,
        Point3D fixedPt,
        long maxAttempts
    ) {
        Point3D pt;
        MapObject obj;
        long attempts = -1;
        do {
            if (++attempts == maxAttempts) {
                return null;
            }

            pt = Random.getPoint(m);
            if (fixedPt.x != -1) {
                pt.x = fixedPt.x;
            }
            if (fixedPt.y != -1) {
                pt.y = fixedPt.y;
            }
            if (fixedPt.z != -1) {
                pt.z = fixedPt.z;
            }
            obj = m.get(pt);
        } while (obj != null && obj.getMapWeight() == MapWeight.OBSTACLE);

        return pt;
    }
}

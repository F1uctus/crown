package com.crown.common.utils;

import com.crown.maps.Map;
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
        int id = getInt(Integer.MIN_VALUE + 1, Integer.MAX_VALUE);
        while (ids.contains(id)) {
            id = getInt(Integer.MIN_VALUE + 1, Integer.MAX_VALUE);
        }
        ids.add(id);
        return id;
    }

    /**
     * Returns a random point on given map.
     */
    public static Point3D getPoint(Map m) {
        return new Point3D(
            rnd.nextInt(m.xSize),
            rnd.nextInt(m.ySize),
            rnd.nextInt(m.zSize)
        );
    }
}

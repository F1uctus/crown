package com.crown.common.utils;

import com.crown.maps.*;

import java.util.HashSet;
import java.util.UUID;

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
     * Returns a random UUID for game object.
     */
    public static UUID getId() {
        return UUID.randomUUID();
    }

    /**
     * Returns a random point on given map.
     */
    public static Point3D getPoint(Map map) {
        return new Point3D(
            rnd.nextInt(map.xSize),
            rnd.nextInt(map.ySize),
            rnd.nextInt(map.zSize)
        );
    }

    /**
     * Returns a random point on given map for object with specified size.
     */
    public static Point3D getPoint(Map map, int xSize, int ySize) {
        return new Point3D(
            rnd.nextInt(map.xSize - xSize),
            rnd.nextInt(map.ySize - ySize),
            rnd.nextInt(map.zSize)
        );
    }

    /**
     * Returns a random point of map not occupied by obstacle.
     */
    public static Point3D getFreePoint(Map map) {
        Point3D pt;
        MapObject obj;
        long attempts = -1;
        do {
            if (++attempts == map.xSize * map.ySize * map.zSize) {
                return null;
            }

            pt = Random.getPoint(map);
            obj = map.get(pt);
        } while (obj != null && obj.getMapWeight() == MapWeight.OBSTACLE);

        return pt;
    }

    /**
     * Returns a random point of map not occupied by obstacle
     * for object with specified size.
     */
    public static Point3D getFreePoint(Map map, int xSize, int ySize) {
        Point3D pt;
        MapObject obj;
        long attempts = -1;
        do {
            if (++attempts == map.xSize * map.ySize * map.zSize) {
                return null;
            }

            pt = Random.getPoint(map, xSize, ySize);
            obj = map.get(pt);
        } while (obj != null && obj.getMapWeight() == MapWeight.OBSTACLE);

        return pt;
    }
}

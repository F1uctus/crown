package com.crown.maps;

public enum Direction {
    none(0, 0, 0),

    /**
     * North-West. ⬉
     */
    nw(1, 1, 0),

    /**
     * North. ⬆
     */
    n(0, 1, 0),

    /**
     * North-East. ⬈
     */
    ne(1, 1, 0),

    /**
     * West. ⬅
     */
    w(-1, 0, 0),

    /**
     * East. ➡
     */
    e(1, 0, 0),

    /**
     * South-West. ⬋
     */
    sw(-1, -1, 0),

    /**
     * South. ⬇
     */
    s(0, -1, 0),

    /**
     * South-East. ⬊
     */
    se(1, -1, 0),

    up(0, 0, 1),
    down(0, 0, -1);

    public final Point3D point;

    Direction(int x, int y, int z) {
        point = new Point3D(x, y, z);
    }

    public static Direction fromCoordinates(int x, int y, int z) {
        Direction dir;
        if (y < 0) {
            if (x < 0) dir = nw; // ⬉
            else if (x == 0) dir = n; // ⬆
            else dir = ne; // ⬈
        } else if (y == 0) {
            if (x < 0) dir = w; // ⬅
            else if (x == 0) dir = none;
            else dir = e; // ➡
        } else { // y > 0
            if (x < 0) dir = sw; // ⬋
            else if (x == 0) dir = s; // ⬇
            else dir = se; // ⬊
        }
        if (z > 0 && dir == none) return up;
        if (z < 0 && dir == none) return down;
        return dir;
    }

    public static Direction fromPoint(Point3D point) {
        return fromCoordinates(point.x, point.y, point.z);
    }
}

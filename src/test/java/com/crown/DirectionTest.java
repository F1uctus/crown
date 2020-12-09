package com.crown;

import com.crown.maps.Direction;
import com.crown.maps.Point3D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectionTest {
    @Test
    public void testFromPoint() {
        assertEquals(Direction.fromPoint(Point3D.ZERO), Direction.none);

        assertEquals(Direction.fromPoint(Point3D.ZERO.withZ(1)), Direction.up);

        assertEquals(Direction.fromPoint(Point3D.ZERO.withZ(-1)), Direction.down);

        // north
        Point3D point = new Point3D(0, -10, 0);
        assertEquals(Direction.fromPoint(point), Direction.n);

        point = new Point3D(0, -1, 0);
        assertEquals(Direction.fromPoint(point), Direction.n);

        // north-east
        point = new Point3D(10, -10, 0);
        assertEquals(Direction.fromPoint(point), Direction.ne);

        point = new Point3D(1, -1, 0);
        assertEquals(Direction.fromPoint(point), Direction.ne);

        // north-west
        point = new Point3D(-10, -10, 0);
        assertEquals(Direction.fromPoint(point), Direction.nw);

        point = new Point3D(-1, -1, 0);
        assertEquals(Direction.fromPoint(point), Direction.nw);

        // south
        point = new Point3D(0, 10, 0);
        assertEquals(Direction.fromPoint(point), Direction.s);

        point = new Point3D(0, 1, 0);
        assertEquals(Direction.fromPoint(point), Direction.s);

        // south-east
        point = new Point3D(10, 10, 0);
        assertEquals(Direction.fromPoint(point), Direction.se);

        point = new Point3D(1, 1, 0);
        assertEquals(Direction.fromPoint(point), Direction.se);

        // south-west
        point = new Point3D(-10, 10, 0);
        assertEquals(Direction.fromPoint(point), Direction.sw);

        point = new Point3D(-1, 1, 0);
        assertEquals(Direction.fromPoint(point), Direction.sw);

        // east
        point = new Point3D(10, 0, 0);
        assertEquals(Direction.fromPoint(point), Direction.e);

        point = new Point3D(1, 0, 0);
        assertEquals(Direction.fromPoint(point), Direction.e);

        // west
        point = new Point3D(-10, 0, 0);
        assertEquals(Direction.fromPoint(point), Direction.w);

        point = new Point3D(-1, 0, 0);
        assertEquals(Direction.fromPoint(point), Direction.w);
    }
}

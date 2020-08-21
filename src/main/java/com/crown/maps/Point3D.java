package com.crown.maps;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents an integer-point in 3D space.
 */
public class Point3D implements Serializable {
    public int x;
    public int y;
    public int z;

    /**
     * Default constructor for a point.
     * default value for x, y, z is 0
     */
    public Point3D() {
        this(0, 0, 0);
    }

    /**
     * Constructor for a point.
     */
    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static final Point3D ZERO = new Point3D(0, 0, 0);

    /**
     * Copy constructor for a point.
     */
    public Point3D(Point3D point) {
        this(point.x, point.y, point.z);
    }

    /**
     * Returns copy of this point with changed X.
     */
    public Point3D withX(int x) {
        return new Point3D(x, y, z);
    }

    /**
     * Returns copy of this point with changed Y.
     */
    public Point3D withY(int y) {
        return new Point3D(x, y, z);
    }

    /**
     * Returns copy of this point with changed Z.
     */
    public Point3D withZ(int z) {
        return new Point3D(x, y, z);
    }

    /**
     * Get the distance between this point and another point.
     */
    public double getDistance(Point3D point) {
        return Math.sqrt(
            Math.pow(x - point.x, 2) +
                Math.pow(y - point.y, 2) +
                Math.pow(z - point.z, 2)
        );
    }

    /**
     * Returns new point that is the sum of this and
     * another points. The sum of two points is another point
     * whose x, y, and z values are the sum of
     * the two point x, y, and z values.
     */
    public Point3D plus(Point3D point) {
        return new Point3D(
            x + point.x,
            y + point.y,
            z + point.z
        );
    }

    /**
     * Returns new point representing difference of
     * this point and another point. The difference of two points is
     * another point whose x, y, and z values are
     * the difference of the two point x, y, and z values.
     */
    public Point3D minus(Point3D point) {
        return new Point3D(
            x - point.x,
            y - point.y,
            z - point.z
        );
    }

    /**
     * Returns new point with all coordinates increased by deltas.
     */
    public Point3D plus(int dx, int dy, int dz) {
        return new Point3D(
            x + dx,
            y + dy,
            z + dz
        );
    }

    /**
     * Returns new point with all coordinates decreased by deltas.
     */
    public Point3D minus(int dx, int dy, int dz) {
        return new Point3D(
            x - dx,
            y - dy,
            z - dz
        );
    }

    /**
     * Returns new point with all coordinates increased by value.
     */
    public Point3D plus(int value) {
        return new Point3D(
            x + value,
            y + value,
            z + value
        );
    }

    /**
     * Returns new point with all coordinates decreased by value.
     */
    public Point3D minus(int value) {
        return new Point3D(
            x - value,
            y - value,
            z - value
        );
    }

    /**
     * Returns new point representing a point with
     * the negative of this point's coordinates
     */
    public Point3D minus() {
        return new Point3D(-x, -y, -z);
    }

    /**
     * Returns new point from current with all coordinates > 0.
     */
    public Point3D abs() {
        return new Point3D(
            Math.abs(x),
            Math.abs(y),
            Math.abs(z)
        );
    }

    /**
     * Returns minimal point, composed from 2 provided points.
     * (Math.min for each coordinate).
     */
    public static Point3D min(Point3D a, Point3D b) {
        return new Point3D(
            Math.min(a.x, b.x),
            Math.min(a.y, b.y),
            Math.min(a.z, b.z)
        );
    }

    /**
     * Returns min from coordinates of this point.
     */
    public int minDirection() {
        return Math.min(Math.min(x, y), z);
    }

    /**
     * Returns maximal point, composed from 2 provided points.
     * (Math.max for each coordinate).
     */
    public static Point3D max(Point3D a, Point3D b) {
        return new Point3D(
            Math.max(a.x, b.x),
            Math.max(a.y, b.y),
            Math.max(a.z, b.z)
        );
    }

    /**
     * Returns max from coordinates of this point.
     */
    public int maxDirection() {
        return Math.max(Math.max(x, y), z);
    }

    /**
     * Checks if this point is inside of a parallelogram
     * defined by given a & b points.
     * In other words, checks if each coordinate of this point
     * is between the given points (e.g min <= x <= max, same for y and z).
     */
    public boolean isInsideOf(Point3D a, Point3D b) {
        return x >= a.x && x <= b.x
            && y >= a.y && y <= b.y
            && z >= a.z && z <= b.z;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Point3D point3D = (Point3D) o;

        if (x != point3D.x) {
            return false;
        }
        if (y != point3D.y) {
            return false;
        }
        return z == point3D.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}

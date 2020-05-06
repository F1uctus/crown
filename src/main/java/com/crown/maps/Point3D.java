package com.crown.maps;

/**
 * This class represents an integer-point in 3D space.
 */
public class Point3D {
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
     * Returns Point3D that is the sum of this and
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
     * Returns Point3D representing difference of
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
    public Point3D min(Point3D other) {
        return new Point3D(
            Math.min(x, other.x),
            Math.min(y, other.y),
            Math.min(z, other.z)
        );
    }

    /**
     * Returns maximal point, composed from 2 provided points.
     * (Math.max for each coordinate).
     */
    public Point3D max(Point3D other) {
        return new Point3D(
            Math.max(x, other.x),
            Math.max(y, other.y),
            Math.max(z, other.z)
        );
    }

    /**
     * Returns Point3D representing a point with
     * the negative of this point's coordinates
     */
    public Point3D minus() {
        return new Point3D(-x, -y, -z);
    }

    /**
     * Determines whether 'obj' is a Point3D
     * that is equal to this point.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Point3D other = (Point3D) obj;

        // comparing Integer object using equals()
        // boxing this.x, this.y, this.z as Integer
        return (((Integer) x).equals(other.x)
                && ((Integer) y).equals(other.y)
                && ((Integer) z).equals(other.z));
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}

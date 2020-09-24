package com.crown.maps;

import com.crown.common.NamedObject;
import com.crown.common.utils.Random;

import org.apache.commons.lang3.tuple.Pair;

import java.util.UUID;

/**
 * A regular object (thing, creature, etc.)
 * that is placed on referenced map.
 * Internally, 'heavy' references are replaced with their UUID's,
 * to maintain deep-copying performance.
 */
public abstract class MapObject extends NamedObject {
    private Map map;
    private final UUID mapIconId;

    private boolean isWalkable;
    private boolean isTransparent;

    protected Point3D[] lastPoints;
    protected Point3D[] points;

    /**
     * Creates new map object with size of 1
     * on the random map point, and places it on this map.
     */
    public MapObject(
        String name,
        Map map,
        MapIcon<?> mapIcon
    ) {
        this(
            name,
            map,
            mapIcon,
            Random.getPoint(map)
        );
    }

    /**
     * Creates new object with size of 1, and places it on the specified map.
     */
    public MapObject(
        String name,
        Map map,
        MapIcon<?> mapIcon,
        Point3D pt0
    ) {
        this(
            name,
            map,
            mapIcon,
            new Point3D[] { pt0 }
        );
    }

    /**
     * Creates new "large" object placed
     * on multiple points of map, and places it on this map.
     */
    public MapObject(
        String name,
        Map map,
        MapIcon<?> mapIcon,
        Point3D[] points
    ) {
        super(name);
        this.mapIconId = mapIcon.getId();
        this.points = lastPoints = points;
        setMap(map);
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        if (this.map != null) this.map.remove(this);
        if (map != null) map.add(this);
        this.map = map;
    }

    public abstract MapIcon<?> getMapIcon();

    public UUID getMapIconId() {
        return mapIconId;
    }

    /**
     * Returns all points on the map occupied by this object.
     */
    public Point3D[] getPoints() {
        return points;
    }

    /**
     * Returns 2D width of this object
     * (as it can occupy more than 1 point on the map).
     */
    public int getWidth() {
        Pair<Point3D, Point3D> bounds = getBounds();
        return bounds.getRight().x - bounds.getLeft().x + 1;
    }

    /**
     * Returns 2D height of this object
     * (as it can occupy more than 1 point on the map).
     */
    public int getHeight() {
        Pair<Point3D, Point3D> bounds = getBounds();
        return bounds.getRight().y - bounds.getLeft().y + 1;
    }

    /**
     * Returns pair of min & max points of this map object.
     */
    private Pair<Point3D, Point3D> getBounds() {
        Map map = getMap();
        Point3D minPt = new Point3D(map.xSize, map.ySize, map.zSize);
        Point3D maxPt = Point3D.ZERO;
        for (Point3D part : points) {
            minPt = Point3D.min(minPt, part);
            maxPt = Point3D.max(maxPt, part);
        }
        return Pair.of(minPt, maxPt);
    }

    /**
     * Returns the top-left point of this object on the map.
     */
    public Point3D getPt0() {
        return points[0];
    }

    /**
     * Returns the last top-left point of this object on the map.
     * (Point, from which object has moved the last time).
     */
    public Point3D getLastPt0() {
        return lastPoints[0];
    }

    /**
     * Moves object's view points to a new map location.
     * Unsafe, map bounds are not checked.
     */
    public void moveView(int deltaX, int deltaY, int deltaZ) {
        lastPoints = new Point3D[points.length];
        for (int i = 0; i < points.length; i++) {
            lastPoints[i] = new Point3D(points[i]);
            points[i].x += deltaX;
            points[i].y += deltaY;
            points[i].z += deltaZ;
        }
        getMap().move(this);
    }

    /**
     * Determines if this object can be passed-through (used by pathfinding algorithms).
     */
    public boolean isWalkable() {
        return isWalkable;
    }

    /**
     * Sets if this object can be passed-through (used by pathfinding algorithms).
     */
    public void setWalkable(boolean walkable) {
        isWalkable = walkable;
    }

    /**
     * Determines if this object can be viewed-through (used by vision algorithms).
     */
    public boolean isTransparent() {
        return isTransparent;
    }

    /**
     * Sets if this object can be viewed-through (used by vision algorithms).
     */
    public void setTransparent(boolean transparent) {
        isTransparent = transparent;
    }

    @Override
    public String toString() {
        return getName()
            + " [#" + getId()
            + " | " + getMapIcon()
            + " | " + (isWalkable ? "walkable" : "obstacle")
            + " | " + (isTransparent ? "transparent" : "opaque")
            + " | @ " + getPt0()
            + " map #" + getMap().getId()
            + "]";
    }
}

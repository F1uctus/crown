package com.crown.maps;

import com.crown.common.NamedObject;
import com.crown.common.utils.Random;
import org.apache.commons.lang3.SerializationUtils;
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
    private final MapWeight mapWeight;

    private boolean isWalkable;
    private boolean isTransparent;

    protected Point3D[] lastPoints;
    protected Point3D[] points;

    /**
     * Creates new map object with size of 1
     * on the random map point.
     */
    public MapObject(
        String name,
        Map map,
        MapIcon<?> mapIcon,
        MapWeight mapWeight
    ) {
        this(
            name,
            map,
            mapIcon,
            mapWeight,
            Random.getPoint(map)
        );
    }

    /**
     * Creates new object with size of 1.
     */
    public MapObject(
        String name,
        Map map,
        MapIcon<?> mapIcon,
        MapWeight mapWeight,
        Point3D pt0
    ) {
        this(
            name,
            map,
            mapIcon,
            mapWeight,
            new Point3D[] { pt0 }
        );
    }

    /**
     * Creates new "large" object placed
     * on multiple points of map.
     */
    public MapObject(
        String name,
        Map map,
        MapIcon<?> mapIcon,
        MapWeight mapWeight,
        Point3D[] points
    ) {
        super(name);
        this.mapIconId = mapIcon.getId();
        this.mapWeight = mapWeight;
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

    public MapWeight getMapWeight() {
        return mapWeight;
    }

    /**
     * Returns all points on the map occupied by this object.
     */
    public Point3D[] getPoints() {
        return points;
    }

    public int getWidth() {
        var bounds = getBounds();
        return bounds.getRight().x - bounds.getLeft().x + 1;
    }

    public int getHeight() {
        var bounds = getBounds();
        return bounds.getRight().y - bounds.getLeft().y + 1;
    }

    /**
     * Returns pair of min & max points of this map object.
     */
    private Pair<Point3D, Point3D> getBounds() {
        var map = getMap();
        var minPt = new Point3D(map.xSize, map.ySize, map.zSize);
        var maxPt = Point3D.ZERO;
        for (var part : points) {
            minPt = Point3D.min(minPt, part);
            maxPt = Point3D.max(maxPt, part);
        }
        return Pair.of(minPt, maxPt);
    }

    public Point3D getPt0() {
        return points[0];
    }

    public Point3D getLastPt0() {
        return lastPoints[0];
    }

    /**
     * Moves object's view points to a new map location.
     * Unsafe, map bounds are not checked.
     */
    public void moveView(int deltaX, int deltaY, int deltaZ) {
        lastPoints = SerializationUtils.clone(points);
        for (Point3D part : points) {
            part.x += deltaX;
            part.y += deltaY;
            part.z += deltaZ;
        }
        getMap().move(this);
    }

    @Override
    public String toString() {
        return getName()
            + " [#" + getId()
            + " | " + getMapIcon()
            + " | w=" + getMapWeight()
            + " | @ " + getPt0()
            + " map #" + getMap().getId()
            + "]";
    }
}

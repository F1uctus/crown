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

    protected Point3D[] lastParticles;
    protected Point3D[] particles;

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
        Point3D[] particles
    ) {
        super(name);
        this.mapIconId = mapIcon.getId();
        this.mapWeight = mapWeight;
        this.particles = lastParticles = particles;
        setMap(map);
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map value) {
        if (value == null) {
            map.remove(this);
        } else {
            value.add(this);
        }
        map = value;
    }

    public abstract MapIcon<?> getMapIcon();

    public UUID getMapIconId() {
        return mapIconId;
    }

    public MapWeight getMapWeight() {
        return mapWeight;
    }

    public Point3D[] getParticles() {
        return particles;
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
        var maxPt = new Point3D();
        for (var part : particles) {
            minPt = minPt.min(part);
            maxPt = maxPt.max(part);
        }
        return Pair.of(minPt, maxPt);
    }

    public Point3D getPt0() {
        return particles[0];
    }

    public Point3D getLastPt0() {
        return lastParticles[0];
    }

    /**
     * Moves object's view points to a new map location.
     * Unsafe, map bounds are not checked.
     */
    public void moveView(int deltaX, int deltaY, int deltaZ) {
        if (deltaX > 0) {
            getMapIcon().direction = Direction.east;
        } else if (deltaX < 0) {
            getMapIcon().direction = Direction.west;
        }
        lastParticles = SerializationUtils.clone(particles);
        for (Point3D part : particles) {
            part.x += deltaX;
            part.y += deltaY;
            part.z += deltaZ;
        }
        getMap().move(this);
    }

    @Override
    public String toString() {
        // noinspection HardCodedStringLiteral
        return getName()
            + " [#" + getId()
            + " | " + getMapIcon()
            + " | w=" + getMapWeight()
            + " | @ " + getPt0()
            + " map #" + getMap().getId()
            + "]";
    }
}

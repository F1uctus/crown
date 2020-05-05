package com.crown.maps;

import com.crown.common.NamedObject;
import com.crown.common.utils.Random;

public abstract class MapObject extends NamedObject {
    protected Map map;

    private final IMapIcon<?> mapIcon;
    private final MapWeight mapWeight;

    protected Point3D pt;
    protected Point3D lastPt;

    /**
     * Creates new map object on the random map point.
     */
    public MapObject(
        String name,
        Map map,
        IMapIcon<?> mapIcon,
        MapWeight mapWeight
    ) {
        this(name, map, mapIcon, mapWeight, Random.getPoint(map));
    }

    public MapObject(
        String name,
        Map map,
        IMapIcon<?> mapIcon,
        MapWeight mapWeight,
        Point3D pt
    ) {
        super(name);
        this.map = map;
        this.mapIcon = mapIcon;
        this.mapWeight = mapWeight;
        this.pt = lastPt = pt;
    }

    public Map getMap() {
        return map;
    }

    public IMapIcon<?> getMapIcon() {
        return this.mapIcon;
    }

    public MapWeight getMapWeight() {
        return mapWeight;
    }

    public Point3D getPt() {
        return pt;
    }

    public Point3D getLastPt() {
        return lastPt;
    }

    @Override
    public String toString() {
        // noinspection HardCodedStringLiteral
        return getName()
               + " [#" + getId()
               + " | " + getMapIcon()
               + " | w=" + getMapWeight()
               + " | @ " + getPt()
               + " map #" + getMap().getId()
               + "]";
    }
}

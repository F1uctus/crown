package com.crown.maps;

import com.crown.common.NamedObject;
import com.crown.common.utils.MathAux;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Map extends NamedObject implements IMap, Serializable {
    public final int xSize;
    public final int ySize;
    public final int zSize;

    protected final MapCell[][][] containers;

    public Map(
        String name,
        int xSize,
        int ySize,
        int zSize
    ) {
        super(name);
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;

        containers = new MapCell[zSize][ySize][xSize];
        for (int z = 0; z < zSize; z++) {
            for (int y = 0; y < ySize; y++) {
                for (int x = 0; x < xSize; x++) {
                    containers[z][y][x] = new MapCell();
                }
            }
        }
    }

    public abstract MapIcon<?> getEmptyIcon();

    /**
     * Returns all objects of given type inside the area with given
     * center point and radius.
     */
    public <T extends MapObject> ArrayList<T> getAll(
        Class<T> ofType,
        Point3D centerPoint,
        int inRadius
    ) {
        ArrayList<T> targets = new ArrayList<T>();
        MapObject[][] rangeObjsMatrix = getRaw2DArea(centerPoint, inRadius);
        for (MapObject[] objRow : rangeObjsMatrix) {
            for (MapObject obj : objRow) {
                if (ofType.isInstance(obj)) {
                    // noinspection unchecked
                    targets.add((T) obj);
                }
            }
        }
        return targets;
    }

    /**
     * Returns 3D area for map region with given radius.
     * Objects with Z-coordinate <= point.z are returned.
     */
    public @Nullable MapObject[][][] getRaw3DArea(Point3D centerPoint, int radius) {
        final int diameter = radius * 2 + 1;
        int height = MathAux.clamp(centerPoint.z + 1, 1, zSize);
        MapObject[][][] area = new MapObject[height][diameter][diameter];
        for (int z = 0; z < height; z++) {
            int areaY = 0;
            for (int y = centerPoint.y - radius; y <= centerPoint.y + radius; y++) {
                int areaX = 0;
                for (int x = centerPoint.x - radius; x <= centerPoint.x + radius; x++) {
                    if (inBounds(x, y)) {
                        area[z][areaY][areaX] = get(x, y, z);
                    }
                    areaX++;
                }
                areaY++;
            }
        }
        return area;
    }

    /**
     * Returns a 3D z-column of this map at specified point.
     * Objects with Z-coordinate <= point.z are returned.
     */
    public @Nullable MapObject[] getColumn(Point3D point) {
        int height = MathAux.clamp(point.z + 1, 1, zSize);
        MapObject[] column = new MapObject[height];
        for (int z = 0; z < height; z++) {
            if (inBounds(point.x, point.y)) {
                column[z] = get(point.x, point.y, z);
            }
        }
        return column;
    }

    /**
     * Returns 3D area for map region with given radius.
     * Icons with Z-coordinate <= point.z are returned.
     */
    public MapIcon<?>[][][] get3DArea(Point3D centerPoint, int radius) {
        MapObject[][][] area = getRaw3DArea(centerPoint, radius);
        MapIcon<?>[][][] icons = new MapIcon<?>[area.length][area[0].length][area[0][0].length];
        for (int z = 0; z < area.length; z++) {
            for (int y = 0; y < area[0].length; y++) {
                for (int x = 0; x < area[0][0].length; x++) {
                    if (area[z][y][x] == null) {
                        icons[z][y][x] = getEmptyIcon();
                    } else {
                        icons[z][y][x] = area[z][y][x].getMapIcon();
                    }
                }
            }
        }
        return icons;
    }

    /**
     * Returns 2D area for map region with given radius.
     * Z coordinate is used to specify "view point height".
     * e. g. if you pass z = 3 for each map point you will get
     * object with the highest z position if it is <= 3.
     */
    public @Nullable MapObject[][] getRaw2DArea(Point3D centerPoint, int radius) {
        final int diameter = radius * 2 + 1;
        MapObject[][] area = new MapObject[diameter][diameter];

        int height = MathAux.clamp(centerPoint.z + 1, 1, zSize);
        for (int z = 0; z < height; z++) {
            int areaY = 0;
            for (int y = centerPoint.y - radius; y <= centerPoint.y + radius; y++) {
                int areaX = 0;
                for (int x = centerPoint.x - radius; x <= centerPoint.x + radius; x++) {
                    if (inBounds(x, y)) {
                        MapObject obj = get(x, y, z);
                        if (obj != null) {
                            area[areaY][areaX] = obj;
                        }
                    }
                    areaX++;
                }
                areaY++;
            }
        }
        return area;
    }

    /**
     * Returns an object that is topmost in selected map column.
     * Z coordinate is used to specify "view point height".
     * e. g. if you pass z = 3 for each map point you will get
     * object with the highest z position if it is <= 3.
     */
    public @Nullable MapObject getTopmost(Point3D point) {
        MapObject result = null;
        int height = MathAux.clamp(point.z + 1, 1, zSize);
        for (int z = 0; z < height; z++) {
            if (inBounds(point.x, point.y)) {
                MapObject obj = get(point.x, point.y, z);
                if (obj != null) {
                    result = obj;
                }
            }
        }
        return result;
    }

    /**
     * Returns icons of 2D area for map region with given radius.
     * Z coordinate is used to specify "view point height".
     * e. g. if you pass z = 3 for each map point you will get
     * icon of object with the highest z position if it is <= 3.
     */
    public MapIcon<?>[][] get2DArea(Point3D centerPoint, int radius) {
        MapObject[][] area = getRaw2DArea(centerPoint, radius);
        MapIcon<?>[][] icons = new MapIcon<?>[area.length][area.length];
        for (int y = 0; y < area.length; y++) {
            for (int x = 0; x < area.length; x++) {
                if (area[y][x] == null) {
                    icons[y][x] = getEmptyIcon();
                } else {
                    icons[y][x] = area[y][x].getMapIcon();
                }
            }
        }
        return icons;
    }

    public void add(@NotNull MapObject mapObj) {
        for (Point3D pt : mapObj.points) {
            getRaw(pt).objects.push(mapObj);
        }
    }

    public void remove(@NotNull MapObject mapObj) {
        for (Point3D pt : mapObj.points) {
            getRaw(pt).objects.remove(mapObj);
        }
    }

    /**
     * Removes specified object from it's last point,
     * then adds it on the current point.
     */
    public void move(@NotNull MapObject mapObj) {
        if (inBounds(mapObj.getPt0())) {
            if (inBounds(mapObj.getLastPt0())) {
                for (Point3D pt : mapObj.lastPoints) {
                    getRaw(pt).objects.remove(mapObj);
                }
            }
            add(mapObj);
        }
    }


    /**
     * Checks if specified point is inside this map's bounds.
     */
    public boolean inBounds(@NotNull Point3D pt) {
        return inBounds(pt.x, pt.y, pt.z);
    }

    /**
     * Checks if specified point is inside this map's bounds.
     */
    public boolean inBounds(int x, int y) {
        return x >= 0 && x < xSize
            && y >= 0 && y < ySize;
    }

    /**
     * Checks if specified point is inside this map's bounds.
     */
    public boolean inBounds(int x, int y, int z) {
        return inBounds(x, y)
            && z >= 0 && z < zSize;
    }

    @Nullable
    public MapObject get(@NotNull Point3D pt) {
        return get(pt.x, pt.y, pt.z);
    }

    @Nullable
    public MapObject get(int x, int y, int z) {
        if (!inBounds(x, y, z)) {
            return null;
        }
        MapCell cont = getRaw(x, y, z);
        if (cont.objects.empty()) {
            return null;
        }
        return cont.objects.peek();
    }

    protected MapCell getRaw(int x, int y, int z) {
        return getRaw(new Point3D(x, y, z));
    }

    protected MapCell getRaw(@NotNull Point3D pt) {
        return containers[pt.z][pt.y][pt.x];
    }

    /**
     * Removes all MapObject-s from the map.
     */
    protected void clear() {
        for (int z = 0; z < zSize; z++) {
            for (int y = 0; y < ySize; y++) {
                for (int x = 0; x < xSize; x++) {
                    getRaw(x, y, z).objects.clear();
                }
            }
        }
    }

    /**
     * Used for player movement checks.
     */
    public boolean isWalkable(@NotNull Point3D point) {
        MapObject obj = get(point);
        return obj == null || obj.isWalkable();
    }

    /**
     * Used for player vision logic.
     */
    public boolean isTransparent(@NotNull Point3D point) {
        MapObject obj = get(point);
        return obj == null || obj.isTransparent();
    }

    /**
     * Used for player vision logic.
     */
    public void visit(Point3D point) {
        // TODO: implement 'visit'
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int z = 0; z < zSize; z++) {
            sb.append("Layer ").append(z + 1).append(":\n");
            for (int y = 0; y < ySize; y++) {
                for (int x = 0; x < xSize; x++) {
                    MapCell c = containers[z][y][x];
                    if (c.objects.empty()) {
                        sb.append("     ");
                        continue;
                    }
                    sb.append(
                        c.objects.peek().getKeyName(),
                        0,
                        Math.min(c.objects.peek().getKeyName().length(), 4)
                    ).append(" ");
                }
                sb.append('\n');
            }
        }
        return sb.toString();
    }
}

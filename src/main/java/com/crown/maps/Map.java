package com.crown.maps;

import com.crown.common.NamedObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlforj.IBoard;

import java.util.stream.IntStream;

public abstract class Map extends NamedObject implements IBoard {
    public final int xSize;
    public final int ySize;
    public final int zSize;
    public final MapIcon<?> emptyIcon;

    protected final MapCell[][][] containers;

    public Map(
        String name,
        int xSize,
        int ySize,
        int zSize,
        MapIcon<?> emptyIcon
    ) {
        super(name);
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
        this.emptyIcon = emptyIcon;

        containers = new MapCell[zSize][ySize][xSize];
        for (int z = 0; z < zSize; z++) {
            for (int y = 0; y < ySize; y++) {
                for (int x = 0; x < xSize; x++) {
                    containers[z][y][x] = new MapCell();
                }
            }
        }
    }

    /**
     * Returns 3D area for map region with given radius.
     * Objects with Z-coordinate <= to point.z are returned.
     */
    public @Nullable MapObject[][][] getRaw3DArea(Point3D pt, int radius) {
        final int diameter = radius * 2 + 1;
        int ptZ = pt.z + 1;
        int height = ptZ > 0 && ptZ < zSize ? ptZ : zSize;

        MapObject[][][] area = new MapObject[height][diameter][diameter];

        int areaZ = 0;
        for (int z = 0; z < height; z++) {
            int areaY = 0;
            for (int y = pt.y - radius; y <= pt.y + radius; y++) {
                int areaX = 0;
                for (int x = pt.x - radius; x <= pt.x + radius; x++) {
                    if (contains(x, y)) {
                        area[areaZ][areaY][areaX] = get(x, y, z);
                    }
                    areaX++;
                }
                areaY++;
            }
            areaZ++;
        }
        return area;
    }

    /**
     * Returns 3D area for map region with given radius.
     * Icons with Z-coordinate <= to point.z are returned.
     */
    public MapIcon<?>[][][] get3DArea(Point3D pt, int radius) {
        var area = getRaw3DArea(pt, radius);
        var icons = new MapIcon<?>[area.length][area[0].length][area[0][0].length];
        for (int z = 0; z < area.length; z++) {
            for (int y = 0; y < area[0].length; y++) {
                for (int x = 0; x < area[0][0].length; x++) {
                    if (area[z][y][x] == null) {
                        icons[z][y][x] = emptyIcon;
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
    public @Nullable MapObject[][] getRaw2DArea(Point3D pt, int radius) {
        final int diameter = radius * 2 + 1;
        MapObject[][] area = new MapObject[diameter][diameter];

        int ptZ = pt.z + 1;
        int height = ptZ > 0 && ptZ < zSize ? ptZ : zSize;

        for (int z = 0; z < height; z++) {
            int areaY = 0;
            for (int y = pt.y - radius; y <= pt.y + radius; y++) {
                int areaX = 0;
                for (int x = pt.x - radius; x <= pt.x + radius; x++) {
                    if (contains(x, y)) {
                        area[areaY][areaX] = get(x, y, z);
                    }
                    areaX++;
                }
                areaY++;
            }
        }
        return area;
    }

    /**
     * Returns icons of 2D area for map region with given radius.
     * Z coordinate is used to specify "view point height".
     * e. g. if you pass z = 3 for each map point you will get
     * icon of object with the highest z position if it is <= 3.
     */
    public MapIcon<?>[][] get2DArea(Point3D pt, int radius) {
        var area = getRaw2DArea(pt, radius);
        var icons = new MapIcon<?>[area.length][area.length];
        for (int y = 0; y < area.length; y++) {
            for (int x = 0; x < area.length; x++) {
                if (area[y][x] == null) {
                    icons[y][x] = emptyIcon;
                } else {
                    icons[y][x] = area[y][x].getMapIcon();
                }
            }
        }
        return icons;
    }

    public void add(@NotNull MapObject mapObj) {
        for (var pt : mapObj.particles) {
            getRaw(pt).objects.push(mapObj);
        }
    }

    public void remove(@NotNull MapObject mapObj) {
        for (var pt : mapObj.particles) {
            getRaw(pt).objects.remove(mapObj);
        }
    }

    /**
     * Removes specified object from it's last point,
     * then adds it on the current point.
     */
    public void move(@NotNull MapObject mapObj) {
        if (contains(mapObj.getPt0())) {
            if (contains(mapObj.getLastPt0())) {
                for (var pt : mapObj.lastParticles) {
                    getRaw(pt).objects.remove(mapObj);
                }
            }
            add(mapObj);
        }
    }

    /**
     * Checks if specified point is inside this map's bounds.
     */
    public boolean contains(@NotNull Point3D pt) {
        return pt.x >= 0
               && pt.x < xSize
               && pt.y >= 0
               && pt.y < ySize
               && pt.z >= 0
               && pt.z < zSize;
    }

    @Nullable
    public MapObject get(@NotNull Point3D pt) {
        return get(pt.x, pt.y, pt.z);
    }

    @Nullable
    public MapObject get(int x, int y, int z) {
        var cont = getRaw(x, y, z);
        if (cont.objects.empty()) {
            return null;
        }
        return cont.objects.peek();
    }

    protected MapCell getRaw(@NotNull Point3D pt) {
        return getRaw(pt.x, pt.y, pt.z);
    }

    protected MapCell getRaw(int x, int y, int z) {
        return containers[z][y][x];
    }

    /**
     * Removes all MapObject-s from map.
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
     * Used for player vision logic. Do not use it manually!
     * Use {@code contains(pt)} instead.
     */
    @Override
    @Deprecated
    public boolean contains(int x, int y) {
        return x >= 0
               && x < xSize
               && y >= 0
               && y < ySize;
    }

    /**
     * Used for player movement checks.
     */
    @Override
    public boolean isObstacle(int x, int y) {
        return IntStream
            .range(0, zSize)
            .mapToObj(z -> get(x, y, z))
            .anyMatch(
                mapObj -> mapObj != null
                          && mapObj.getMapWeight() == MapWeight.OBSTACLE
            );
    }

    /**
     * Used for player vision logic. Do not use it manually!
     */
    @Override
    @Deprecated
    public boolean blocksLight(int x, int y) {
        return IntStream
            .range(0, zSize)
            .mapToObj(z -> get(x, y, z))
            .anyMatch(
                mapObj -> mapObj != null
                          && mapObj.getMapWeight() == MapWeight.BLOCKS_LIGHT
            );
    }

    /**
     * Used for player vision logic. Do not use it manually!
     */
    @Override
    @Deprecated
    public boolean blocksStep(int x, int y) {
        return IntStream
            .range(0, zSize)
            .mapToObj(z -> get(x, y, z))
            .anyMatch(
                mapObj -> mapObj != null
                          && mapObj.getMapWeight() == MapWeight.BLOCKS_STEP
            );
    }

    /**
     * Used for player vision logic. Do not use it manually!
     */
    @Override
    @Deprecated
    public void visit(int x, int y) {
        // TODO: implement 'visit'
    }
}

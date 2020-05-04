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

    protected final MapObjectContainer[][][] container;

    private final IMapIcon<?> mapEndIcon;

    public Map(
        String name,
        int xSize,
        int ySize,
        int zSize,
        IMapIcon<?> mapEndIcon
    ) {
        super(name);
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
        this.container = new MapObjectContainer[zSize][ySize][xSize];
        for (int z = 0; z < zSize; z++) {
            for (int y = 0; y < ySize; y++) {
                for (int x = 0; x < xSize; x++) {
                    container[z][y][x] = new MapObjectContainer();
                }
            }
        }
        this.mapEndIcon = mapEndIcon;
    }

    /**
     * Returns 2D area for map region with given radius.
     * Z coordinate is used to specify "view point height".
     * e. g. if you pass z = 3 for each map point you will get
     * object with the highest z position if it is <= 3.
     */
    public IMapIcon<?>[][] get2DArea(Point3D pt, int radius) {
        final int diameter = radius * 2 + 1;
        IMapIcon<?>[][] area = new IMapIcon[diameter][diameter];

        int height = pt.z > 0 && pt.z < zSize ? pt.z : zSize;

        for (int z = 0; z <= height; z++) {
            int areaY = 0;
            int areaX = 0;
            for (int y = pt.y - radius; y < pt.y + radius; y++, areaY++) {
                for (int x = pt.x - radius; x < pt.x + radius; x++, areaX++) {
                    if (contains(x, y)) {
                        MapObject mapObj = get(x, y, z);
                        if (mapObj != null) {
                            area[areaX][areaY] = mapObj.getMapIcon();
                        }
                    }
                }
            }
        }
        for (int y = 0; y < diameter; y++) {
            for (int x = 0; x < diameter; x++) {
                if (area[y][x] == null) {
                    area[y][x] = mapEndIcon;
                }
            }
        }

        return area;
    }

    public void add(MapObject mapObj) {
        set(mapObj.getPt(), mapObj);
    }

    public void remove(@NotNull MapObject mapObj) {
        set(mapObj.getPt(), null);
    }

    public void move(@NotNull MapObject mapObj) {
        if (contains(mapObj.getPt())) {
            var l = mapObj.getLastPt();
            if (contains(l)) {
                set(l, container[l.z][l.y][l.x].previousObj);
            }
            add(mapObj);
        }
    }

    public boolean contains(@NotNull Point3D pt) {
        return pt.x >= 0
               && pt.x <= xSize
               && pt.y >= 0
               && pt.y <= ySize
               && pt.z >= 0
               && pt.z <= zSize;
    }

    @Nullable
    public MapObject get(@NotNull Point3D pt) {
        return get(pt.x, pt.y, pt.z);
    }

    @Nullable
    public MapObject get(int x, int y, int z) {
        return container[z][y][x].currentObj;
    }

    protected void set(@NotNull Point3D pt, MapObject value) {
        set(pt.x, pt.y, pt.z, value);
    }

    protected void set(int x, int y, int z, MapObject value) {
        container[z][y][x].previousObj = container[z][y][x].currentObj;
        container[z][y][x].currentObj = value;
    }

    protected void clear() {
        for (int z = 0; z < zSize; z++) {
            for (int y = 0; y < ySize; y++) {
                for (int x = 0; x < xSize; x++) {
                    set(x, y, z, null);
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
               && x <= xSize
               && y >= 0
               && y <= ySize;
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

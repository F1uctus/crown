package com.crown.maps.vision;

import com.crown.maps.IMap;
import com.crown.maps.Point3D;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface ILineOfSight {
    /**
     * Determines whether line of sight exists between point start and
     * end. Calculates the path of projection.
     *
     * @param b        The board to be visited.
     * @param start    Starting point.
     * @param end      Target point.
     * @return true if a line of sight could be established.
     */
    Pair<Boolean, Point3D[]> exists(
        final IMap b,
        Point3D start,
        Point3D end
    );
}

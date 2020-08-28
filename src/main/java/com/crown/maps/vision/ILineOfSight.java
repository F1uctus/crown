package com.crown.maps.vision;

import com.crown.maps.IMap;
import com.crown.maps.Point3D;

import java.util.List;

public interface ILineOfSight {
    /**
     * Determines whether line of sight exists between point start and
     * end. Optionally calculates the path of projection (retrievable via call to
     * {@link ILineOfSight#getPath}).
     *
     * @param b        The board to be visited.
     * @param start    Starting point.
     * @param end      Target point.
     * @param savePath Whether to also calculate and store the path from the source to the target.
     * @return true if a line of sight could be established.
     */
    boolean exists(
        final IMap b,
        Point3D start,
        Point3D end,
        final boolean savePath
    );

    /**
     * Obtain the path of the projection calculated during the last call
     * to {@link ILineOfSight#exists}.
     *
     * @return null if no los was established so far, or a list of points if a los found.
     */
    List<Point3D> getPath();
}

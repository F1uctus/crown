package com.crown.maps;

import com.crown.common.NamedObject;
import org.jetbrains.annotations.NotNull;

/**
 * Map object's icon. Left-sided by default.
 */
public abstract class MapIcon<T> extends NamedObject {
    protected Direction direction = Direction.west;

    /**
     * Creates new map icon.
     *
     * @param keyName Can be a path to image, or symbol used as icon, etc.
     */
    public MapIcon(@NotNull String keyName) {
        super(keyName);
    }

    public abstract T get();
}

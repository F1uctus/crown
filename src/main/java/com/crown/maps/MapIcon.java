package com.crown.maps;

import com.crown.common.NamedObject;
import org.jetbrains.annotations.NotNull;

/**
 * Map object's icon. Left-sided by default.
 */
public abstract class MapIcon<T> extends NamedObject {
    private Direction direction = Direction.w;

    /**
     * Creates new map icon.
     *
     * @param keyName Can be a path to image, or symbol used as icon, etc.
     */
    public MapIcon(@NotNull String keyName) {
        super(keyName);
    }

    public abstract T get();

    public abstract void stepAnimation();

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}

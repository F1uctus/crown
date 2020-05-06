package com.crown.maps;

/**
 * Map object's icon. Left-sided by default.
 */
public abstract class MapIcon<T> {
    protected boolean flipped = false;

    public abstract T get();
}

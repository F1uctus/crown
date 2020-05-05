package com.crown.maps;

/**
 * Map object's icon. Left-sided by default.
 */
public abstract class MapIcon<T> {
    boolean flipped = false;
    abstract T get();
}

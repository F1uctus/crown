package com.crown.maps;

import java.io.Serializable;

/**
 * Map object's icon. Left-sided by default.
 */
public abstract class MapIcon<T> implements Serializable {
    protected boolean flipped = false;

    public abstract T get();
}

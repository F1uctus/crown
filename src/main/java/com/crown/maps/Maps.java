package com.crown.maps;

import com.crown.common.ObjectCollection;

/**
 * Global collection of in-game maps.
 * Should be initialized using {@link Maps#withPredefinedItems()}.
 */
public class Maps extends ObjectCollection<Map> {
    private static Maps instance;

    public static Maps getMaps() {
        if (instance == null) {
            synchronized (Maps.class) {
                if (instance == null) {
                    instance = new Maps();
                }
            }
        }
        return instance;
    }
}

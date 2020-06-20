package com.crown.maps;

import com.crown.common.ObjectCollection;

public class MapIcons extends ObjectCollection<MapIcon<?>> {
    private static MapIcons instance;

    public static MapIcons getMapIcons() {
        if (instance == null) {
            synchronized (MapIcons.class) {
                if (instance == null) {
                    instance = new MapIcons();
                }
            }
        }
        return instance;
    }
}

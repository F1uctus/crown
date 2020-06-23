package com.crown.maps;

import com.crown.common.ObjectByIdMap;

public class MapIcons extends ObjectByIdMap<MapIcon<?>> {
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
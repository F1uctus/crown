package com.crown.maps;

import com.crown.common.ObjectsMap;

public class BaseMapIcons extends ObjectsMap<MapIcon<?>> {
    private static BaseMapIcons instance;

    public static BaseMapIcons getIcons() {
        if (instance == null) {
            synchronized (BaseMapIcons.class) {
                if (instance == null) {
                    instance = (BaseMapIcons) new BaseMapIcons().withPredefinedItems();
                }
            }
        }
        return instance;
    }
}

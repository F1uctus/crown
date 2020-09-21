package com.crown.maps;

import java.util.Stack;

class MapCell {
    public final Stack<MapObject> objects = new Stack<>();

    @Override
    public String toString() {
        return objects.stream().map(MapObject::toString).reduce((a, b) -> a + ";\n" + b).orElse("");
    }
}

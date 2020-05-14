package com.crown.maps;

import java.io.Serializable;
import java.util.Stack;

class MapCell implements Serializable {
    public final Stack<MapObject> objects = new Stack<>();
}

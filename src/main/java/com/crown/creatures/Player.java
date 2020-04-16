package com.crown.creatures;

import com.crown.common.utils.Random;
import com.crown.maps.IMapIcon;
import com.crown.maps.Map;
import com.crown.maps.MapWeight;

public abstract class Player extends Creature {
    public Player(
        String name,
        Map map,
        IMapIcon<?> mapIcon
    ) {
        super(name, map, mapIcon, MapWeight.OBSTACLE, Random.getPoint(map));
    }
}

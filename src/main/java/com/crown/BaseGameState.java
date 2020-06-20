package com.crown;

import com.crown.common.ObjectCollection;
import com.crown.creatures.Creature;
import com.crown.maps.Map;
import com.crown.time.Timeline;

import java.io.Serializable;

public class BaseGameState implements Serializable {
    public final ObjectCollection<Creature> players = new ObjectCollection<>();
    public final Map map;

    public BaseGameState(Map map) {
        this.map = map;
    }

    protected void addNewPlayer(Creature p) {
        p.timeline = Timeline.main;
        players.add(p);
    }

    public void removePlayer(Creature p) {
        players.remove(p.getKeyName());
    }
}
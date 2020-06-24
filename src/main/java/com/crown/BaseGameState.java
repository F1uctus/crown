package com.crown;

import com.crown.common.ObjectsMap;
import com.crown.creatures.Creature;
import com.crown.time.Timeline;

public class BaseGameState {
    public final ObjectsMap<Creature> players = new ObjectsMap<>();

    protected void addPlayer(Creature p) {
        p.timeline = Timeline.main;
        players.add(p);
    }

    public void removePlayer(Creature p) {
        players.remove(p);
    }
}

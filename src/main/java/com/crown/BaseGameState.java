package com.crown;

import com.crown.common.ObjectByKeyNameMap;
import com.crown.creatures.Creature;
import com.crown.time.Timeline;

public class BaseGameState {
    public final ObjectByKeyNameMap<Creature> players = new ObjectByKeyNameMap<>();

    public BaseGameState() {
    }

    protected void addPlayer(Creature p) {
        p.timeline = Timeline.main;
        players.add(p);
    }

    public void removePlayer(Creature p) {
        players.remove(p);
    }
}

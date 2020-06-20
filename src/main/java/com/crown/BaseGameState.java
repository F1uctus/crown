package com.crown;

import com.crown.common.ObjectCollection;
import com.crown.creatures.Creature;
import com.crown.time.Timeline;

public class BaseGameState {
    public final ObjectCollection<Creature> players = new ObjectCollection<>();

    public BaseGameState() {
    }

    protected void addNewPlayer(Creature p) {
        p.timeline = Timeline.main;
        players.add(p);
    }

    public void removePlayer(Creature p) {
        players.remove(p);
    }
}

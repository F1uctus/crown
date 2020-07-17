package com.crown;

import com.crown.common.ObjectsMap;
import com.crown.creatures.Creature;

public class BaseGameState {
    public final ObjectsMap<Creature> players = new ObjectsMap<>();

    public void rename(Creature c, String newName) {
        players.remove(c);
        c.setKeyName(newName);
        players.add(c);
    }
}

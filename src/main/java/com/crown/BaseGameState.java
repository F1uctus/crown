package com.crown;

import com.crown.common.ObjectsMap;
import com.crown.creatures.Creature;
import com.crown.maps.Map;

public class BaseGameState {
    public final ObjectsMap<Creature> players = new ObjectsMap<>();
    private final Map globalMap;

    public BaseGameState(Map globalMap) {
        this.globalMap = globalMap;
    }

    public Map getGlobalMap() {
        return globalMap;
    }

    public void rename(Creature c, String newName) {
        players.remove(c);
        c.setKeyName(newName);
        players.add(c);
    }
}

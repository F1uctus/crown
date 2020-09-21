package com.crown;

import com.crown.common.ObjectsMap;
import com.crown.creatures.Organism;
import com.crown.maps.Map;

public class BaseGameState {
    public final ObjectsMap<Organism> players = new ObjectsMap<>();
    private final Map globalMap;

    public BaseGameState(Map globalMap) {
        this.globalMap = globalMap;
    }

    public Map getGlobalMap() {
        return globalMap;
    }

    public void rename(Organism c, String newName) {
        players.remove(c);
        c.setKeyName(newName);
        players.add(c);
    }
}

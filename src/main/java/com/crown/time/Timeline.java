package com.crown.time;

import com.crown.BaseGameState;
import com.crown.creatures.Creature;
import com.crown.i18n.ITemplate;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A timeline for game events.
 * The "heart" of time travelling implementation.
 */
public class Timeline implements Serializable {
    public static Timeline main;
    public static ArrayList<Timeline> alternativeLines = new ArrayList<>();

    private final VirtualClock clock;
    private final BaseGameState gameState;
    private final ArrayList<Action<?>> actions = new ArrayList<>();

    public Timeline(VirtualClock clock, BaseGameState gameState) {
        this.clock = clock;
        this.gameState = gameState;
    }

    public static void setMain(Timeline value) {
        main = value;
    }

    public ITemplate perform(Action<?> a) {
        actions.add(a);
        return a.perform();
    }

    public void rollbackTo(TimePoint point, Creature savedCreature) {
        for (int i = 0; i < actions.size(); i++) {
            Action<?> a = actions.get(i);
            if (a.point.gt(point)) {
                if (a.performer != savedCreature) {
                    a.rollback();
                }
                actions.remove(a);
            }
        }
        clock.startAt(actions.get(actions.size() - 1).point);
    }

    /**
     * Moves specified creature back in time to specified point in the new timeline.
     */
    public void beginChanges(Creature c, TimePoint point) {
        var newTimeline = SerializationUtils.clone(main);
        newTimeline.rollbackTo(point, newTimeline.gameState.players.get(c.getKeyName()));
        alternativeLines.add(newTimeline);
        main.gameState.removePlayer(c);
    }

    public void commitChanges(Creature c) {
        if (c.timeline == null || c.timeline == main) {
            return;
        }
        setMain(c.timeline);
    }

    public VirtualClock getClock() {
        return clock;
    }

    public BaseGameState getGameState() {
        return gameState;
    }
}

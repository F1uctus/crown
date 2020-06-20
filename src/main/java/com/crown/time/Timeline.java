package com.crown.time;

import com.crown.BaseGameState;
import com.crown.creatures.Creature;
import com.crown.i18n.ITemplate;
import com.rits.cloning.Cloner;
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

    private TimePoint offsetToMain;
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
        for (Action<?> a : actions) {
            if (a.point.gt(point)) {
                if (a.performer != savedCreature) {
                    a.rollback();
                }
            }
        }
    }

    /**
     * Moves specified creature back in time to specified point in the new timeline.
     */
    public void beginChanges(Creature c, TimePoint point) {
        var cloner = new Cloner();
        var newTimeline = cloner.deepClone(main);
        newTimeline.offsetToMain = VirtualClock.now().plus(point.minus());
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

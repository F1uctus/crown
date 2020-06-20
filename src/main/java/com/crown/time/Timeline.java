package com.crown.time;

import com.crown.BaseGameState;
import com.crown.creatures.Creature;
import com.crown.i18n.ITemplate;
import com.rits.cloning.Cloner;

import java.util.ArrayList;

/**
 * A timeline for game events.
 * The "heart" of time travelling implementation.
 */
public class Timeline {
    /**
     * Main real-time timeline, where
     * all players are placed by default.
     */
    public static Timeline main;

    /**
     * Alternative timelines
     * (are created when some player travels back in time).
     */
    public static final ArrayList<Timeline> alternativeLines = new ArrayList<>();

    /**
     * Main game clock, common across all timelines.
     * Time difference is specified by {@link #offsetToMain}.
     */
    private static VirtualClock clock;

    /**
     * Time difference of [main timeline] - [this timeline].
     * Equal to {@link TimePoint#zero} for main timeline, obviously.
     */
    private TimePoint offsetToMain;

    private final BaseGameState gameState;
    private final ArrayList<Action<?>> actions = new ArrayList<>();

    public Timeline(BaseGameState gameState) {
        this.gameState = gameState;
    }

    public static void setMain(Timeline value) {
        main = value;
    }

    public static void setGameClock(VirtualClock clock) {
        Timeline.clock = clock;
    }

    public static VirtualClock getGameClock() {
        return clock;
    }

    public TimePoint getOffsetToMain() {
        return offsetToMain;
    }

    public BaseGameState getGameState() {
        return gameState;
    }

    public ITemplate perform(Action<?> a) {
        actions.add(a);
        return a.perform();
    }

    /**
     * Main logic of backward time-travelling.
     * Discards all actions in this timeline made up to {@code point},
     * IF they weren't made by {@code preservedCreature}.
     */
    void rollbackTo(TimePoint point, Creature preservedCreature) {
        // Ensure we're travelling into past
        assert point.lt(clock.now());
        // Throws ConcurrentModificationException
        // noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < actions.size(); i++) {
            Action<?> a = actions.get(i);
            if (a.point.gt(point)) {
                if (a.performer != preservedCreature) {
                    a.rollback();
                }
            }
        }
    }

    /**
     * Moves specified creature {@code c} back in time
     * to specified {@code point} in the new timeline.
     */
    public void beginChanges(Creature c, TimePoint point) {
        var cloner = new Cloner();
        cloner.setDumpClonedClasses(true);
        var newTimeline = cloner.deepClone(main);
        newTimeline.offsetToMain = clock.now().plus(point.minus());
        newTimeline.rollbackTo(point, newTimeline.gameState.players.get(c.getId()));
        alternativeLines.add(newTimeline);
        main.gameState.removePlayer(c);
    }

    /**
     * Commits all changes made by {@code c}.
     * (Makes creature's timeline the main one).
     */
    public void commitChanges(Creature c) {
        if (c.timeline == null || c.timeline == main) {
            return;
        }
        setMain(c.timeline);
    }
}

package com.crown.time;

import com.crown.BaseGameState;
import com.crown.creatures.Organism;
import com.crown.i18n.ITemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentSkipListMap;

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
     * Main game clock, common across all timelines.
     */
    static VirtualClock clock;

    /**
     * Time difference of [main timeline] - [this timeline].
     * Equal to 0 for main timeline, obviously.
     */
    Duration offsetToMain;

    /**
     * A game state bound to this timeline.
     */
    final BaseGameState gameState;

    /**
     * Automatically sorted set, pointing action's perform time to action itself.
     * Contains actions that were performed in observable past.
     */
    final ConcurrentSkipListMap<Instant, Action<?>> performedActions = new ConcurrentSkipListMap<>();

    /**
     * Automatically sorted set, pointing action's perform time to action itself.
     * Contains actions that will be performed in observable future.
     * Empty for the main timeline.
     */
    final ConcurrentSkipListMap<Instant, Action<?>> pendingActions = new ConcurrentSkipListMap<>();

    public Timeline(BaseGameState gameState) {
        this.gameState = gameState;
        offsetToMain = Duration.ZERO;
    }

    /**
     * Main initialization function for the game.
     * Before timeline re-initialization, {@link Timeline.main} must be set to null.
     */
    public static void init(VirtualClock clock, BaseGameState state) {
        if (main != null) return;
        Timeline.clock = clock;
        main = new Timeline(state);
    }

    public static VirtualClock getClock() {
        return clock;
    }

    public Duration getOffsetToMain() {
        return offsetToMain;
    }

    public BaseGameState getGameState() {
        return gameState;
    }

    public <T extends Organism> ITemplate perform(Action<T> action) {
        Instant now = clock.now();
        performedActions.put(now, action);
        return action.perform();
    }
}

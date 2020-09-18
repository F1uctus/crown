package com.crown.time;

import com.crown.BaseGameState;
import com.crown.creatures.Organism;
import com.crown.i18n.ITemplate;
import com.rits.cloning.Cloner;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
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
     * Alternative timeline that is getting changed by some player.
     */
    public static final HashMap<String, Timeline> alternatives = new HashMap<>();

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

    private static boolean initialized = false;

    public Timeline(BaseGameState gameState) {
        this.gameState = gameState;
        offsetToMain = Duration.ZERO;
    }

    /**
     * Main initialization function for the game.
     * Must be invoked only once.
     */
    public static void init(VirtualClock clock, BaseGameState state) {
        if (initialized) return;
        Timeline.clock = clock;
        main = new Timeline(state);
        initialized = true;
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
        var now = clock.now();
        performedActions.put(now, action);
        var result = action.perform();

        // mirror actions from main timeline to alternatives
        if (this == main) {
            for (var alternative : alternatives.values()) {
                var cloner = new Cloner();
                if (action.getTarget() != null) {
                    // skip cloning performer, it is replaced in alternative timeline
                    cloner.nullInsteadOfClone(action.getTarget().getClass());
                }
                var alternativeAction = cloner.deepClone(action);
                // player from alternative timeline has exactly
                // the same type as player from main.
                // noinspection unchecked
                alternativeAction.setTarget((T) alternative.gameState.players.get(action.getTarget().getKeyName()));
                alternative.pendingActions.put(now.minus(offsetToMain), alternativeAction);
            }
        }
        return result;
    }

    @SuppressWarnings("HardCodedStringLiteral")
    private static String encodeName(Organism traveller) {
        return traveller.getKeyName() + "::clone";
    }
}

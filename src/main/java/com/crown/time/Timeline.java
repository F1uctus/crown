package com.crown.time;

import com.crown.BaseGameState;
import com.crown.creatures.Creature;
import com.crown.i18n.I18n;
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
     * An action performing timeline actions flow.
     * Invoked automatically when time travelling is performed.
     * It is repeating actions from future the same way they were performed in the main timeline.
     */
    final TimelineFlowAction flowAction = new TimelineFlowAction(this);

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

    public <T extends Creature> ITemplate perform(Action<T> action) {
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

    /**
     * Performs all pending actions up to {@code targetPoint} if
     * they were not made by creature with {@code travellerOriginName}
     * (it's actions will be overridden by time traveller).
     */
    private void commitTo(Instant targetPoint, String travellerOriginName) {
        for (Instant actionPoint : pendingActions.keySet()) {
            if (actionPoint.isBefore(targetPoint)) {
                var action = performedActions.get(actionPoint);
                // skip actions made by traveller's origin
                if (action.getTarget().getKeyName().equals(travellerOriginName)) continue;
                action.rollback();
                performedActions.remove(actionPoint);
                pendingActions.put(actionPoint, action);
            }
        }
    }

    /**
     * Discards all actions in this timeline made up to {@code targetPoint}.
     */
    private void rollbackTo(Instant targetPoint) {
        for (Instant actionPoint : performedActions.keySet()) {
            if (actionPoint.isAfter(targetPoint)) {
                var action = performedActions.get(actionPoint);
                action.rollback();
                performedActions.remove(actionPoint);
                pendingActions.put(actionPoint, action);
            }
        }
    }

    /**
     * Moves specified creature {@code traveller} back in time
     * to specified {@code targetPoint} in the new timeline.
     * Returns a copy of provided creature moved
     * to the alternative timeline in the past.
     * Original traveller is renamed, his id is changed.
     */
    public static ITemplate move(Creature traveller, Instant targetPoint) {
        if (!targetPoint.isBefore(clock.now())) {
            return I18n.of("time.travel.future");
        }

        var timelinePresent = traveller.getTimeline();
        var travellerName = traveller.getKeyName();

        // TODO check parallel travelling
        if (!alternatives.isEmpty()) {
            return I18n.of("time.travel.parallel");
        }

        // if (alternatives.containsKey(travellerName)) {
        //     return I18n.of("time.travel.fromPast");
        // }

        // cloning timeline
        var cloner = new Cloner();
        cloner.setDumpClonedClasses(true);
        var alternative = cloner.deepClone(timelinePresent);
        alternative.offsetToMain = Duration.between(targetPoint, clock.now());

        // rebind player from future to the past
        var mapFromPast = alternative.gameState.players.get(traveller.getKeyName()).getMap();
        timelinePresent.gameState.players.remove(traveller);
        traveller.setMap(mapFromPast);
        traveller.setTimeline(alternative);
        traveller.setKeyName(encodeName(traveller));
        traveller.newId();
        alternative.gameState.players.add(traveller);

        // rolling timeline back to the past
        alternative.rollbackTo(targetPoint);
        // scheduling clock to redo actions from future
        clock.schedule(alternative.flowAction);

        alternatives.put(travellerName, alternative);
        return I18n.okMessage;
    }

    /**
     * Commits all changes made traveller.
     * (Makes traveller's timeline the main one).
     */
    public static ITemplate commitChanges(Creature traveller) {
        var tl = traveller.getTimeline();
        if (tl == null || tl == main) {
            return I18n.of("commit.fromMain");
        }

        var travellerName = decodeName(traveller);
        // `originalTraveller` will be deleted
        var originalTraveller = tl.gameState.players.get(travellerName);

        clock.cancel(tl.flowAction);
        clock.freeze(() -> {
            if (!tl.pendingActions.isEmpty()) {
                // instantly moving creature's timeline state to be equal to main
                tl.commitTo(tl.pendingActions.lastKey(), travellerName);
            }
            tl.gameState.players.remove(travellerName);
            originalTraveller.setMap(null);
            originalTraveller.setTimeline(null);
            tl.gameState.rename(traveller, travellerName);
            main = tl;
            alternatives.remove(travellerName);
        });

        return I18n.okMessage;
    }

    /**
     * Undoes all changes made by traveller in the alternative timeline.
     * Drops traveller back to the main timeline.
     * Returns original player's key name.
     */
    public static ITemplate rollbackChanges(Creature traveller) {
        var tl = traveller.getTimeline();
        if (tl == null || tl == main) {
            return I18n.of("rollback.fromMain");
        }
        if (main.gameState.players.size() == 0) {
            return I18n.of("rollback.mainTimelineEmpty");
        }

        var travellerName = decodeName(traveller);
        // `traveller` will be deleted
        var originalTraveller = tl.gameState.players.get(travellerName);

        clock.cancel(tl.flowAction);
        clock.freeze(() -> {
            originalTraveller.setMap(main.gameState.players.first().getMap());
            originalTraveller.setTimeline(main);
            traveller.setMap(null);
            traveller.setTimeline(null);
            main.gameState.players.add(originalTraveller);
            alternatives.remove(travellerName);
        });

        return I18n.raw(travellerName);
    }

    @SuppressWarnings("HardCodedStringLiteral")
    private static String encodeName(Creature traveller) {
        return traveller.getKeyName() + "::clone";
    }

    @SuppressWarnings("HardCodedStringLiteral")
    private static String decodeName(Creature traveller) {
        return traveller.getKeyName().replace("::clone", "");
    }
}

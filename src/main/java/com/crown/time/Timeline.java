package com.crown.time;

import com.crown.BaseGameState;
import com.crown.creatures.Creature;
import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.crown.maps.Map;
import com.rits.cloning.Cloner;
import org.apache.commons.lang3.tuple.Pair;

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
     * they were not made by creature with {@code originalTravellerName}
     * (it's actions will be overridden by time traveller).
     */
    private void commitAll(String originalTravellerName) {
        for (Instant actionPoint : pendingActions.keySet()) {
            var action = pendingActions.get(actionPoint);
            // skip actions made by original traveller
            if (action.getTarget().getKeyName().equals(originalTravellerName)) continue;
            action.perform();
            performedActions.put(actionPoint, action);
            pendingActions.remove(actionPoint);
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

    private Creature originalTraveller;
    private Map originalMap;

    /**
     * Moves specified creature {@code traveller} back in time
     * to specified {@code targetPoint} in the new timeline.
     * Returns a copy of provided creature moved
     * to the alternative timeline in the past.
     */
    public static Pair<ITemplate, Creature> move(Creature traveller, Instant targetPoint) {
        if (!targetPoint.isBefore(clock.now())) {
            return Pair.of(I18n.of("time.travel.future"), traveller);
        }
        if (traveller.getTimeline() != main) {
            return Pair.of(I18n.of("time.travel.notFromMain"), traveller);
        }

        // TODO check parallel travelling
        if (!alternatives.isEmpty()) {
            return Pair.of(I18n.of("time.travel.parallel"), traveller);
        }

        final Creature[] travellerClone = new Creature[1];
        clock.freeze(() -> {
            // cloning timeline
            var cloner = new Cloner();
            // DEBUG
            //  cloner.setDumpClonedClasses(true);
            var timelineClone = cloner.deepClone(main);
            timelineClone.offsetToMain = Duration.between(targetPoint, clock.now());
            timelineClone.originalTraveller = traveller;
            timelineClone.originalMap = traveller.getMap();

            // unbind original traveller from environment
            traveller.setMap(null);
            traveller.setTimeline(null);
            main.gameState.players.remove(traveller);

            // cloning traveller to <name>::clone
            travellerClone[0] = cloner.deepClone(traveller);
            var mapClone = timelineClone.gameState.players.get(traveller.getKeyName()).getMap();
            travellerClone[0].setMap(mapClone);
            travellerClone[0].setTimeline(timelineClone);
            travellerClone[0].setKeyName(encodeName(traveller));
            travellerClone[0].newId();
            timelineClone.gameState.players.add(travellerClone[0]);

            // rolling timeline back to the past
            timelineClone.rollbackTo(targetPoint);
            // scheduling clock to redo actions from future
            clock.schedule(timelineClone.flowAction);

            alternatives.put(traveller.getKeyName(), timelineClone);
        });
        return Pair.of(I18n.okMessage, travellerClone[0]);
    }

    /**
     * Commits all changes made {@code travellerClone}.
     * (Makes {@code travellerClone}'s timeline the main one).
     */
    public static ITemplate commitChanges(Creature travellerClone) {
        var tl = travellerClone.getTimeline();
        if (tl == null || tl == main) {
            return I18n.of("commit.fromMain");
        }

        clock.cancel(tl.flowAction);
        clock.freeze(() -> {
            var originalTravellerName = tl.originalTraveller.getKeyName();
            // instantly moving creature's timeline state to be equal to main
            tl.commitAll(originalTravellerName);
            // `originalTraveller` is deleted
            var originalTravellerInPast = tl.gameState.players.get(originalTravellerName);
            originalTravellerInPast.setMap(null);
            originalTravellerInPast.setTimeline(null);
            tl.gameState.players.remove(originalTravellerName);
            tl.originalTraveller = null;
            tl.originalMap = null;
            // clone renamed to original name
            tl.gameState.rename(travellerClone, originalTravellerName);
            main = tl;
            alternatives.remove(originalTravellerName);
        });

        return I18n.okMessage;
    }

    /**
     * Undoes all changes made by travellerClone in the alternative timeline.
     * Drops original traveller back to the main timeline.
     */
    public static Pair<ITemplate, Creature> rollbackChanges(Creature travellerClone) {
        var tl = travellerClone.getTimeline();
        if (tl == null || tl == main) {
            return Pair.of(I18n.of("rollback.fromMain"), travellerClone);
        }
        if (main.gameState.players.size() == 0) {
            return Pair.of(I18n.of("rollback.mainTimelineEmpty"), travellerClone);
        }

        clock.cancel(tl.flowAction);
        clock.freeze(() -> {
            // bind original traveller back to the main timeline
            tl.originalTraveller.setMap(tl.originalMap);
            tl.originalTraveller.setTimeline(main);
            main.gameState.players.add(tl.originalTraveller);
            // `travellerClone` is deleted
            travellerClone.setMap(null);
            travellerClone.setTimeline(null);
            alternatives.remove(tl.originalTraveller.getKeyName());
        });

        return Pair.of(I18n.okMessage, tl.originalTraveller);
    }

    @SuppressWarnings("HardCodedStringLiteral")
    private static String encodeName(Creature traveller) {
        return traveller.getKeyName() + "::clone";
    }
}

package com.crown.time;

import com.crown.BaseGameState;
import com.crown.creatures.Creature;
import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.rits.cloning.Cloner;

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

    public static Timeline alternative;

    /**
     * Main game clock, common across all timelines.
     * Time difference is specified by {@link #offsetToMain}.
     */
    private static VirtualClock clock;

    /**
     * Time difference of [main timeline] - [this timeline].
     * Equal to 0 for main timeline, obviously.
     */
    private Duration offsetToMain;

    private final TimelineMirrorAction mirrorAction = new TimelineMirrorAction(this);
    private final BaseGameState gameState;
    final ConcurrentSkipListMap<Instant, Action<?>> performedActions = new ConcurrentSkipListMap<>();
    final ConcurrentSkipListMap<Instant, Action<?>> pendingActions = new ConcurrentSkipListMap<>();

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
        // mirror actions from main timeline to alternative
        if (this == main && alternative != null) {
            var cloner = new Cloner();
            // skip cloning performer, it is replaced in alternative timeline
            cloner.nullInsteadOfClone(action.getPerformer().getClass());
            var alternativeAction = cloner.deepClone(action);
            // player from alternative timeline has exactly
            // the same type as player from main.
            // noinspection unchecked
            alternativeAction.setPerformer((T) alternative.gameState.players.get(action.getPerformer().getKeyName()));
            alternative.pendingActions.put(now.minus(offsetToMain), alternativeAction);
        }
        return result;
    }

    /**
     * Main logic of backward time-travelling.
     * Discards all actions in this timeline made up to {@code point},
     * and then re-schedules these actions to "future" of this timeline.
     */
    private void rollbackTo(Instant point) {
        // Throws ConcurrentModificationException, so
        // noinspection ForLoopReplaceableByForEach
        for (Instant actionPoint : performedActions.keySet()) {
            if (actionPoint.isAfter(point)) {
                var action = performedActions.get(actionPoint);
                action.rollback();
                performedActions.remove(actionPoint);
                pendingActions.put(actionPoint, action);
            }
        }
        // scheduling clock to make actions from
        // future in the same way they were performed.
        clock.scheduleAction(mirrorAction);
    }

    /**
     * Moves specified creature {@code traveller} back in time
     * to specified {@code point} in the new timeline.
     * Returns a copy of provided creature moved
     * to the alternative timeline in the past.
     * Original traveller is renamed, his id is changed.
     */
    public static void moveToThePast(Creature traveller, Instant point) {
        // ensure we're travelling into past
        assert point.isBefore(clock.now());
        var timelinePresent = traveller.getTimeline();

        var cloner = new Cloner();
        cloner.setDumpClonedClasses(true);
        alternative = cloner.deepClone(timelinePresent);
        alternative.offsetToMain = Duration.between(point, clock.now());

        // rebind player from future to the past
        var mapFromPast = alternative.gameState.players.get(traveller.getKeyName()).getMap();
        timelinePresent.gameState.players.remove(traveller);
        traveller.setMap(mapFromPast);
        traveller.setTimeline(alternative);

        // noinspection HardCodedStringLiteral
        traveller.setKeyName(traveller.getKeyName() + "::clone");
        traveller.newId();

        // binding clone from future to the cloned timeline
        alternative.gameState.players.add(traveller);

        // rolling timeline back to the past
        alternative.rollbackTo(point);
    }

    /**
     * Commits all changes made by {@code cloned}.
     * (Makes creature's timeline the main one).
     */
    @SuppressWarnings("HardCodedStringLiteral")
    public ITemplate commitChanges(Creature cloned) {
        var tl = cloned.getTimeline();
        if (tl == null || tl == main) {
            return I18n.of("commit.mainTimeline");
        }
        getGameClock().cancelAction(mirrorAction);
        // original player is getting deleted
        var originalPlayer = tl.gameState.players.get(
            cloned.getKeyName().replace("::clone", "")
        );
        tl.gameState.players.remove(originalPlayer.getKeyName());
        originalPlayer.setMap(null);
        originalPlayer.setTimeline(null);
        tl.gameState.rename(cloned, originalPlayer.getKeyName());
        setMain(tl);
        return I18n.okMessage;
    }

    /**
     * Undoes all changes made by {@code cloned} in the alternative timeline.
     * Drops cloned creature back to the main timeline.
     * Returns original player's key name.
     */
    @SuppressWarnings("HardCodedStringLiteral")
    public ITemplate rollbackChanges(Creature cloned) {
        var tl = cloned.getTimeline();
        if (tl == null || tl == main) {
            return I18n.of("rollback.mainTimeline");
        }
        if (main.gameState.players.size() == 0) {
            return I18n.of("rollback.mainTimelineEmpty");
        }
        getGameClock().cancelAction(mirrorAction);
        var originalPlayer = cloned.getTimeline().gameState.players.get(
            cloned.getKeyName().replace("::clone", "")
        );
        originalPlayer.setMap(main.gameState.players.first().getMap());
        originalPlayer.setTimeline(main);
        main.gameState.players.add(originalPlayer);
        return I18n.raw(originalPlayer.getKeyName());
    }
}

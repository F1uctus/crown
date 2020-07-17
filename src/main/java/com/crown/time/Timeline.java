package com.crown.time;

import com.crown.BaseGameState;
import com.crown.creatures.Creature;
import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.rits.cloning.Cloner;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.TreeMap;

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
    final ArrayList<Action<?>> performedActions = new ArrayList<>();
    final TreeMap<Instant, Action<?>> pendingActions = new TreeMap<>();

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
        performedActions.add(action);
        var result = action.perform();
        // mirror actions from main timeline to alternative
        if (this == main && alternative != null) {
            var cloner = new Cloner();
            // skip cloning properties, they're replaced in alternative timeline
            cloner.nullInsteadOfClone(action.getPerformer().getClass());
            cloner.nullInsteadOfClone(action.getPoint().getClass());
            var alternativeAction = cloner.deepClone(action);
            // player from alternative timeline has exactly
            // the same type as player from main.
            // noinspection unchecked
            alternativeAction.setPerformer((T) alternative.gameState.players.get(action.getPerformer().getKeyName()));
            alternativeAction.setPoint(action.getPoint().minus(offsetToMain));
            alternative.pendingActions.put(alternativeAction.getPoint(), alternativeAction);
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
        for (int i = 0; i < performedActions.size(); i++) {
            Action<?> a = performedActions.get(i);
            if (a.getPoint().isAfter(point)) {
                a.rollback();
                performedActions.remove(a);
                pendingActions.put(a.getPoint().minus(offsetToMain), a);
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
     */
    public static Creature moveToThePast(Creature traveller, Instant point) {
        // Ensure we're travelling into past
        assert point.isBefore(clock.now());

        var cloner = new Cloner();
        cloner.setDumpClonedClasses(true);
        var timelineClone = cloner.deepClone(traveller.getTimeline());
        alternative = timelineClone;
        timelineClone.offsetToMain = Duration.between(point, clock.now());

        // clear fields to unbind player from any environment
        var mapFromPast = timelineClone.gameState.players.get(traveller.getKeyName()).getMap();
        traveller.setMap(null);
        traveller.getTimeline().gameState.players.remove(traveller);
        traveller.setTimeline(null);
        // noinspection HardCodedStringLiteral
        traveller.setKeyName(traveller.getKeyName() + "::clone");
        traveller.newId();

        // making a copy for the new timeline
        var travellerClone = cloner.deepClone(traveller);

        // binding clone from future to the cloned timeline
        travellerClone.setTimeline(timelineClone);
        travellerClone.setMap(mapFromPast);

        // rolling timeline back to the past
        timelineClone.rollbackTo(point);

        timelineClone.gameState.players.add(travellerClone);
        return travellerClone;
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

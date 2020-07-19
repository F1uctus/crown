package com.crown.time;

import com.crown.common.ObjectsMap;
import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Simplified clock logic for virtual game time.
 */
public class VirtualClock {
    public final int secondLength;

    private boolean paused = false;
    private Instant timePoint;
    private Timer timer;
    private final Runnable tickAction;
    private final ObjectsMap<TimelineMirrorAction> instantActions = new ObjectsMap<>();

    /**
     * Creates new game clock with Earth-like time units
     * and custom second length.
     */
    public VirtualClock(
        int secondLength,
        @NotNull Runnable tickAction
    ) {
        assert secondLength > 0;
        this.secondLength = secondLength;
        this.tickAction = tickAction;
    }

    /**
     * Starts this instance of clock at random time point.
     */
    public VirtualClock startAtRnd() {
        long begin = Timestamp.valueOf("0001-01-01 00:00:00").getTime();
        long end = Timestamp.from(Instant.now()).getTime();
        long diff = end - begin + 1;
        return startAt(
            new Timestamp(begin + (long) (Math.random() * diff)).toInstant()
        );
    }

    /**
     * Starts this instance of clock at specified time point.
     */
    public VirtualClock startAt(Instant point) {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        timePoint = point;
        start();
        return this;
    }

    /**
     * Schedules time units increment
     * and {@link VirtualClock#tickAction} running every
     * {@link VirtualClock#secondLength} milliseconds period of time.
     */
    private void start() {
        timer = new Timer();
        final int period = 10;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!paused) {
                    timePoint = timePoint.plusMillis(period);
                    tickAction.run();
                    for (var a : instantActions) {
                        a.run();
                    }
                }
            }
        }, 0, period);
    }

    /**
     * <pre>
     * - pauses the clock
     * - cancels timeline mirroring actions
     * - performs an action (commit/rollback logic)
     * - resumes the clock
     * </pre>
     */
    void freeze(Timeline t, Runnable action) {
        paused = true;
        cancelAction(t.mirrorAction);
        action.run();
        paused = false;
    }

    /**
     * Schedules an action to execute every 10 ms of virtual clock timer.
     */
    public void scheduleAction(TimelineMirrorAction action) {
        instantActions.add(action);
    }

    /**
     * Removes last action virtual clock timer schedule.
     */
    public void cancelAction(TimelineMirrorAction action) {
        instantActions.remove(action);
    }

    /**
     * Returns instant time of this clock.
     */
    public Instant now() {
        return timePoint;
    }
}

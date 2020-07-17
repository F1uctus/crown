package com.crown.time;

import com.crown.common.ObjectsMap;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Simplified clock logic for virtual game time.
 */
public class VirtualClock {
    public final int secondLength;

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

    private int elapsedMilliseconds = 0;

    /**
     * Schedules time units increment
     * and {@link VirtualClock#tickAction} running every
     * {@link VirtualClock#secondLength} milliseconds period of time.
     */
    private void start() {
        timer = new Timer();
        elapsedMilliseconds = 0;
        final int period = 10;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                elapsedMilliseconds += period;
                // this is done to avoid overriding actions
                // that happen almost simultaneously (in bounds of 1 game-second)
                timePoint = timePoint.plusNanos(1);
                // Doing clock units increment if needed
                if (elapsedMilliseconds >= secondLength) {
                    elapsedMilliseconds = 0;
                    timePoint = timePoint.plusSeconds(1);
                    tickAction.run();
                }
                for (var a : instantActions) {
                    a.run();
                }
            }
        }, 0, period);
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

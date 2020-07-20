package com.crown.time;

import com.crown.common.ObjectsMap;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Abstraction over in-game virtual clock logic.
 */
public class VirtualClock {
    private boolean paused = false;
    private Instant instantValue;
    private Timer timer;

    private final int tickPeriod;
    private final Runnable tickAction;

    private final ObjectsMap<TimelineFlowAction> scheduledActions = new ObjectsMap<>();

    /**
     * Creates new game clock with Earth-like time units.
     *
     * @param tickPeriod Specifies a delay (in milliseconds) between
     *                   calls to the tick action.
     */
    public VirtualClock(int tickPeriod, @NotNull Runnable tickAction) {
        this.tickPeriod = tickPeriod;
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
        instantValue = point;
        start();
        return this;
    }

    /**
     * Schedules time units increment and {@link VirtualClock#tickAction}
     * running every {@link VirtualClock#tickPeriod} milliseconds of time.
     */
    private void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!paused) {
                    instantValue = instantValue.plusMillis(tickPeriod);
                    tickAction.run();
                    for (var a : scheduledActions) {
                        a.run();
                    }
                }
            }
        }, 0, tickPeriod);
    }

    /**
     * Pauses the clock, performs an action; then resumes the clock.
     */
    void freeze(Runnable action) {
        paused = true;
        action.run();
        paused = false;
    }

    /**
     * Schedules an action to execute every {@link VirtualClock#tickPeriod} ms.
     */
    void schedule(TimelineFlowAction action) {
        scheduledActions.add(action);
    }

    /**
     * Removes last action virtual clock timer schedule.
     */
    void cancel(TimelineFlowAction action) {
        scheduledActions.remove(action);
    }

    /**
     * Returns instant time of this clock.
     */
    public Instant now() {
        return instantValue;
    }
}

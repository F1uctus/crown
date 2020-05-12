package com.crown.time;

import com.crown.common.utils.Random;

import java.time.Year;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Simplified clock logic for virtual game time.
 */
public class VirtualClock {
    public final int maxYears = Integer.MAX_VALUE;
    private int years;

    public final int maxMonths;
    private int months;

    public final int maxWeeks;
    private int weeks;

    public final int maxDays;
    private int days;

    public final int maxHours;
    private int hours;

    public final int maxMinutes;
    private int minutes;

    public final int maxSeconds;
    private int seconds;

    public final int secondLength;
    private Timer timer;
    private final Runnable tickAction;

    /**
     * Creates new game clock with Earth-like timing
     * (12m/4w/7d/24h/60m/60s) with custom second length.
     */
    public VirtualClock(
        int secondLength,
        Runnable tickAction
    ) {
        this(
            12,
            4,
            7,
            24,
            60,
            60,
            secondLength,
            tickAction
        );
    }

    public VirtualClock(
        int maxMonths,
        int maxWeeks,
        int maxDays,
        int maxHours,
        int maxMinutes,
        int maxSeconds,
        int secondLength,
        Runnable tickAction
    ) {
        this.maxMonths = maxMonths;
        this.maxWeeks = maxWeeks;
        this.maxDays = maxDays;
        this.maxHours = maxHours;
        this.maxMinutes = maxMinutes;
        this.maxSeconds = maxSeconds;
        this.secondLength = secondLength;
        this.tickAction = tickAction;
    }

    public VirtualClock startAtRnd() {
        return startAt(
            new TimePoint(
                Random.getInt(1000, Year.now().getValue() + 1),
                Random.getInt(1, maxMonths + 1),
                Random.getInt(1, maxWeeks + 1),
                Random.getInt(1, maxDays + 1),
                Random.getInt(1, maxHours),
                Random.getInt(1, maxMinutes),
                Random.getInt(1, maxSeconds)
            )
        );
    }

    /**
     * Starts this instance of clock
     */
    public VirtualClock startAt(TimePoint point) {
        if (timer != null) {
            timer.cancel();
        }
        this.years = point.years;
        this.months = point.months;
        this.weeks = point.weeks;
        this.days = point.days;
        this.hours = point.hours;
        this.minutes = point.minutes;
        this.seconds = point.seconds;
        start();
        return this;
    }

    private void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seconds++;
                if (seconds >= maxSeconds) {
                    seconds = 0;
                    minutes++;
                    if (minutes >= maxMinutes) {
                        minutes = 0;
                        hours++;
                        if (hours >= maxHours) {
                            hours = 0;
                            days++;
                            if (days > maxDays) {
                                days = 1;
                                weeks++;
                                if (weeks > maxWeeks) {
                                    weeks = 1;
                                    months++;
                                    if (months > maxMonths) {
                                        months = 1;
                                        years++;
                                    }
                                }
                            }
                        }
                    }
                }
                tickAction.run();
            }
        }, 0, secondLength);
    }

    TimePoint now() {
        return new TimePoint(years, months, weeks, days, hours, minutes, seconds);
    }
}

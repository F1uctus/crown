package com.crown.time;

import com.crown.common.utils.Random;

import java.io.Serializable;
import java.time.Year;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Simplified clock logic for virtual game time.
 */
public class VirtualClock implements Serializable {
    public static final int maxYears = Integer.MAX_VALUE;
    private static int years;

    private static int maxMonths;
    private static int months;

    private static int maxWeeks;
    private static int weeks;

    private static int maxDays;
    private static int days;

    private static int maxHours;
    private static int hours;

    private static int maxMinutes;
    private static int minutes;

    private static int maxSeconds;
    private static int seconds;

    private static int secondLength;

    private static Timer timer;
    private static Runnable tickAction;

    public void setPeriods(
        int maxMonths,
        int maxWeeks,
        int maxDays,
        int maxHours,
        int maxMinutes,
        int maxSeconds,
        int secondLength,
        Runnable tickAction
    ) {
        VirtualClock.maxMonths = maxMonths;
        VirtualClock.maxWeeks = maxWeeks;
        VirtualClock.maxDays = maxDays;
        VirtualClock.maxHours = maxHours;
        VirtualClock.maxMinutes = maxMinutes;
        VirtualClock.maxSeconds = maxSeconds;
        VirtualClock.secondLength = secondLength;
        VirtualClock.tickAction = tickAction;
    }

    /**
     * Starts game clock with Earth-like timing
     * (12m/4w/7d/24h/60m/60s) and custom second length.
     */
    public void startEarthLike(
        int secondLength,
        Runnable tickAction
    ) {
        VirtualClock.secondLength = secondLength;
        VirtualClock.tickAction = tickAction;
        startAt(
            new TimePoint(
                maxYears,
                12,
                4,
                7,
                24,
                60,
                60
            )
        );
    }

    public static void startAtRnd(
        int secondLength,
        Runnable tickAction
    ) {
        VirtualClock.secondLength = secondLength;
        VirtualClock.tickAction = tickAction;
        startAt(
            new TimePoint(
                Random.getInt(1, Year.now().getValue() + 1),
                Random.getInt(1, maxMonths + 1),
                Random.getInt(1, maxWeeks + 1),
                Random.getInt(1, maxDays + 1),
                Random.getInt(1, maxHours),
                Random.getInt(1, maxMinutes),
                Random.getInt(1, maxSeconds)
            )
        );
    }

    public static void startAt(TimePoint point) {
        if (timer != null) {
            timer.cancel();
        }
        years = point.years;
        months = point.months;
        weeks = point.weeks;
        days = point.days;
        hours = point.hours;
        minutes = point.minutes;
        seconds = point.seconds;
        start();
    }

    private static void start() {
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

    public static TimePoint now() {
        return new TimePoint(years, months, weeks, days, hours, minutes, seconds);
    }

    public static int getMaxMonths() {
        return maxMonths;
    }

    public static int getMaxWeeks() {
        return maxWeeks;
    }

    public static int getMaxDays() {
        return maxDays;
    }

    public static int getMaxHours() {
        return maxHours;
    }

    public static int getMaxMinutes() {
        return maxMinutes;
    }

    public static int getMaxSeconds() {
        return maxSeconds;
    }
}

package com.crown.time;

import java.io.Serializable;

public class TimePoint implements Serializable {
    public final int years;
    public final int months;
    public final int weeks;
    public final int days;
    public final int hours;
    public final int minutes;
    public final int seconds;

    public static TimePoint zero =
        new TimePoint(0, 0, 0, 0, 0, 0, 0);

    public TimePoint(
        int years,
        int months,
        int weeks,
        int days,
        int hours,
        int minutes,
        int seconds
    ) {
        this.years = years;
        this.months = months;
        this.weeks = weeks;
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public TimePoint plus(int seconds) {
        return plus(0, 0, 0, 0, 0, 0, seconds);
    }

    public TimePoint plus(int minutes, int seconds) {
        return plus(0, 0, 0, 0, 0, minutes, seconds);
    }

    public TimePoint plus(int hours, int minutes, int seconds) {
        return plus(0, 0, 0, 0, hours, minutes, seconds);
    }

    public TimePoint plus(
        int years,
        int months,
        int weeks,
        int days,
        int hours,
        int minutes,
        int seconds
    ) {
        return new TimePoint(
            this.years + years,
            this.months + months,
            this.weeks + weeks,
            this.days + days,
            this.hours + hours,
            this.minutes + minutes,
            this.seconds + seconds
        );
    }

    public TimePoint plus(TimePoint other) {
        return new TimePoint(
            years + other.years,
            months + other.months,
            weeks + other.weeks,
            days + other.days,
            hours + other.hours,
            minutes + other.minutes,
            seconds + other.seconds
        );
    }

    public TimePoint minus() {
        return new TimePoint(
            -years,
            -months,
            -weeks,
            -days,
            -hours,
            -minutes,
            -seconds
        );
    }

    public boolean gt(TimePoint point) {
        return years > point.years
               || (years == point.years
                   && months > point.months
                   || (months == point.months
                       && days > point.days
                       || (days == point.days
                           && hours > point.hours
                           || (hours == point.hours
                               && minutes > point.minutes
                               || (minutes == point.minutes
                                   && seconds > point.seconds)))));
    }

    public boolean gteq(TimePoint point) {
        return years > point.years
               || (years == point.years
                   && months > point.months
                   || (months == point.months
                       && days > point.days
                       || (days == point.days
                           && hours > point.hours
                           || (hours == point.hours
                               && minutes > point.minutes
                               || (minutes == point.minutes
                                   && seconds >= point.seconds)))));

    }

    public boolean lt(TimePoint point) {
        return years < point.years
               || (years == point.years
                   && months < point.months
                   || (months == point.months
                       && days < point.days
                       || (days == point.days
                           && hours < point.hours
                           || (hours == point.hours
                               && minutes < point.minutes
                               || (minutes == point.minutes
                                   && seconds < point.seconds)))));
    }

    public boolean lteq(TimePoint point) {
        return years < point.years
               || (years == point.years
                   && months < point.months
                   || (months == point.months
                       && days < point.days
                       || (days == point.days
                           && hours < point.hours
                           || (hours == point.hours
                               && minutes < point.minutes
                               || (minutes == point.minutes
                                   && seconds <= point.seconds)))));
    }

    @Override
    public String toString() {
        // noinspection HardCodedStringLiteral
        return String.format("%04d", years) +
               "/" + String.format("%02d", months) +
               "/" + String.format("%02d", days)
               + ", " + hours + ":" + minutes + ":" + seconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TimePoint timePoint = (TimePoint) o;

        if (years != timePoint.years) {
            return false;
        }
        if (months != timePoint.months) {
            return false;
        }
        if (weeks != timePoint.weeks) {
            return false;
        }
        if (days != timePoint.days) {
            return false;
        }
        if (hours != timePoint.hours) {
            return false;
        }
        if (minutes != timePoint.minutes) {
            return false;
        }
        return seconds == timePoint.seconds;
    }
}

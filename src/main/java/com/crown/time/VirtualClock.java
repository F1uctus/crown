package com.crown.time;

/**
 * Simplified clock logic for virtual game time.
 */
public class VirtualClock {
    /**
     * Virtual hours count in virtual day.
     */
    public final int maxHours;

    /**
     * Virtual minutes count in virtual hour.
     */
    public final int maxMinutes;

    /**
     * Virtual seconds count in virtual minute.
     */
    public final int maxSeconds;

    /**
     * Virtual second length, specified in real milliseconds.
     */
    public final int secondLength;

    /**
     * Creates new game clock with Earth-like timing
     * (24h/60m/60s) with custom second length.
     * If second length = 1000, then this clock day interval
     * is even with Earth one (excluding leap years).
     */
    public VirtualClock(
        int secondLength
    ) {
        this.maxHours = 24;
        this.maxMinutes = 60;
        this.maxSeconds = 60;
        this.secondLength = secondLength;
    }

    public VirtualClock(
        int maxHours,
        int maxMinutes,
        int maxSeconds,
        int secondLength
    ) {
        this.maxHours = maxHours;
        this.maxMinutes = maxMinutes;
        this.maxSeconds = maxSeconds;
        this.secondLength = secondLength;
    }

    VirtualClock tick() {
        return this;
    }
}

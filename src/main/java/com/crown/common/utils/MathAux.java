package com.crown.common.utils;

public class MathAux {
    /**
     * Constraints a number into a range of [min, max] if needed.
     */
    public static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Returns max if value is not in [min, max] range.
     */
    public static int clampUp(int value, int min, int max) {
        return value >= min && value <= max ? value : max;
    }

    /**
     * Returns min if value is not in [min, max] range.
     */
    public static int clampDown(int value, int min, int max) {
        return value >= min && value <= max ? value : min;
    }
}

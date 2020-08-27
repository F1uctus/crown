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

    /**
     * Returns 1 if value >= 0, and returns 0 otherwise.
     */
    public static int positiveSign(int value) {
        return value >= 0 ? 1 : 0;
    }

    /**
     * @return the value {@code 0} if {@code x == 0};
     * a value {@code -1} if {@code x < 0}; and
     * a value {@code 1} if {@code x > 0}
     */
    public static int signum(int value) {
        return Integer.compare(value, 0);
    }

    /**
     * Variadic version of Math.max
     */
    public static int max(int a, int b, int... c) {
        int max = Math.max(a, b);
        if (c != null) {
            for (int i : c) {
                if (i > max) max = i;
            }
        }
        return max;
    }

    /**
     * Variadic version of Math.min
     */
    public static int min(int a, int b, int... c) {
        int min = Math.min(a, b);
        if (c != null) {
            for (int i : c) {
                if (i < min) min = i;
            }
        }
        return min;
    }

    public static int normalizeAngle(int angle) {
        if (angle < 0) {
            angle %= 360;
            angle += 360;
        }
        if (angle > 360) {
            angle %= 360;
        }
        return angle;
    }
}

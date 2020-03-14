package com.crown.Constraints;

import com.crown.Common.Constraint;
import javafx.util.Pair;

import java.util.function.Consumer;

public class NumRange extends Constraint<Double> {
    private final double min;
    private final double max;

    private Consumer<NumRange> onMinValue = (c) -> {
    };
    private Consumer<NumRange> onMaxValue = (c) -> {
    };

    public NumRange(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public NumRange withTriggerOnMinValue(Consumer<NumRange> fn) {
        this.onMinValue = fn;
        return this;
    }

    public NumRange withTriggerOnMaxValue(Consumer<NumRange> fn) {
        this.onMaxValue = fn;
        return this;
    }

    @Override
    protected Pair<Boolean, Double> apply(Double oldValue, Double newValue) {
        newValue = Math.max(min, Math.min(max, newValue));
        if (newValue == min) {
            onMinValue.accept(this);
        } else if (newValue == max) {
            onMaxValue.accept(this);
        }
        return new Pair(true, newValue);
    }

    @Override
    protected String getRange() {
        return min + " - " + max;
    }
}

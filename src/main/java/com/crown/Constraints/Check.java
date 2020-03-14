package com.crown.Constraints;

import com.crown.Common.Constraint;
import javafx.util.Pair;

import java.util.function.BiFunction;

public class Check<T> extends Constraint<T> {
    private final BiFunction<T, T, Boolean> condition;

    public Check(BiFunction<T, T, Boolean> condition) {
        this.condition = condition;
    }

    @Override
    protected Pair<Boolean, T> apply(T oldValue, T newValue) {
        return new Pair(true, newValue);
    }

    @Override
    protected String getRange() {
        return null;
    }
}

package com.crown.constraints;

import com.crown.common.Constraint;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.BiFunction;

public class Check<T> extends Constraint<T> {
    private final BiFunction<T, T, Boolean> condition;

    public Check(BiFunction<T, T, Boolean> condition) {
        this.condition = condition;
    }

    @Override
    protected Pair<Boolean, T> apply(T oldValue, T newValue) {
        return Pair.of(true, newValue);
    }

    @Override
    protected String getRange() {
        return null;
    }
}

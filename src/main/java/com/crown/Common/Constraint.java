package com.crown.common;

import org.apache.commons.lang3.tuple.Pair;

public abstract class Constraint<T> {
    protected abstract Pair<Boolean, T> apply(T oldValue, T newValue);

    protected abstract String getRange();
}

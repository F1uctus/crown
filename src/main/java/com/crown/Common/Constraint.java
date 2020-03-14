package com.crown.Common;

import javafx.util.Pair;

public abstract class Constraint<T> {
    protected abstract Pair<Boolean, T> apply(T oldValue, T newValue);

    protected abstract String getRange();
}

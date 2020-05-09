package com.crown.time;

import com.crown.i18n.ITemplate;

import java.util.function.Function;

public abstract class Action {
    public abstract ITemplate perform();

    public abstract ITemplate rollback();
}

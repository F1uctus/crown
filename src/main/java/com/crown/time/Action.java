package com.crown.time;

import com.crown.i18n.ITemplate;

import java.util.function.Function;

public abstract class Action {
    public abstract ITemplate perform();

    public abstract ITemplate rollback();

    public Action changeProperty(Function<Integer, ITemplate> changer, int delta) {
        return new Action() {
            @Override
            public ITemplate perform() {
                return changer.apply(delta);
            }

            @Override
            public ITemplate rollback() {
                return changer.apply(-delta);
            }
        };
    }
}

package com.crown.time;

import com.crown.i18n.ITemplate;

public abstract class Action {
    public abstract ITemplate perform();

    public abstract ITemplate rollback();
}

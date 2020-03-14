package com.crown.Common;

import static com.crown.Main.rb;

public abstract class Thing extends NamedObject {
    public Thing(String keyName) {
        super(keyName);
    }

    @Override
    public String getName() {
        return rb.getString("Thing." + getKeyName() + ".Name");
    }

    @Override
    public String getDescription() {
        return rb.getString("Thing." + getKeyName() + ".Description");
    }
}

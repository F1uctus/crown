package com.cotfk.Common;

/**
 * Represents a localizable object with name and description.
 */
public abstract class NamedObject {
    private String keyName;

    public NamedObject(String keyName) {
        this.keyName = keyName;
    }

    protected NamedObject() {
    }

    public String getKeyName() {
        return keyName;
    }

    public abstract String getName();

    public abstract String getDescription();
}

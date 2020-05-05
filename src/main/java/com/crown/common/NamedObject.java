package com.crown.common;

import com.crown.common.utils.Random;
import com.crown.i18n.ITemplate;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a localizable object
 * with {@code id}, {@code name} and {@code description}.
 */
public abstract class NamedObject {
    protected int id;
    protected String name;
    protected String description;
    private final String keyName;

    public NamedObject(@NotNull String keyName) {
        this.id = Random.getId();
        this.keyName = keyName.toLowerCase();
    }

    public int getId() {
        return id;
    }

    public String getKeyName() {
        return keyName;
    }

    public abstract ITemplate getName();

    public abstract ITemplate getDescription();
}

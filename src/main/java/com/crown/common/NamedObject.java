package com.crown.common;

import com.crown.common.utils.Random;
import com.crown.i18n.ITemplate;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Represents a localizable object
 * with {@code id}, {@code name} and {@code description}.
 */
public abstract class NamedObject implements Serializable {
    protected final int id;
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

package com.crown.common;

import com.crown.common.utils.Random;
import com.crown.i18n.ITemplate;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents a localizable object
 * with unique {@link NamedObject#id},
 * resource {@link NamedObject#keyName}, and description.
 */
public abstract class NamedObject {
    private UUID id;
    private String keyName;

    public NamedObject(@NotNull String keyName) {
        this.id = Random.getId();
        this.keyName = keyName.strip().toLowerCase();
    }

    public UUID getId() {
        return id;
    }

    public void newId() {
        id = Random.getId();
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String value) {
        keyName = value.strip().toLowerCase();
    }

    public abstract ITemplate getName();

    public abstract ITemplate getDescription();
}

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
    protected final UUID id;
    protected final String keyName;

    public NamedObject(@NotNull String keyName) {
        this.id = Random.getId();
        this.keyName = keyName.toLowerCase();
    }

    public UUID getId() {
        return id;
    }

    public abstract ITemplate getName();

    public abstract ITemplate getDescription();
}

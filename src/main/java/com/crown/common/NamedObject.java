package com.crown.common;

import com.crown.common.utils.Random;
import com.crown.creatures.Organism;
import com.crown.i18n.I18n;
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

    /**
     * Returns a Universally Unique ID for this game object.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Generates a new UUID for this game object.
     * Used to create a clone for the game object during time travelling process.
     * Think twice before using this method, changing object id will unbind
     * it from {@link ObjectsMap}s and other places in code,
     * so should NOT be used in your code in general.
     */
    public void newId() {
        id = Random.getId();
    }

    /**
     * Sets a new UUID for this game object.
     * Think twice before using this method, changing object id will unbind
     * it from {@link ObjectsMap}s and other places in code,
     * so should NOT be used in your code in general.
     */
    public void setId(UUID value) {
        id = value;
    }

    /**
     * Returns a key name of this object.
     * It can be a key for your I18n resource file,
     * or just a non-localizable name.
     */
    public String getKeyName() {
        return keyName;
    }

    /**
     * Sets a new key name for this object.
     * It can be a key for your I18n resource file, or just a non-localizable name.
     * Used to create a clone for the game object during time travelling process,
     * so should NOT be used in your code in general.
     * Use {@link com.crown.BaseGameState#rename(Organism, String)} instead.
     */
    public void setKeyName(String value) {
        keyName = value.strip().toLowerCase();
    }

    /**
     * Localizable name of this object.
     * Returns localized object's {@link #keyName},
     * or raw value if it is not localizable.
     */
    public ITemplate getName() {
        return I18n.of(keyName);
    }

    /**
     * Localizable description of this object.
     * Purposed to be overridden with custom I18n key.
     * Empty by default.
     */
    public ITemplate getDescription() {
        return I18n.empty;
    }
}

package com.crown.common;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Hash map of named objects.
 * Items can be retrieved either by their UUID, or by key name.
 * This class is usually extended, and then used as a singleton.
 */
public class ObjectsMap<T extends NamedObject> implements Iterable<T> {
    protected final LinkedHashMap<UUID, T> byIdMap = new LinkedHashMap<>();
    protected final LinkedHashMap<String, T> byKeyNameMap = new LinkedHashMap<>();

    /**
     * Adds a new element to the collection.
     */
    public void add(T obj) {
        if (obj == null) return;
        byIdMap.put(obj.getId(), obj);
        byKeyNameMap.put(obj.getKeyName(), obj);
    }

    /**
     * Returns an object from collection by it's UUID.
     */
    public T get(UUID id) {
        return byIdMap.getOrDefault(id, null);
    }

    /**
     * Returns an object from collection by it's key name.
     */
    public T get(String keyName) {
        return byKeyNameMap.getOrDefault(keyName, null);
    }

    /**
     * Removes an object from collection by it's UUID.
     */
    public void remove(UUID id) {
        remove(byIdMap.get(id));
    }

    /**
     * Removes an object from collection by it's key name.
     */
    public void remove(String keyName) {
        remove(byKeyNameMap.get(keyName));
    }

    /**
     * Removes an object from collection.
     */
    public void remove(T obj) {
        if (obj == null) return;
        byIdMap.remove(obj.getId());
        byKeyNameMap.remove(obj.getKeyName());
    }

    public Collection<T> values() {
        return byIdMap.values();
    }

    public int size() {
        return byIdMap.size();
    }

    /**
     * Returns the first element of collection, or null if collection is empty.
     */
    public T first() {
        return byIdMap.values().stream().findFirst().orElse(null);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return this.values().iterator();
    }
}

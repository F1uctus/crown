package com.crown.common;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * Collection of named objects. This class is usually extended,
 * and then populated with predefined items
 * using {@link ObjectCollection#withPredefinedItems()}.
 * Internally, uses object UUID's as hash map keys.
 */
public class ObjectCollection<T extends NamedObject> {
    protected final LinkedHashMap<UUID, T> all = new LinkedHashMap<>();

    protected ObjectCollection<T> withPredefinedItems() {
        return this;
    }

    public void add(T obj) {
        all.put(obj.getId(), obj);
    }

    public T get(UUID id) {
        return all.getOrDefault(id, null);
    }

    public void remove(T obj) {
        all.remove(obj.getId());
    }

    public void remove(UUID id) {
        all.remove(id);
    }

    public Collection<T> values() {
        return all.values();
    }
}

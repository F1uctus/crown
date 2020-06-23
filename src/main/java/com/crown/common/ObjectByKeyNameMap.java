package com.crown.common;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Hash map of named objects. This class is usually extended,
 * and then populated with predefined items
 * using {@link ObjectByIdMap#withPredefinedItems()}.
 * Internally, uses object key names as hash map keys.
 */
public class ObjectByKeyNameMap<T extends NamedObject> {
    protected final LinkedHashMap<String, T> all = new LinkedHashMap<>();

    protected ObjectByKeyNameMap<T> withPredefinedItems() {
        return this;
    }

    public void add(T obj) {
        all.put(obj.getKeyName(), obj);
    }

    public T get(String id) {
        return all.getOrDefault(id, null);
    }

    public void remove(T obj) {
        all.remove(obj.getKeyName());
    }

    public void remove(String id) {
        all.remove(id);
    }

    public Collection<T> values() {
        return all.values();
    }
}

package com.crown.common;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * Hash map of named objects. This class is usually extended,
 * and then populated with predefined items
 * using {@link ObjectsMap#withPredefinedItems()}.
 * Internally, uses object UUIDs as hash map keys.
 */
public class ObjectsMap<T extends NamedObject> implements Iterable<T> {
    protected final LinkedHashMap<UUID, T> byIdMap = new LinkedHashMap<>();
    protected final LinkedHashMap<String, T> byKeyNameMap = new LinkedHashMap<>();

    protected ObjectsMap<T> withPredefinedItems() {
        return this;
    }

    public void add(T obj) {
        byIdMap.put(obj.getId(), obj);
        byKeyNameMap.put(obj.getKeyName(), obj);
    }

    public T get(UUID id) {
        return byIdMap.getOrDefault(id, null);
    }

    public T get(String keyName) {
        return byKeyNameMap.getOrDefault(keyName, null);
    }

    public void remove(UUID id) {
        var obj = byIdMap.get(id);
        byIdMap.remove(obj.getId());
        byKeyNameMap.remove(obj.getKeyName());
    }

    public void remove(String keyName) {
        var obj = byKeyNameMap.get(keyName);
        byIdMap.remove(obj.getId());
        byKeyNameMap.remove(obj.getKeyName());
    }

    public void remove(T obj) {
        byIdMap.remove(obj.getId());
        byKeyNameMap.remove(obj.getKeyName());
    }

    public Collection<T> values() {
        return byIdMap.values();
    }

    public int size() {
        return byIdMap.size();
    }

    public T first() {
        return byIdMap.values().stream().findFirst().orElse(null);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return this.values().iterator();
    }
}

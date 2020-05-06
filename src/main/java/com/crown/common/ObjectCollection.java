package com.crown.common;

import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.Collection;


public class ObjectCollection<T extends NamedObject> {
    protected final LinkedCaseInsensitiveMap<T> all = new LinkedCaseInsensitiveMap<>();

    protected ObjectCollection<T> fromBuiltIn() {
        return this;
    }

    public void add(T obj) {
        all.put(obj.getKeyName(), obj);
    }

    public T get(String name) {
        return all.getOrDefault(name, null);
    }

    public void remove(String name) {
        all.remove(name);
    }

    public Collection<T> values() {
        return all.values();
    }
}

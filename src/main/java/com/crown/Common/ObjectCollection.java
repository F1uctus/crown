package com.crown.common;

import org.springframework.util.LinkedCaseInsensitiveMap;

public class ObjectCollection<T extends NamedObject> {
    public final LinkedCaseInsensitiveMap<T> all = new LinkedCaseInsensitiveMap<>();

    protected ObjectCollection<T> fromBuiltIn() {
        return this;
    }

    public void add(T obj) {
        all.put(obj.getKeyName(), obj);
    }

    public T get(String name) {
        var result = all.values()
                        .stream()
                        .filter(x -> x.getName().equalsIgnoreCase(name))
                        .findFirst();
        return result.orElse(null);
    }
}

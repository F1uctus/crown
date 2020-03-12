package com.cotfk.Common;

import org.springframework.util.LinkedCaseInsensitiveMap;

public class ObjectCollection<T extends NamedObject> {
    public final LinkedCaseInsensitiveMap<T> all = new LinkedCaseInsensitiveMap<>();

    protected ObjectCollection<T> fromBuiltIn() {
        return this;
    }

    public void add(T obj) {
        all.put(obj.getName(), obj);
    }
}

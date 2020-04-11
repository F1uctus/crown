package com.crown;

import com.crown.common.ObjectCollection;
import com.crown.common.Property;
import org.jetbrains.annotations.NonNls;

import static com.crown.interaction.CommandParser.clear;

public class PropertiesCollection extends ObjectCollection<Property> {
    public <T2> T2 get(Class<T2> cls, @NonNls String name) {
        var result = all.get(clear(name));
        if (result == null) {
            return null;
        }
        return (T2) result.getValue();
    }

    public <T2> void change(String name, T2 byValue) {
        var result = all.get(clear(name));
        if (result != null) {
            result.change(byValue);
        }
    }
}

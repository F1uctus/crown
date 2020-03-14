package com.cotfk;

import com.cotfk.Common.ObjectCollection;
import com.cotfk.Common.Property;

import static com.cotfk.Interaction.CommandParser.clear;

public class PropertiesCollection extends ObjectCollection<Property> {
    public <T2> T2 get(Class<T2> cls, String name) {
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

package com.cotfk.Common;

/**
 * Represents an object with name and description.
 */
public class NamedObject {
    final private String name;
    final private String description;

    public NamedObject(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

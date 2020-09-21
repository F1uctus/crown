package com.crown.items;

import com.crown.common.NamedObject;
import org.jetbrains.annotations.NotNull;

public abstract class InventoryItem extends NamedObject {
    public InventoryItem(@NotNull String keyName) {
        super(keyName);
    }
}

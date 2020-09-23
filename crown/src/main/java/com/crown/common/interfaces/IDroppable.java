package com.crown.common.interfaces;

import com.crown.items.InventoryItem;

public interface IDroppable {
    /**
     * Determines objects that are dropped when some condition is satisfied
     * (e.g. object destroyed or was hit by something).
     */
    InventoryItem[] drop();
}

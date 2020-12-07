package com.crown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.crown.common.NamedObject;
import com.crown.common.ObjectsMap;

public class ObjectsMapTest {
    public static class GameObject extends NamedObject {
        public GameObject(String keyName) {
            super(keyName);
        }
    }

    @Test
    public void testAddGet() {
        ObjectsMap<GameObject> map = new ObjectsMap<>();
        GameObject obj1 = new GameObject("object 1");
        GameObject obj2 = new GameObject("object 2");

        map.add(obj1);
        map.add(obj2);

        assertEquals(map.get("object 1"), obj1);
        assertEquals(map.get("object 2"), obj2);

    }

    @Test
    public void testRemoveByKeyName() {
        ObjectsMap<GameObject> map = new ObjectsMap<>();
        GameObject obj1 = new GameObject("object 1");

        map.add(obj1);
        map.remove("object 1");

        assertNull(map.get("object 1"));
    }

    @Test
    public void testRemoveByObject() {
        ObjectsMap<GameObject> map = new ObjectsMap<>();
        GameObject obj1 = new GameObject("object 1");

        map.add(obj1);
        map.remove(obj1);

        assertNull(map.get("object 1"));
    }

    @Test
    public void testFirst() {
        ObjectsMap<GameObject> map = new ObjectsMap<>();
        GameObject obj1 = new GameObject("object 1");

        map.add(obj1);

        assertEquals(map.first(), obj1);
    }
}

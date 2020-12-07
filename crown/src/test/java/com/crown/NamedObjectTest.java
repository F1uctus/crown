package com.crown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.crown.common.NamedObject;

public class NamedObjectTest {
    @Test
    public void testGetKeyName() {
        NamedObject no = new NamedObject("  Some Object  ") { };
        assertEquals(no.getKeyName(), "some object");
    }

    @Test
    public void testSetKeyName() {
        NamedObject no = new NamedObject("  Some Object  ") { };
        no.setKeyName("  Other Object  ");
        assertEquals(no.getKeyName(), "other object");
    }
}

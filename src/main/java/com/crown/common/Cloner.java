package com.crown.common;

import com.crown.time.Timeline;
import com.esotericsoftware.kryo.kryo5.Kryo;

public class Cloner {
    public final Kryo kryo = new Kryo();
    public static final Cloner instance = new Cloner();

    private Cloner() {
        kryo.register(Timeline.class);
    }
}

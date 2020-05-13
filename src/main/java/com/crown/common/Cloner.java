package com.crown.common;

import com.crown.BaseGameState;
import com.crown.time.Action;
import com.crown.time.Timeline;
import com.crown.time.VirtualClock;
import com.esotericsoftware.kryo.kryo5.Kryo;

import java.util.ArrayList;

public class Cloner {
    public final Kryo kryo = new Kryo();
    public static final Cloner instance = new Cloner();

    private Cloner() {
        kryo.register(ArrayList.class);
        kryo.register(Timeline.class);
        kryo.register(VirtualClock.class);
        kryo.register(BaseGameState.class);
        kryo.register(Action.class);
    }
}

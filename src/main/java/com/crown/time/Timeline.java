package com.crown.time;

import com.crown.i18n.ITemplate;

import java.util.ArrayList;

/**
 * A timeline for game events.
 * The "heart" of time travelling implementation.
 */
public class Timeline {
    public static ArrayList<Action> actions = new ArrayList<>();

    public static ITemplate add(Action a) {
        actions.add(a);
        return a.perform();
    }
}

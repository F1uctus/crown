package com.crown.time;

import com.crown.i18n.ITemplate;

import java.util.ArrayList;
import java.util.function.Function;

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

    public static ITemplate addPropertyChange(Function<Integer, ITemplate> changer, int delta) {
        var a = new Action() {
            @Override
            public ITemplate perform() {
                return changer.apply(delta);
            }

            @Override
            public ITemplate rollback() {
                return changer.apply(-delta);
            }
        };
        actions.add(a);
        return a.perform();
    }
}

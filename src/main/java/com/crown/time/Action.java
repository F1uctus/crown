package com.crown.time;

import com.crown.creatures.Creature;
import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import org.jetbrains.annotations.NonNls;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class Action<T extends Creature> {
    public final TimePoint point;
    public T performer;

    public Action(T performer) {
        point = performer.timeline.clock.now();
        this.performer = performer;
    }

    public static Action<Creature> change(Creature c, @NonNls String changerMethodName, int delta) {
        Method m;
        try {
            m = c.getClass().getMethod(changerMethodName, int.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
        return new Action<>(c) {
            @Override
            public ITemplate perform() {
                try {
                    return (ITemplate) m.invoke(performer, delta);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    return I18n.of(e.getMessage());
                }
            }

            @Override
            public ITemplate rollback() {
                try {
                    return (ITemplate) m.invoke(performer, -delta);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    return I18n.of(e.getMessage());
                }
            }
        };
    }

    public abstract ITemplate perform();

    public abstract ITemplate rollback();
}

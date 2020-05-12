package com.crown.time;

import com.crown.creatures.Creature;
import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import org.jetbrains.annotations.NonNls;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
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
        MethodHandle m;
        try {
            m = MethodHandles.lookup().findSpecial(
                Creature.class, changerMethodName,
                MethodType.methodType(ITemplate.class),
                c.getClass()
            ).bindTo(c);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        return new Action<>(c) {
            @Override
            public ITemplate perform() {
                try {
                    return (ITemplate) m.invoke(c);
                } catch (Throwable e) {
                    return I18n.of(e.getMessage());
                }
            }

            @Override
            public ITemplate rollback() {
                try {
                    return (ITemplate) m.invoke(-delta);
                } catch (Throwable e) {
                    return I18n.of(e.getMessage());
                }
            }
        };
    }

    public abstract ITemplate perform();

    public abstract ITemplate rollback();
}

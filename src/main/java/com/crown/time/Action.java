package com.crown.time;

import com.crown.creatures.Creature;
import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.crown.maps.MapObject;
import org.jetbrains.annotations.NonNls;

import java.lang.reflect.Method;

public abstract class Action<T extends MapObject> {
    private T target;

    public Action(T target) {
        this.target = target;
    }

    public abstract ITemplate perform();

    public abstract ITemplate rollback();

    public T getTarget() {
        return target;
    }

    public void setTarget(T value) {
        target = value;
    }

    public static Action<Creature> change(Creature target, @NonNls String changerMethodName, int delta) {
        Method m;
        try {
            m = Creature.class.getDeclaredMethod(changerMethodName, int.class);
            m.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }

        return new Action<>(target) {
            @Override
            public ITemplate perform() {
                try {
                    return (ITemplate) m.invoke(getTarget(), delta);
                } catch (Throwable e) {
                    return I18n.of(e.getMessage());
                }
            }

            @Override
            public ITemplate rollback() {
                try {
                    return (ITemplate) m.invoke(getTarget(), -delta);
                } catch (Throwable e) {
                    return I18n.of(e.getMessage());
                }
            }
        };
    }
}

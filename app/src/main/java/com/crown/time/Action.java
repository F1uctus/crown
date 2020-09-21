package com.crown.time;

import com.crown.creatures.Organism;
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

    public static Action<Organism> change(Organism target, @NonNls String changerMethodName, int delta) {
        Method method;
        Class<?> cls = target.getClass();
        do {
            try {
                method = cls.getDeclaredMethod(changerMethodName, int.class);
                method.setAccessible(true);
            } catch (NoSuchMethodException e1) {
                method = null;
            }
            cls = cls.getSuperclass();
        } while (cls != null && method == null);

        if (cls == null && method == null) {
            // failed to get method
            System.err.println(
                "Failed to build Action.change for method "
                    + target.getClass().getTypeName() + "." + changerMethodName
            );
            System.exit(-1);
        }

        final Method finalMethod = method;
        return new Action<Organism>(target) {
            @Override
            public ITemplate perform() {
                try {
                    return (ITemplate) finalMethod.invoke(getTarget(), delta);
                } catch (Throwable e) {
                    return I18n.of(e.getMessage());
                }
            }

            @Override
            public ITemplate rollback() {
                try {
                    return (ITemplate) finalMethod.invoke(getTarget(), -delta);
                } catch (Throwable e) {
                    return I18n.of(e.getMessage());
                }
            }
        };
    }
}

package com.cotfk.Common;

import javafx.util.Pair;
import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static com.cotfk.Main.rb;

public class Property<T> extends NamedObject {
    protected T value;
    private final ArrayList<Constraint<T>> constraints = new ArrayList<>();
    protected BiFunction<T, T, T> transformer;
    protected Consumer<T> onChange = (v) -> {
    };

    // Required by bean
    protected Property() {
    }


    public Property(
        @NonNls String keyName,
        T initValue
    ) {
        super(keyName);
        this.value = initValue;
    }

    public Property<T> withConstraint(Constraint<T> constraint) {
        constraints.add(constraint);
        return this;
    }

    public Property<T> withTransformer(BiFunction<T, T, T> transformer) {
        this.transformer = transformer;
        return this;
    }

    public Property<T> withTriggerOnChange(Consumer<T> fn) {
        this.onChange = fn;
        return this;
    }

    public T getValue() {
        return value;
    }

    protected Pair<Boolean, T> checkAllConstraints(T newValue) {
        boolean ok = true;
        T constrainedValue = null;
        for (var constr : constraints) {
            var pair = constr.apply(value, newValue);
            ok = ok && pair.getKey();
            constrainedValue = pair.getValue();
        }
        return new Pair(ok, constrainedValue);
    }

    public void change(T byValue) {
        var pair = checkAllConstraints(transformer.apply(value, byValue));
        if (pair.getKey()) {
            var newValue = pair.getValue();
            onChange.accept(newValue);
            value = newValue;
        }
    }

    @Override
    public String getName() {
        return rb.getString("Props." + getKeyName() + ".Name");
    }

    @Override
    public String getDescription() {
        var kn = getKeyName();
        if (rb.containsKey(kn)) {
            return rb.getString("Props." + kn + ".Description");
        } else {
            return null;
        }
    }

    public String getOnChangeMessage() {
        var kn = getKeyName();
        if (rb.containsKey(kn)) {
            return rb.getString("Props." + kn + ".OnChange");
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return getName() + "\t\t= " + value;
    }
}

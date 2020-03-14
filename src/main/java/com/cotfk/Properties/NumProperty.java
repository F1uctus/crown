package com.cotfk.Properties;

import com.cotfk.Common.Property;
import org.jetbrains.annotations.NonNls;

import java.util.function.Consumer;

public class NumProperty extends Property<Double> {
    private Consumer<Double> onReduce = (d) -> {
    };
    private Consumer<Double> onGrow = (d) -> {
    };

    // Required by bean
    private NumProperty() {
    }

    public NumProperty(@NonNls String keyName, Double initValue) {
        super(keyName, initValue);
        this.transformer = Double::sum;
    }

    public NumProperty withTriggerOnReduce(Consumer<Double> fn) {
        this.onReduce = fn;
        return this;
    }

    public NumProperty withTriggerOnGrow(Consumer<Double> fn) {
        this.onGrow = fn;
        return this;
    }

    @Override
    public void change(Double byValue) {
        var pair = checkAllConstraints(transformer.apply(value, byValue));
        if (pair.getKey()) {
            var newValue = pair.getValue();
            onChange.accept(newValue);
            if (value < newValue) {
                onGrow.accept(newValue);
            } else if (value > newValue) {
                onReduce.accept(newValue);
            }
            value = newValue;
        }
    }
}

package com.cotfk.Players;

import com.cotfk.Common.NamedObject;
import com.cotfk.Common.Property;
import com.cotfk.Constraints.Check;
import com.cotfk.Constraints.NumRange;
import com.cotfk.Properties.NumProperty;
import com.cotfk.PropertiesCollection;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.stream.Collectors;

import static com.cotfk.Main.random;
import static com.cotfk.Main.rb;

public class RegularPlayer extends NamedObject {
    @NonNls
    public PropertiesCollection props = new PropertiesCollection();

    // Required by bean
    protected RegularPlayer() {
    }

    public RegularPlayer(String name) {
        super(normalizeName(name));

        props.add(new NumProperty("health", 100d)
            .withConstraint(
                new NumRange(0, 100)
                    .withTriggerOnMinValue((c) -> {
                        // health = 0: death
                        System.out.println(getName() + ": " + rb.getString("Player.Death." + (random.nextInt(10) + 1)));
                    })
            )
        );

        props.add(new NumProperty("energy", 100d)
            .withTriggerOnGrow((newValue) -> {
                // noinspection HardCodedStringLiteral
                System.out.println(getName() + ": z-z-z...");
            })
            .withConstraint(
                new NumRange(0, 100)
                    .withTriggerOnMinValue((c) -> {
                        System.out.println(MessageFormat.format(rb.getString("Player.Tired"), getName()));
                    })
            )
        );

        props.add(new NumProperty("speed", 1d)
            .withConstraint(new NumRange(0, 10))
        );

        props.add(new NumProperty("x", 0d)
            .withConstraint(new NumRange(0, 100))
            .withConstraint(
                new Check<>(
                    (oldValue, newValue) -> props.get(double.class, "energy") >= Math.abs(oldValue - newValue)
                )
            )
            .withTransformer((value, delta) -> value + delta * props.get(double.class, "speed"))
            .withTriggerOnChange((newValue) -> {
                props.change("energy", Math.abs(newValue) * -1d);
            })
        );

        props.add(new NumProperty("y", 0d)
            .withConstraint(new NumRange(0, 100))
            .withConstraint(
                new Check<>(
                    (oldValue, newValue) -> props.get(double.class, "energy") >= Math.abs(oldValue - newValue)
                )
            )
            .withTransformer((value, delta) -> value + delta * props.get(double.class, "speed"))
            .withTriggerOnChange((newValue) -> {
                props.change("energy", Math.abs(newValue) * -1d);
            })
        );

        System.out.println(MessageFormat.format(rb.getString("Player.Created"), getName()));
    }

    public String getName() {
        return getKeyName();
    }

    @Override
    public String getDescription() {
        return "";
    }

    public String getStats() {
        return props.all.values().stream().map(Property::toString).collect(Collectors.joining("\n"));
    }

    @NotNull
    private static String normalizeName(@NotNull String name) {
        String[] words = name.split("\\s");
        StringBuilder result = new StringBuilder();
        for (String w : words) {
            result.append(w.substring(0, 1).toUpperCase()).append(w.substring(1)).append(" ");
        }
        return result.toString().trim();
    }
}

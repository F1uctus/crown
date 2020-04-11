package com.crown.magic;

import com.crown.common.NamedObject;
import com.crown.players.RegularPlayer;
import org.jetbrains.annotations.NonNls;

import java.util.function.Consumer;

import static com.crown.Main.rb;

public class Spell extends NamedObject {
    private Consumer<RegularPlayer> effect;
    private double energyCost;
    private double learnEnergyCost;

    // Required by bean
    private Spell() {
    }

    public Spell(
        @NonNls String keyName,
        Consumer<RegularPlayer> effect,
        double energyCost,
        double learnEnergyCost
    ) {
        super(keyName);
        this.effect = effect;
        this.energyCost = energyCost;
        this.learnEnergyCost = learnEnergyCost;
    }

    public void apply(RegularPlayer target) {
        new Thread(() -> {
            effect.accept(target);
        }).start();
    }

    public String getName() {
        return rb.getString("Spell." + getKeyName() + ".Name");
    }

    public String getDescription() {
        return rb.getString("Spell." + getKeyName() + ".Description");
    }

    public double getEnergyCost() {
        return energyCost;
    }

    public double getLearnEnergyCost() {
        return learnEnergyCost;
    }

    @Override
    public String toString() {
        var descLines = getDescription().split("\n");
        StringBuilder alignedDescription;
        if (descLines.length > 1) {
            alignedDescription = new StringBuilder(descLines[0]);
            for (int i = 1; i < descLines.length; i++) {
                // length calculation
                alignedDescription.append("\n").append(" ".repeat(21)).append(descLines[i]);
            }
        } else {
            alignedDescription = new StringBuilder(getDescription());
        }
        return String.format("%-20s %-10s", getName(), alignedDescription);
    }
}
package com.cotfk.Magic;

import com.cotfk.Common.NamedObject;
import com.cotfk.Player;

import java.util.function.Consumer;

public class Spell extends NamedObject {
    private final Consumer<Player> effect;
    private final int energyCost;
    private final int learnEnergyCost;

    public Spell(String name, String description, Consumer<Player> effect) {
        this(name, description, effect, 5);
    }

    public Spell(String name, String description, Consumer<Player> effect, int energyCost) {
        this(name, description, effect, energyCost, 10);
    }

    public Spell(String name, String description, Consumer<Player> effect, int energyCost, int learnEnergyCost) {
        super(name, description);
        this.effect = effect;
        this.energyCost = energyCost;
        this.learnEnergyCost = learnEnergyCost;
    }

    public void apply(Player target) {
        new Thread(() -> {
            effect.accept(target);
        }).start();
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public int getLearnEnergyCost() {
        return learnEnergyCost;
    }
}
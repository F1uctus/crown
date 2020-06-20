package com.crown.skills;

import com.crown.common.NamedObject;
import com.crown.maps.MapObject;

import java.util.function.Consumer;

public abstract class Skill extends NamedObject {
    private final Consumer<MapObject> effect;
    private final int energyCost;
    private final int learnEnergyCost;
    private final int affectedRange;

    public Skill(
        String keyName,
        Consumer<MapObject> effect,
        int energyCost,
        int learnEnergyCost,
        int affectedRange
    ) {
        super(keyName);
        this.effect = effect;
        this.energyCost = energyCost;
        this.learnEnergyCost = learnEnergyCost;
        this.affectedRange = affectedRange;
    }

    public void apply(MapObject target) {
        new Thread(() -> effect.accept(target)).start();
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public int getLearnEnergyCost() {
        return learnEnergyCost;
    }

    public int getAffectedRange() {
        return affectedRange;
    }
}

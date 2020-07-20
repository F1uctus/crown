package com.crown.skills;

import com.crown.common.NamedObject;
import com.crown.maps.MapObject;
import com.crown.time.Action;

public abstract class Skill<T extends MapObject> extends NamedObject {
    private final Class<T> targetClass;
    private final Action<T> effect;
    private final int energyCost;
    private final int learnEnergyCost;
    private final int affectedRange;

    public Skill(
        Class<T> targetClass,
        String keyName,
        Action<T> effect,
        int energyCost,
        int learnEnergyCost,
        int affectedRange
    ) {
        super(keyName);
        this.targetClass = targetClass;
        this.effect = effect;
        this.energyCost = energyCost;
        this.learnEnergyCost = learnEnergyCost;
        this.affectedRange = affectedRange;
    }

    public void perform(T target) {
        effect.setTarget(target);
        new Thread(effect::perform).start();
    }

    public void rollback(T target) {
        effect.setTarget(target);
        new Thread(effect::rollback).start();
    }

    public Class<T> getTargetClass() {
        return targetClass;
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

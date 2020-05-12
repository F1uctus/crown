package com.crown.creatures;

import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.crown.items.InventoryItem;
import com.crown.maps.*;
import com.crown.time.Action;
import com.crown.time.Timeline;

import java.util.ArrayList;
import java.util.List;

public abstract class Creature extends MapObject {
    protected int maxHp;
    protected int hp;

    protected final int maxEnergy;
    protected int energy;

    protected final int maxFov = 100;
    protected int fov;

    protected final int maxSpeed = 10;
    protected int speed;

    protected int level;
    protected int xp = 0;
    protected int skillPoints = 0;

    public Timeline timeline;
    private final List<InventoryItem> inventory = new ArrayList<>();

    public Creature(
        String name,
        Map map,
        MapIcon<?> mapIcon,
        MapWeight mapWeight,
        Point3D position,
        int maxEnergy,
        int maxHp,
        int level,
        int fieldOfView,
        int speed
    ) {
        super(name, map, mapIcon, mapWeight, position);

        this.maxHp = hp = maxHp;
        this.maxEnergy = energy = maxEnergy;
        this.fov = fieldOfView;
        this.speed = speed;
        this.level = level;
    }

    /**
     * Returns creature's statistics
     * (all it's properties & their values).
     * Maybe, some additional data.
     */
    public abstract ITemplate getStats();

    // region HP

    /**
     * Maximal allowed health points
     * of creature at current level.
     */
    public int getMaxHp() {
        return maxHp;
    }

    /**
     * Returns creature's health points.
     */
    public int getHp() {
        return hp;
    }

    /**
     * Changes creature health points by {@code delta}.
     * Timeline support included.
     */
    public ITemplate changeHpBy(int delta) {
        return timeline.perform(Action.change(this, "changeHp", delta));
    }

    /**
     * Internal logic, may be overridden if needed.
     */
    protected ITemplate changeHp(int delta) {
        if (invalidDelta(hp, delta, maxHp)) {
            return invalidDeltaMessage;
        }
        hp += delta;
        return I18n.okMessage;
    }

    // endregion

    // region Energy

    /**
     * Maximal allowed energy points of creature.
     */
    public int getMaxEnergy() {
        return maxEnergy;
    }

    /**
     * Returns creature's energy points.
     */
    public int getEnergy() {
        return energy;
    }


    /**
     * Fulfills creature's energy points.
     */
    public ITemplate sleep() {
        return changeEnergyBy(maxEnergy - energy);
    }

    /**
     * Changes creature speed by {@code delta}.
     * Timeline support included.
     */
    public ITemplate changeEnergyBy(int delta) {
        return timeline.perform(Action.change(this, "changeEnergy", delta));
    }

    /**
     * Internal logic, may be overridden if needed.
     */
    protected ITemplate changeEnergy(int delta) {
        if (invalidDelta(energy, delta, maxEnergy)) {
            return invalidDeltaMessage;
        }
        energy += delta;
        return I18n.okMessage;
    }

    // endregion

    // region Speed

    /**
     * Maximal allowed speed of creature.
     */
    public int getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Returns creature's speed.
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Changes creature speed by {@code delta}.
     * Timeline support included.
     */
    public ITemplate changeSpeedBy(int delta) {
        return timeline.perform(Action.change(this, "changeSpeed", delta));
    }

    /**
     * Internal logic, may be overridden if needed.
     */
    protected ITemplate changeSpeed(int delta) {
        if (invalidDelta(speed, delta, maxSpeed)) {
            return invalidDeltaMessage;
        }
        speed += delta;
        return I18n.okMessage;
    }

    // endregion

    // region FOV

    /**
     * Maximal allowed field of vision for creature.
     */
    public int getMaxFov() {
        return maxFov;
    }

    /**
     * Returns creature's field of vision.
     */
    public int getFov() {
        return fov;
    }

    /**
     * Changes creature's field of vision by {@code delta}.
     * Timeline support included.
     */
    public ITemplate changeFovBy(int delta) {
        return timeline.perform(Action.change(this, "changeFov", delta));
    }

    /**
     * Internal logic, may be overridden if needed.
     */
    protected ITemplate changeFov(int delta) {
        if (invalidDelta(fov, delta, maxFov)) {
            return invalidDeltaMessage;
        }
        fov += delta;
        return I18n.okMessage;
    }

    // endregion

    // region XP

    /**
     * Returns creature's experience points.
     */
    public int getXp() {
        return xp;
    }

    /**
     * Changes creature experience points by {@code delta}.
     * Timeline support included.
     */
    public ITemplate changeXpBy(int delta) {
        return timeline.perform(Action.change(this, "changeXp", delta));
    }

    /**
     * Internal logic, may be overridden if needed.
     */
    protected ITemplate changeXp(int delta) {
        if (invalidDelta(xp, delta)) {
            return invalidDeltaMessage;
        }
        xp += delta;
        if (delta > 0) {
            while (xp > getXpForLevel(level)) {
                changeLevel(+1);
            }
        } else {
            while (xp <= getXpForLevel(level)) {
                changeLevel(-1);
            }
        }
        return I18n.okMessage;
    }

    // endregion

    // region Level

    /**
     * Returns creature's level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Changes creature level by {@code delta}.
     * Gives 1 skill point for each level.
     * Timeline support included.
     */
    public ITemplate changeLevelBy(int delta) {
        return timeline.perform(Action.change(this, "changeLevel", delta));
    }

    /**
     * Internal logic, may be overridden if needed.
     */
    protected ITemplate changeLevel(int delta) {
        if (invalidDelta(level, delta)) {
            return invalidDeltaMessage;
        }
        level += delta;
        changeSkillPointsBy((int) Math.signum(delta));
        return I18n.okMessage;
    }

    // endregion

    // region Skill points

    /**
     * Returns creature's skill points.
     */
    public int getSkillPoints() {
        return skillPoints;
    }

    /**
     * Changes creature skill points by {@code delta}.
     * Timeline support included.
     */
    public ITemplate changeSkillPointsBy(int delta) {
        return timeline.perform(Action.change(this, "changeSkillPoints", delta));
    }

    /**
     * Internal logic, may be overridden if needed.
     */
    protected ITemplate changeSkillPoints(int delta) {
        if (invalidDelta(skillPoints, delta)) {
            return invalidDeltaMessage;
        }
        skillPoints += delta;
        return I18n.okMessage;
    }

    // endregion

    // region Movement

    /**
     * Changes creature 2D position by coordinates delta.
     * Timeline support included.
     */
    public ITemplate moveBy(int deltaX, int deltaY) {
        return move(deltaX, deltaY, 0);
    }

    /**
     * Changes creature 3D position by coordinates delta.
     * Timeline support included.
     */
    public ITemplate moveBy(int deltaX, int deltaY, int deltaZ) {
        return timeline.perform(new Action<>(this) {
            @Override
            public ITemplate perform() {
                return performer.move(deltaX, deltaY, deltaZ);
            }

            @Override
            public ITemplate rollback() {
                return performer.move(-deltaX, -deltaY, -deltaZ);
            }
        });
    }

    /**
     * Internal logic to move character to specified target point.
     * May be overridden if needed.
     */
    protected ITemplate move(int deltaX, int deltaY, int deltaZ) {
        var tgtPos = getPt0().plus(new Point3D(deltaX, deltaY, deltaZ));
        var tgtObj = map.get(tgtPos);
        if (map.contains(tgtPos)
            && (tgtObj == null || tgtObj.getMapWeight() != MapWeight.OBSTACLE)) {
            var delta = (int) getPt0().getDistance(tgtPos);
            if (getEnergy() < delta) {
                return I18n.of("stats.energy.low");
            } else {
                moveView(deltaX, deltaY, deltaZ);
                changeEnergyBy(-delta);
                return I18n.okMessage;
            }
        } else {
            return I18n.of("fail.move.obstacle");
        }
    }

    // endregion

    /**
     * Returns creature's inventory items.
     */
    public List<InventoryItem> getInventory() {
        return inventory;
    }

    // utilities

    protected int getXpForLevel(int lvl) {
        int lvl10 = lvl / 10;
        int lvlRem = lvl % 10;
        return 500 * (lvl10 * lvl10 + lvl10) + (lvlRem * (100 * lvl10 + 100));
    }

    protected final ITemplate invalidDeltaMessage = I18n.of("fail.delta.outOfBounds");

    protected static boolean invalidDelta(int val, int delta) {
        return delta == 0
               || delta < 0 && val + delta < 0;
    }

    protected static boolean invalidDelta(int val, int delta, int max) {
        return delta == 0
               || delta < 0 && val + delta < 0
               || delta > 0 && val + delta > max;
    }
}

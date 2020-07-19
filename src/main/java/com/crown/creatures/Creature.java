package com.crown.creatures;

import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.crown.items.InventoryItem;
import com.crown.maps.*;
import com.crown.time.Action;
import com.crown.time.Timeline;

import java.util.ArrayList;

/**
 * Main class for every in-game creature.
 * Methods with {@code By}-suffix are used for creature
 * to make some action, while methods with same name,
 * but without {@code By}-suffix contain internal (non-timeline)
 * logic that can be overridden by successors, and used inside
 * timeline-logic implementation.
 */
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

    private Timeline timeline;
    private final ArrayList<InventoryItem> inventory = new ArrayList<>();

    /**
     * Adds a new creature to the main timeline.
     */
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
        timeline = Timeline.main;
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
    public ITemplate changeHp(int delta) {
        if (invalidDelta(hp, delta, maxHp)) {
            return I18n.invalidDeltaMessage;
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
    public ITemplate changeEnergy(int delta) {
        if (invalidDelta(energy, delta, maxEnergy)) {
            return I18n.invalidDeltaMessage;
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
    public ITemplate changeSpeed(int delta) {
        if (invalidDelta(speed, delta, maxSpeed)) {
            return I18n.invalidDeltaMessage;
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
    public ITemplate changeFov(int delta) {
        if (invalidDelta(fov, delta, maxFov)) {
            return I18n.invalidDeltaMessage;
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
    public ITemplate changeXp(int delta) {
        if (invalidDelta(xp, delta)) {
            return I18n.invalidDeltaMessage;
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
    public ITemplate changeLevel(int delta) {
        if (invalidDelta(level, delta)) {
            return I18n.invalidDeltaMessage;
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
    public ITemplate changeSkillPoints(int delta) {
        if (invalidDelta(skillPoints, delta)) {
            return I18n.invalidDeltaMessage;
        }
        skillPoints += delta;
        return I18n.okMessage;
    }

    // endregion

    // region Movement

    /**
     * Moves creature by specified delta-point.
     * Reduces creature's energy.
     * Timeline support included.
     */
    public ITemplate moveBy(int deltaX, int deltaY) {
        return moveBy(deltaX, deltaY, 0);
    }

    /**
     * Moves creature by specified delta-point.
     * Reduces creature's energy.
     * Timeline support included.
     */
    public ITemplate moveBy(int deltaX, int deltaY, int deltaZ) {
        return timeline.perform(new Action<>(this) {
            @Override
            public ITemplate perform() {
                var result = getTarget().move(deltaX, deltaY, deltaZ);
                if (result == I18n.okMessage) {
                    // SIDE-EFFECT: decrease energy if player moved
                    changeEnergy(-(int) Math.sqrt(
                        Math.pow(deltaX, 2) +
                            Math.pow(deltaY, 2) +
                            Math.pow(deltaZ, 2)
                    ));
                }
                return result;
            }

            @Override
            public ITemplate rollback() {
                var result = getTarget().move(-deltaX, -deltaY, -deltaZ);
                if (result == I18n.okMessage) {
                    // SIDE-EFFECT: increase energy if player moved
                    changeEnergy((int) Math.sqrt(
                        Math.pow(deltaX, 2) +
                            Math.pow(deltaY, 2) +
                            Math.pow(deltaZ, 2)
                    ));
                }
                return result;
            }
        });
    }

    /**
     * Changes creature 3D position by delta point.
     * Creature's energy remains UNCHANGED.
     * May be overridden if needed.
     */
    public ITemplate move(int deltaX, int deltaY, int deltaZ) {
        var tgtPos = getPt0().plus(new Point3D(deltaX, deltaY, deltaZ));
        var tgtObj = getMap().get(tgtPos);
        if (getMap().contains(tgtPos)
            && (tgtObj == null || tgtObj.getMapWeight() != MapWeight.OBSTACLE)) {
            var delta = (int) getPt0().getDistance(tgtPos);
            if (getEnergy() < delta) {
                return I18n.of("stats.energy.low");
            } else {
                moveView(deltaX, deltaY, deltaZ);
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
    public ArrayList<InventoryItem> getInventory() {
        return inventory;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline value) {
        timeline = value;
    }

    // utilities

    /**
     * Returns an absolute experience required to achieve some level.
     * Original implementation is picked from a Steam users level system.
     */
    protected int getXpForLevel(int lvl) {
        int lvl10 = lvl / 10;
        int lvlRem = lvl % 10;
        return 500 * (lvl10 * lvl10 + lvl10) + (lvlRem * (100 * lvl10 + 100));
    }

    /**
     * Utility function used to check if creature property changed by delta
     * is always greater or equal to zero.
     */
    protected static boolean invalidDelta(int val, int delta) {
        return delta == 0
            || delta < 0 && val + delta < 0;
    }

    /**
     * Utility function used to check if creature property changed by delta
     * is always greater or equal to zero and less than given max value.
     */
    protected static boolean invalidDelta(int val, int delta, int max) {
        return delta == 0
            || delta < 0 && val + delta < 0
            || delta > 0 && val + delta > max;
    }
}

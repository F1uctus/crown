package com.crown.creatures;

import com.crown.i18n.ITemplate;
import com.crown.items.InventoryItem;
import com.crown.maps.*;

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
    protected int maxXp = 10;
    protected int skillPoints = 0;

    private final List<InventoryItem> inventory = new ArrayList<>();

    public Creature(
        String name,
        Map map,
        IMapIcon<?> mapIcon,
        MapWeight mapWeight,
        Point3D position
    ) {
        // RULE: default creature parameters
        this(
            name,
            map,
            mapIcon,
            mapWeight,
            position,
            200,
            100,
            1,
            5,
            1
        );
    }

    public Creature(
        String name,
        Map map,
        IMapIcon<?> mapIcon,
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
        this.maxEnergy = maxEnergy;
        // RULE: every creature appears with half energy
        this.energy = maxEnergy / 2;
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
     */
    public abstract ITemplate changeHp(int delta);

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
        return sleep(maxEnergy - energy);
    }

    /**
     * Fulfills creature's energy points by {@code delta}.
     */
    public abstract ITemplate sleep(int delta);

    public abstract ITemplate changeEnergy(int delta);

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
     */
    public abstract ITemplate changeSpeed(int delta);

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
     */
    public abstract ITemplate changeFov(int delta);

    // endregion

    // region XP

    /**
     * Maximal allowed experience points
     * of creature at current level.
     */
    public int getMaxXp() {
        return maxXp;
    }

    /**
     * Returns creature's experience points.
     */
    public int getXp() {
        return xp;
    }

    /**
     * Changes creature experience points by {@code delta}.
     */
    public abstract ITemplate changeXp(int delta);

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
     */
    public abstract ITemplate changeLevel();

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
     */
    public abstract ITemplate changeSkillPoints();

    // endregion

    /**
     * Returns creature's inventory items.
     */
    public List<InventoryItem> getInventory() {
        return inventory;
    }

    /**
     * Moves creature to specified location of map.
     * Doesn't change energy or anything else.
     */
    public void teleport(Point3D newPt) {
        lastPt = pt;
        pt = newPt;
        map.move(this);
    }

    /**
     * Moves character to specified target point.
     */
    public abstract ITemplate move(Point3D targetPt);
}

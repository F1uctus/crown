package com.crown.creatures;

import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.crown.items.InventoryItem;
import com.crown.maps.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Creature extends MapObject {
    private int maxHp;
    private int hp;

    private final int maxEnergy;
    private int energy;

    private final int maxFov = 100;
    private int fov;

    private final int maxSpeed = 10;
    private int speed;

    private int level;
    private int xp = 0;
    private int xpToNextLevel = 10;
    private int skillPoints = 0;

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

    public ITemplate getStats() {
        return I18n.fmtOf(
            String.join(
                "\n",
                "{0}: {1}/{2}",
                "{3}: {4}/{5}",
                "{6}: {7}/{8}",
                "{8}: {9}",
                "{10}: {11}",
                "{12}: {13}",
                "{14}: {15}",
                "{16}: {17}"
            ),
            "stats.hp", getHp(), getMaxHp(),
            "stats.energy", getEnergy(), getMaxEnergy(),
            "stats.speed", getSpeed(),
            "stats.fov", getFov(),
            "stats.xp", getXp(),
            "stats.level", getLevel(),
            "stats.skillPoints", getSkillPoints(),
            "stats.xp.toNextLevel", getXpToNextLevel()
        );
    }

    // region HP

    public int getMaxHp() {
        return maxHp;
    }

    public int getHp() {
        return hp;
    }

    public ITemplate adjustHp(int delta) {
        assert delta > 0;
        if (skillPoints > 0) {
            if (hp + delta > maxHp) {
                hp = maxHp;
                return I18n.of("stats.hp.max");
            }
            skillPoints -= 1;
            return changeHp(delta);
        } else {
            return I18n.of("stats.xp.notEnough");
        }
    }

    private ITemplate changeHp(int delta) {
        maxHp += delta;
        return I18n.changeableOf("stats.hp.{0}", delta);
    }

    // endregion

    // region Energy

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public int getEnergy() {
        return energy;
    }

    public ITemplate sleep() {
        return sleep(maxEnergy - energy);
    }

    public ITemplate sleep(int delta) {
        assert delta > 0 && energy + delta <= maxEnergy;
        if (energy <= 50) {
            return changeEnergy(delta);
        } else {
            return I18n.of("stats.energy.highEnough");
        }
    }

    public ITemplate changeEnergy(int delta) {
        energy += delta;
        return I18n.changeableOf("stats.energy.{0}", delta);
    }

    // endregion

    // region Speed

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public int getSpeed() {
        return speed;
    }

    public ITemplate changeSpeed(int delta) {
        speed += delta;
        return I18n.changeableOf("stats.speed.{0}", delta);
    }

    // endregion

    // region FOV

    public int getMaxFov() {
        return maxFov;
    }

    public int getFov() {
        return fov;
    }

    public ITemplate adjustFov(int delta) {
        assert delta > 0;
        if (skillPoints > 0) {
            if (fov + delta > maxFov) {
                fov = maxFov;
                return I18n.of("stats.fov.max");
            }
            skillPoints -= 1;
            return changeFov(delta);
        } else {
            return I18n.of("stats.xp.notEnough");
        }
    }

    private ITemplate changeFov(int delta) {
        fov += delta;
        return I18n.changeableOf("stats.fov.{0}", delta);
    }

    // endregion

    // region XP

    public int getXp() {
        return xp;
    }

    public int getXpToNextLevel() {
        return xpToNextLevel;
    }

    public ITemplate adjustXp() {
        return changeXp(1);
    }

    private ITemplate changeXp(int delta) {
        assert delta > 0;
        xp += delta;
        if (xp >= xpToNextLevel) {
            adjustLevel();
        }
        return I18n.fmtOf("stats.xp.increased", delta);
    }

    // endregion

    // region Level

    public int getLevel() {
        return level;
    }

    public ITemplate adjustLevel() {
        level += 1;
        skillPoints += 1;
        xp = 0;
        xpToNextLevel = xpToNextLevel + (int) ((float) xpToNextLevel / 100 * 30);
        return I18n.of("stats.level.next");
    }

    // endregion

    public int getSkillPoints() {
        return skillPoints;
    }

    public List<InventoryItem> getInventory() {
        return inventory;
    }

    public void teleport(Point3D newPt) {
        lastPt = pt;
        pt = newPt;
        map.move(this);
    }

    public ITemplate move(Point3D targetPos) {
        // TODO insert path finder
        if (getEnergy() >= 1) {
            teleport(targetPos);
            changeEnergy(-1);
            adjustXp();
            return I18n.of("Success!");
        } else {
            return I18n.of("stats.energy.low");
        }
    }
}

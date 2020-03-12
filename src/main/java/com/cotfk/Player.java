package com.cotfk;

import com.cotfk.Common.NamedObject;
import com.google.common.primitives.Doubles;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

import static com.cotfk.Main.random;
import static com.cotfk.Main.rb;

public class Player extends NamedObject {
    private double maxHealth = 100;
    protected double health = maxHealth;

    private double maxEnergy = 50;
    protected double energy = maxEnergy;

    protected int posX;
    protected int posY;

    public Player(String name) {
        super(normalizeName(name), "");
        System.out.println(MessageFormat.format(rb.getString("Player.Created"), getName()));
    }

    public double getHealth() {
        return health;
    }

    public double getEnergy() {
        return energy;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public String getStats() {
        return String.join(
            "\n",
            MessageFormat.format(rb.getString("Stats.Position"), posX, posY),
            MessageFormat.format(rb.getString("Stats.Energy"), energy)
        );
    }

    @NotNull
    private static String normalizeName(String name) {
        String[] words = name.split("\\s");
        StringBuilder result = new StringBuilder();
        for (String w : words) {
            result.append(w.substring(0, 1).toUpperCase()).append(w.substring(1)).append(" ");
        }
        return result.toString().trim();
    }

    public void moveX(int deltaX) {
        if (energy < deltaX) {
            System.out.println(MessageFormat.format(rb.getString("Player.MoveTooLong"), getName()));
            return;
        }
        posX += deltaX;
        energy -= Math.abs(deltaX);
        System.out.println(getStats());
    }

    public void moveY(int deltaY) {
        if (energy < deltaY) {
            System.out.println(MessageFormat.format(rb.getString("Player.MoveTooLong"), getName()));
            return;
        }
        posY += deltaY;
        energy -= Math.abs(deltaY);
        System.out.println(getStats());
    }

    public void sleep(int time) {
        // noinspection HardCodedStringLiteral
        System.out.println(getName() + ": z-z-z...");
        energy = Math.min(energy + time, maxEnergy);
    }

    public void die() {
        energy = 0;
        // notice: this is for undead.
        maxEnergy *= 3;
        System.out.println(getName() + ": " + rb.getString("Player.Death." + (random.nextInt(10) + 1)));
    }

    public void changeHealth(int byValue) {
        health = Doubles.constrainToRange(health + byValue, 0, maxHealth);
    }

    public void changeEnergy(int byValue) {
        energy = Doubles.constrainToRange(energy + byValue, 0, maxEnergy);
    }
}

package com.cotfk.Magic;

import com.cotfk.Common.ObjectCollection;
import com.cotfk.Player;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

import static com.cotfk.Main.rb;

public class Wizard extends Player {
    public ObjectCollection<Spell> knownSpells = new ObjectCollection<>();

    public Wizard(String name) {
        super(name);
    }

    public void cast(@NotNull Spell spell, Player target) {
        if (!knownSpells.all.containsKey(spell.getName())) {
            System.out.println(rb.getString("Wizard.DontKnowSpell"));
            return;
        }
        if (energy < spell.getEnergyCost()) {
            System.out.println(rb.getString("Wizard.SpellLowEnergy"));
            return;
        }
        spell.apply(target);
        energy -= spell.getEnergyCost();
        System.out.println(MessageFormat.format(rb.getString("Wizard.SpellCasted"), getName(), target.getName(), spell.getName()));
    }

    public void learn(Spell spell) {
        if (energy < spell.getLearnEnergyCost()) {
            System.out.println(rb.getString("Wizard.LearnSpellLowEnergy"));
            return;
        }
        energy -= spell.getLearnEnergyCost();
        knownSpells.add(spell);
        System.out.println(MessageFormat.format(rb.getString("Wizard.SpellLearned"), getName(), spell.getName()));
    }
}
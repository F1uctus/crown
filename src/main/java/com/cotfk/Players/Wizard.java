package com.cotfk.Players;

import com.cotfk.Common.ObjectCollection;
import com.cotfk.Magic.Spell;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

import static com.cotfk.Main.rb;

public class Wizard extends RegularPlayer {
    public ObjectCollection<Spell> knownSpells = new ObjectCollection<>();

    // Required by bean
    private Wizard() {
    }

    public Wizard(String name) {
        super(name);
    }

    public void cast(@NotNull Spell spell, RegularPlayer target) {
        if (!knownSpells.all.containsKey(spell.getName())) {
            System.out.println(rb.getString("Wizard.DontKnowSpell"));
            return;
        }
        if (props.get(double.class, "energy") < spell.getEnergyCost()) {
            System.out.println(rb.getString("Wizard.SpellLowEnergy"));
            return;
        }
        spell.apply(target);
        props.change("energy", Math.abs(spell.getEnergyCost()) * -1);
        System.out.println(MessageFormat.format(rb.getString("Wizard.SpellCasted"), getName(), target.getName(), spell.getName()));
    }

    public void learn(Spell spell) {
        if (props.get(double.class, "energy") < spell.getLearnEnergyCost()) {
            System.out.println(rb.getString("Wizard.LearnSpellLowEnergy"));
            return;
        }
        props.change("energy", Math.abs(spell.getLearnEnergyCost()) * -1);
        knownSpells.add(spell);
        System.out.println(MessageFormat.format(rb.getString("Wizard.SpellLearned"), getName(), spell.getName()));
    }
}
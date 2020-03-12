package com.cotfk.Interaction;

import com.cotfk.Common.ObjectCollection;
import com.cotfk.Magic.Wizard;

import java.text.MessageFormat;

import static com.cotfk.Main.*;

/**
 * Contains all built-in game commands.
 */
public class Commands extends ObjectCollection<Command> {
    public Commands fromBuiltIn() {
        add(new Command(
            "all",
            (args) -> {
                System.out.println(gameState.playersAsTable());
            }
        ));
        add(new Command(
            "new",
            (args) -> {
                gameState.addPlayer(args[0], args[1]);
            },
            "type",
            "name"
        ));
        add(new Command(
            "select",
            (args) -> {
                gameState.selectPlayer(args[0]);
            },
            "name"
        ));
        add(new Command(
            "main",
            (args) -> {
                gameState.unselectPlayer();
            }
        ));

        // region ACTIONS FOR SELECTED PLAYER
        add(new Command(
            "kill",
            (args) -> {
                if (gameState.getCurrentPlayer() == null) {
                    System.out.println(rb.getString("Commands.PlayerNotSelected"));
                    return;
                }
                gameState.removePlayer();
            }
        ));
        add(new Command(
            "stats",
            (args) -> {
                if (gameState.getCurrentPlayer() == null) {
                    System.out.println(rb.getString("Commands.PlayerNotSelected"));
                    return;
                }
                System.out.println(gameState.getCurrentPlayer().getStats());
            }
        ));
        add(new Command(
            "move",
            (args) -> {
                if (gameState.getCurrentPlayer() == null) {
                    System.out.println(rb.getString("Commands.PlayerNotSelected"));
                    return;
                }
                gameState.getCurrentPlayer().moveX(Integer.parseInt(args[0]));
                gameState.getCurrentPlayer().moveY(Integer.parseInt(args[1]));
            },
            "x",
            "y"
        ));
        add(new Command(
            "sleep",
            (args) -> {
                if (gameState.getCurrentPlayer() == null) {
                    System.out.println(rb.getString("Commands.PlayerNotSelected"));
                    return;
                }
                gameState.getCurrentPlayer().sleep(Integer.parseInt(args[0]));
            },
            "time"
        ));
        // endregion

        // region ACTIONS FOR SELECTED WIZARD
        add(new Command(
            "cast",
            (args) -> {
                if (gameState.getCurrentPlayer() == null) {
                    System.out.println(rb.getString("Commands.PlayerNotSelected"));
                    return;
                }
                if (!(gameState.getCurrentPlayer() instanceof Wizard)) {
                    System.out.println(rb.getString("Commands.Cast.NotAWizard"));
                    return;
                }
                var spell = gameState.spells.all.get(args[0]);
                if (spell == null) {
                    System.out.println(rb.getString("Commands.Cast.UnknownSpell"));
                    return;
                }
                var target = gameState.players.all.get(args[1]);
                if (target == null) {
                    System.out.println(rb.getString("Commands.PlayerDoesntExist"));
                    return;
                }
                ((Wizard) gameState.getCurrentPlayer()).cast(spell, target);
            },
            "spell",
            "player"
        ));
        add(new Command(
            "learn",
            (args) -> {
                if (gameState.getCurrentPlayer() == null) {
                    System.out.println(rb.getString("Commands.PlayerNotSelected"));
                    return;
                }
                if (!(gameState.getCurrentPlayer() instanceof Wizard)) {
                    System.out.println(rb.getString("Commands.Cast.NotAWizard"));
                    return;
                }
                var spell = gameState.spells.all.get(args[0]);
                if (spell == null) {
                    System.out.println(rb.getString("Commands.Cast.UnknownSpell"));
                    return;
                }
                ((Wizard) gameState.getCurrentPlayer()).learn(spell);
            },
            "spell"
        ));
        // endregion

        add(new Command(
            "lang",
            (args) -> {
                if (!bundles.containsKey(args[0])) {
                    System.out.println(rb.getString("Lang.InvalidLanguage"));
                    System.out.println(MessageFormat.format(rb.getString("Lang.AvailableLangs"), String.join(",", bundles.keySet())));
                } else {
                    setLang(args[0]);
                }
            },
            "name"
        ));

        add(new Command(
            "help",
            (args) -> {
                if (args[0].equals("a")) {
                    for (Command cmd : all.values()) {
                        System.out.println(cmd.toAlignedString());
                    }
                } else {
                    var cmd = all.get(args[0]);
                    if (cmd != null) {
                        System.out.println(cmd);
                    }
                }
            },
            "subject"
        ));
        return this;
    }
}

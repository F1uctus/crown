package com.cotfk;

import com.cotfk.Common.ObjectCollection;
import com.cotfk.Magic.Spell;
import com.cotfk.Magic.Wizard;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

import static com.cotfk.Main.gameState;
import static com.cotfk.Main.rb;

/**
 * Contains game state for current running session.
 * Try not to access this through `Player` - based classes, you programmer!
 */
public class GameState {
    public final ObjectCollection<Spell> spells = new ObjectCollection<>();
    public final ObjectCollection<Player> players = new ObjectCollection<>();

    private Player currentPlayer;

    public GameState() {
        initBuiltInSpells();
    }

    private void initBuiltInSpells() {
        spells.add(new Spell(
            rb.getString("Spell.Fatigue.Name"),
            rb.getString("Spell.Fatigue.Description"),
            (target) -> {
                for (int i = 0; i < 10; i++) {
                    target.changeEnergy(-10);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }, 15, 10
        ));
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void removePlayer() {
        gameState.players.all.remove(currentPlayer.getName());
        currentPlayer.die();
        unselectPlayer();
    }

    public void unselectPlayer() {
        currentPlayer = null;
    }

    public void selectPlayer(String name) {
        var target = players.all.get(name);
        if (target == null) {
            System.out.println(rb.getString("Commands.PlayerDoesntExist"));
            return;
        }
        currentPlayer = target;
    }

    public void addPlayer(@NotNull @NonNls String type, String name) {
        Player newPlayer;
        switch (type) {
        case "w":
        case "wizard":
            newPlayer = new Wizard(name);
            break;
        case "p":
        case "player":
            newPlayer = new Player(name);
            break;
        default:
            System.out.println(rb.getString("Player.InvalidType"));
            return;
        }
        players.add(newPlayer);
        currentPlayer = newPlayer;
    }

    public String playersAsTable() {
        return players.all.values().stream().map(Player::getName).collect(Collectors.joining("\n"));
    }
}

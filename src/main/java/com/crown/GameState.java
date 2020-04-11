package com.crown;

import com.crown.common.ObjectCollection;
import com.crown.magic.Spell;
import com.crown.players.RegularPlayer;
import com.crown.players.Wizard;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

import static com.crown.Main.gameState;
import static com.crown.Main.rb;

/**
 * Contains game state for current running session.
 * Try not to access this through `Player` - based classes, you programmer!
 */
public class GameState {
    public final ObjectCollection<Spell> spells = new ObjectCollection<>();
    public final ObjectCollection<RegularPlayer> players = new ObjectCollection<>();

    private RegularPlayer currentPlayer;

    public GameState() {
        initBuiltInSpells();
    }

    private void initBuiltInSpells() {
        spells.add(new Spell("Fatigue", (target) -> {
            for (int i = 0; i < 5; i++) {
                target.props.change("energy", -10d);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        }, 15, 10));
        spells.add(new Spell("Snare", (target) -> {
            target.props.change("speed", -1d);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
            }
            target.props.change("speed", 1d);
        }, 15, 10));
    }

    public RegularPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public void removePlayer() {
        gameState.players.all.remove(currentPlayer.getName());
        currentPlayer.props.change("health", Double.MIN_VALUE);
        unselectPlayer();
    }

    public void unselectPlayer() {
        currentPlayer = null;
    }

    public void selectPlayer(String name) {
        var target = players.all.get(name);
        if (target == null) {
            System.out.println(rb.getString("command.playerDoesntExist"));
            return;
        }
        currentPlayer = target;
    }

    public void addPlayer(@NotNull @NonNls String type, String name) {
        RegularPlayer newPlayer;
        switch (type) {
        case "w":
        case "wizard":
            newPlayer = new Wizard(name);
            break;
        case "p":
        case "player":
            newPlayer = new RegularPlayer(name);
            break;
        default:
            System.out.println(rb.getString("player.invalidType"));
            return;
        }
        players.add(newPlayer);
        currentPlayer = newPlayer;
    }

    public String playersAsTable() {
        return players.all.values()
                          .stream()
                          .map(RegularPlayer::getName)
                          .collect(Collectors.joining("\n"));
    }
}

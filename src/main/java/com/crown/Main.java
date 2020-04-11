package com.crown;

import com.crown.interaction.CommandParser;
import com.esotericsoftware.yamlbeans.YamlWriter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    @NonNls
    public static ResourceBundle rb;
    public static final HashMap<String, ResourceBundle> bundles = new HashMap<>();
    public static final Random random = new Random();
    public static GameState gameState;
    private static final Scanner s = new Scanner(System.in);

    @SuppressWarnings("HardCodedStringLiteral")
    public static void main(@NotNull String[] args) throws IOException {
        bundles.put("ru", ResourceBundle.getBundle("gameMessages", new Locale("ru_RU")));
        bundles.put("en", ResourceBundle.getBundle("gameMessages", new Locale("en_US")));

        rb = bundles.get(Locale.getDefault().getLanguage());
        if (rb == null) {
            rb = bundles.get("en");
        }

        System.out.println(rb.getString("welcome"));
        System.out.println(rb.getString("help"));

        gameState = new GameState();
        var parser = new CommandParser();
        while (true) {
            printInputPrefix();
            String input = readFromConsole();
            if (input.equals("")) {
                continue;
            }
            if (input.equals("exit") || input.equals("quit")) {
                break;
            }
            parser.parse(input);
        }

        YamlWriter writer = new YamlWriter(new FileWriter("last-game-state.yml"));
        writer.write(gameState);
        writer.close();
    }

    public static String readFromConsole() {
        String input;
        if (s.hasNextLine()) {
            input = s.nextLine();
        } else {
            input = "ERROR";
        }
        return input.strip();
    }

    public static void printInputPrefix() {
        String inputPrefix;
        if (gameState.getCurrentPlayer() != null) {
            inputPrefix = gameState.getCurrentPlayer().getName();
        } else {
            inputPrefix = "cotfk";
        }
        System.out.print(inputPrefix + "> ");
    }

    public static void setLang(String shortLangName) {
        shortLangName = CommandParser.clear(shortLangName);
        var newRb = bundles.get(shortLangName);
        if (newRb != null) {
            rb = newRb;
        }
    }
}

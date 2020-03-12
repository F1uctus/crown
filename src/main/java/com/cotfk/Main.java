package com.cotfk;

import com.cotfk.Interaction.CommandParser;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Main {
    @NonNls
    public static ResourceBundle rb;
    public static final HashMap<String, ResourceBundle> bundles = new HashMap<>();
    public static final Scanner scanner = new Scanner(System.in);
    public static final Random random = new Random();
    public static GameState gameState;

    @SuppressWarnings("HardCodedStringLiteral")
    public static void main(@NotNull String[] args) {
        bundles.put("ru", ResourceBundle.getBundle("gameMessages", new Locale("ru_RU")));
        bundles.put("en", ResourceBundle.getBundle("gameMessages", new Locale("en_US")));

        rb = bundles.get(Locale.getDefault().getLanguage());
        if (rb == null) {
            rb = bundles.get("en");
        }

        System.out.println(rb.getString("Welcome"));
        System.out.println(rb.getString("TypeHelp"));

        gameState = new GameState();
        var parser = new CommandParser();
        parser.beginInteraction();
        scanner.close();
    }

    public static void setLang(String shortLangName) {
        shortLangName = CommandParser.clear(shortLangName);
        var newRb = bundles.get(shortLangName);
        if (newRb != null) {
            rb = newRb;
        }
    }
}

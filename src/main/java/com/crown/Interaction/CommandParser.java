package com.crown.Interaction;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;

import static com.crown.Main.*;

public class CommandParser {
    static final Commands commands = new Commands().fromBuiltIn();

    public void printInputPrefix() {
        String inputPrefix;
        if (gameState.getCurrentPlayer() != null) {
            inputPrefix = gameState.getCurrentPlayer().getName();
        } else {
            inputPrefix = "cotfk";
        }
        System.out.print(inputPrefix + "> ");
    }

    public static String clear(String input) {
        return input.trim().toLowerCase();
    }

    public void beginInteraction() {
        while (true) {
            printInputPrefix();
            String input = clear(scanner.nextLine());
            if (input.equals("")) {
                System.out.print("\033[1A");
                System.out.print("\033[2K");
                continue;
            }
            if (input.equals("exit") || input.equals("quit")) {
                break;
            }
            parse(input);
        }
    }

    public void parse(final String input) {
        if (input == null || input.isBlank()) {
            return;
        }
        final String[] inputParts = clear(input).replaceAll("\\s+", " ").split(" ");

        // validating input
        if (inputParts.length == 0) {
            System.out.println(rb.getString("InvalidCommandName"));
            return;
        }

        var cmd = commands.all.get(inputParts[0]);
        if (cmd == null) {
            System.out.println(rb.getString("Commands.UnknownCommand"));
            System.out.println(rb.getString("TypeHelp"));
            return;
        }

        int expectedArgs = cmd.getFormalParameters().length;
        if (inputParts.length - 1 > expectedArgs) {
            // too much of args provided
            System.out.println(MessageFormat.format(rb.getString("Commands.ArgsExpected"), expectedArgs));
            System.out.println(
                MessageFormat.format(rb.getString("TypeHelp.AboutCommand"), inputParts[0])
            );
            return;
        }

        var parameters = new ArrayList<>(Arrays.asList(inputParts)).subList(1, inputParts.length);
        if (parameters.size() < expectedArgs) {
            // less than required args provided, request more
            System.out.println(rb.getString("Commands.TooFewArguments"));
            while (parameters.size() < expectedArgs) {
                System.out.print(cmd.getFormalParameters()[parameters.size()] + "> ");
                var param = clear(scanner.nextLine());
                if (param.equals("!x")) {
                    return;
                }
                parameters.add(param);
            }
        }

        cmd.execute(parameters.toArray(new String[parameters.size()]));
    }
}

package com.crown.interaction;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static com.crown.Main.rb;

public class CommandParser {
    static final Commands commands = new Commands().fromBuiltIn();

    public static String clear(String input) {
        return input.trim().toLowerCase();
    }

    public void parse(@NotNull String input) {
        input = clear(input);
        if (input.isBlank()) {
            return;
        }
        final String[] inputParts = clear(input).replaceAll("\\s+", " ").split(" ");

        // validating input
        if (inputParts.length == 0) {
            System.out.println(rb.getString("command.unknown"));
            return;
        }

        var cmd = commands.all.get(inputParts[0]);
        if (cmd == null) {
            System.out.println(rb.getString("command.unknown"));
            System.out.println(rb.getString("help"));
            return;
        }

        int expectedArgs = cmd.getFormalParameters().length;
        if (inputParts.length - 1 > expectedArgs) {
            // too much of args provided
            System.out.println(MessageFormat.format(
                rb.getString("command.argsExpected"),
                expectedArgs
            ));
            System.out.println(MessageFormat.format(
                rb.getString("help.aboutCommand"),
                inputParts[0]
            ));
            return;
        }

        var parameters = new ArrayList<>(Arrays.asList(inputParts)).subList(1, inputParts.length);
        if (parameters.size() < expectedArgs) {
            // less than required args provided, request more
            System.out.println(rb.getString("command.tooFewArguments"));
            while (parameters.size() < expectedArgs) {
                System.out.print(cmd.getFormalParameters()[parameters.size()] + "> ");
                var scanner = new Scanner(System.in);
                var param = clear(scanner.nextLine());
                scanner.close();
                if (param.equals("!x")) {
                    return;
                }
                parameters.add(param);
            }
        }

        cmd.execute(parameters.toArray(new String[0]));
    }
}

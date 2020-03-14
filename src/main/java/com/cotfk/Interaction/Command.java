package com.cotfk.Interaction;

import com.cotfk.Common.NamedObject;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.cotfk.Main.rb;

class Command extends NamedObject {
    private final Consumer<String[]> action;
    private final String[] formalParameters;

    public Command(@NonNls String keyName, Consumer<String[]> action, @NonNls String... formalParameters) {
        super(keyName);
        this.action = action;
        this.formalParameters = formalParameters;
    }

    public String getName() {
        return getKeyName();
    }

    @Override
    public String getDescription() {
        return rb.getString("Command.Description." + getName());
    }

    public String[] getFormalParameters() {
        return formalParameters;
    }

    public void execute(@NotNull String... actualParameters) {
        if (actualParameters.length != formalParameters.length) {
            return;
        }
        action.accept(actualParameters);
    }

    @Override
    public String toString() {
        String params = "";
        if (formalParameters.length > 0) {
            params = (" <" + String.join("> <", formalParameters) + ">");
        }
        var descLines = getDescription().split("\n");
        StringBuilder alignedDescription;
        if (descLines.length > 1) {
            alignedDescription = new StringBuilder(descLines[0]);
            for (int i = 1; i < descLines.length; i++) {
                // length calculation
                alignedDescription.append("\n").append(" ".repeat(32)).append(descLines[i]);
            }
        } else {
            alignedDescription = new StringBuilder(getDescription());
        }
        return String.format("%-10s %-20s %-10s", getName(), params, alignedDescription);
    }
}
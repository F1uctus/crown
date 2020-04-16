package com.crown.i18n;

import java.text.MessageFormat;

class FormatTemplate implements ITemplate {
    private final String message;
    private final Object[] parts;

    public FormatTemplate(String message, Object... format) {
        this.message = message;
        this.parts = format;
    }

    public String getLocalized(String langName) {
        var result = new StringBuilder();
        String[] fArgs = new String[parts.length];
        for (int i = 0, partsLength = parts.length; i < partsLength; i++) {
            Object part = parts[i];
            if (part instanceof String) {
                // try to get message from resource
                var s = (String) part;
                if (I18n.has(langName, s)) {
                    fArgs[i] = I18n.get(langName, s);
                    continue;
                }
            } else if (part instanceof Template) {
                // recursive template localization
                fArgs[i] = ((Template) part).getLocalized(langName);
                continue;
            }
            // if nothing succeeded, leave arg as-is
            fArgs[i] = part.toString();
        }
        // noinspection RedundantCast
        result.append(MessageFormat.format(message, (Object[]) fArgs));
        return result.toString();
    }

    @Override
    public String toString() {
        return getLocalized();
    }
}

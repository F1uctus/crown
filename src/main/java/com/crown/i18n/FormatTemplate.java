package com.crown.i18n;

import java.text.MessageFormat;

class FormatTemplate implements ITemplate {
    private final String message;
    private final Object[] formatArgs;

    public FormatTemplate(String message, Object... formatArgs) {
        this.message = message;
        this.formatArgs = formatArgs;
    }

    public String getLocalized(String langName) {
        var result = new StringBuilder();
        String[] fArgs = new String[formatArgs.length];
        for (int i = 0; i < formatArgs.length; i++) {
            Object part = formatArgs[i];
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
        String pattern = I18n.has(langName, message) ? I18n.get(langName, message) : message;
        // noinspection RedundantCast
        result.append(MessageFormat.format(pattern, (Object[]) fArgs));
        return result.toString();
    }

    @Override
    public String toString() {
        return getLocalized();
    }
}

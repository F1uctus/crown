package com.crown.i18n;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Game messages internationalization utilities.
 */
public class I18n {
    /**
     * Maps short language name (ru/en/etc.) to corresponding {@link ResourceBundle}.
     */
    protected static HashMap<String, ResourceBundle> bundles = new HashMap<>();

    /**
     * Initializes internationalization functions for game.
     * NOTE: Invoke this function only once on game init.
     */
    public static void init(HashMap<String, ResourceBundle> bundles) {
        I18n.bundles = bundles;
    }

    /**
     * Checks if {@link ResourceBundle} with given {@code lang}
     * contains specified {@code resourceKey}.
     */
    protected static boolean has(String lang, String resourceKey) {
        return bundles.get(lang).containsKey(resourceKey);
    }

    /**
     * Returns specified {@code key} from
     * {@link ResourceBundle} with given {@code lang}.
     */
    protected static String get(String lang, String resourceKey) {
        return bundles.get(lang).getString(resourceKey);
    }

    /**
     * Returns a internationalization {@link Template}
     * from given {@code messageParts}.
     */
    public static ITemplate of(Object... messageParts) {
        return new Template(messageParts);
    }

    /**
     * Returns a internationalization {@link FormatTemplate}
     * from message with given {@code resourceKey}
     * formatted with specified {@code format} objects.
     */
    public static ITemplate fmtOf(String resourceKey, Object... format) {
        return new FormatTemplate(resourceKey, format);
    }

    /**
     * Returns a internationalization {@link FormatTemplate}
     * from message with given {@code resourceKey} formatted with
     * {@code "increased"} or {@code "decreased"} string
     * based on sign of given {@code delta}.
     * <p>
     * Example:
     * <pre>
     *     changeableOf("stats.xp.{0}", -1")
     * </pre>
     * returns template with resource key {@code "stats.xp.decreased"}.
     */
    public static ITemplate changeableOf(String resourceKey, int delta) {
        String key;
        if (delta > 0) {
            key = MessageFormat.format(
                resourceKey,
                "increased"
            );
        } else {
            key = MessageFormat.format(
                resourceKey,
                "decreased"
            );
        }
        return I18n.fmtOf(key, delta);
    }

    public static Set<String> getLanguages() {
        return bundles.keySet();
    }
}

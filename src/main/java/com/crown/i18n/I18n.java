package com.crown.i18n;

import org.jetbrains.annotations.NonNls;

import java.text.MessageFormat;
import java.util.*;

/**
 * Game messages internationalization utilities.
 */
public class I18n {
    /**
     * Maps short language name (ru/en/etc.) to corresponding {@link ResourceBundle}.
     */
    protected static HashMap<String, ResourceBundle> bundles = new HashMap<>();

    @NonNls
    public static final ITemplate okMessage = I18n.of("message.ok");

    @NonNls
    public static final ITemplate invalidDeltaMessage = I18n.of("fail.delta.outOfBounds");

    /**
     * Initializes internationalization resources.
     * NOTE: Invoke this function only once on app init.
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
    public static ITemplate of(@NonNls Object... messageParts) {
        return new Template(messageParts) {
            @Override
            public boolean isRaw() {
                return true;
            }
        };
    }

    /**
     * Returns a non-localizable "raw" template.
     */
    public static ITemplate raw(@NonNls Object... messageParts) {
        return new Template(messageParts) {
            @Override
            public boolean isRaw() {
                return true;
            }
        };
    }

    /**
     * Returns a internationalization {@link FormatTemplate}
     * from message with given {@code resourceKey}
     * formatted with specified {@code format} objects.
     * Example of usage:
     * <pre>
     *     fmtOf("{0} picked up a {1}", player.getName(), item.getName())
     * </pre>
     */
    public static ITemplate fmtOf(String message, Object... formatArgs) {
        return new FormatTemplate(message, formatArgs);
    }

    /**
     * Returns a internationalization {@link FormatTemplate}
     * from message with given {@code resourceKey} formatted with
     * {@code "increased"} or {@code "decreased"} string
     * based on sign of given {@code delta}.
     * <p>
     * Example of usage:
     * <pre>
     *     changeableOf("stats.xp.{0}", -1)
     * </pre>
     * returns template with resource key {@code "stats.xp.decreased"}.
     */
    public static ITemplate changeableOf(String resourceKey, int delta) {
        String key;
        if (delta > 0) {
            // noinspection HardCodedStringLiteral
            key = MessageFormat.format(
                resourceKey,
                "increased"
            );
        } else {
            // noinspection HardCodedStringLiteral
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

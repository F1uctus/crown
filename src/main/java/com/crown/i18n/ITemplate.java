package com.crown.i18n;

public interface ITemplate {
    default String getLocalized() {
        // noinspection HardCodedStringLiteral
        return getLocalized("en");
    }

    /**
     * Indicates if this template is non-localizable.
     */
    default boolean isRaw() {
        return false;
    }

    String getLocalized(String langName);
}

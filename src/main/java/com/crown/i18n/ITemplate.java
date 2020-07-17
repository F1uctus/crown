package com.crown.i18n;

public interface ITemplate {
    default String getLocalized() {
        // noinspection HardCodedStringLiteral
        return getLocalized("en");
    }

    default boolean isRaw() {
        return false;
    }

    String getLocalized(String langName);
}

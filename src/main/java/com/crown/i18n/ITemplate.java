package com.crown.i18n;

public interface ITemplate {
    default String getLocalized() {
        return getLocalized("en");
    }

    String getLocalized(String langName);
}

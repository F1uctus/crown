package com.crown.i18n;

public interface ITemplate {
    String getLocalized();
    /**
     * Indicates if this template is non-localizable.
     */
    boolean isRaw();

    String getLocalized(String langName);
}

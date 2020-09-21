package com.crown.i18n;

class Template implements ITemplate {
    private final Object[] parts;

    public Template(Object... parts) {
        this.parts = parts;
    }

    public String getLocalized() {
        return getLocalized("en");
    }

    public boolean isRaw() {
        return false;
    }

    public String getLocalized(String langName) {
        StringBuilder result = new StringBuilder();
        for (Object part : parts) {
            if (part instanceof String) {
                String s = (String) part;
                if (I18n.has(langName, s)) {
                    result.append(I18n.get(langName, s));
                    continue;
                }
            } else if (part instanceof Template) {
                result.append(((Template) part).getLocalized(langName));
                continue;
            }
            result.append(part);
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return getLocalized();
    }
}

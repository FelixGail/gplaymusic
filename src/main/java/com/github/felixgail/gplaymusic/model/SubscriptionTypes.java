package com.github.felixgail.gplaymusic.model;

public enum SubscriptionTypes {
    SUBSCRIBED("aa"),
    FREE("fr");

    private final String text;

    /**
     * @param text
     */
    SubscriptionTypes(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}

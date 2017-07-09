package com.github.felixgail.gplaymusic.model;

public enum SubscriptionType {
    SUBSCRIBED("aa"),
    FREE("fr");

    private final String text;

    /**
     * @param text
     */
    SubscriptionType(final String text) {
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

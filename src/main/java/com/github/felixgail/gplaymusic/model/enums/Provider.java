package com.github.felixgail.gplaymusic.model.enums;

public enum Provider {
    STREAM("mplay"),
    STATION("wplay"),
    PODCAST("fplay");

    private String value;

    Provider(String v) {
        this.value = v;
    }

    @Override
    public String toString() {
        return this.value;
    }
}

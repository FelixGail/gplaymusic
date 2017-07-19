package com.github.felixgail.gplaymusic.model;

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

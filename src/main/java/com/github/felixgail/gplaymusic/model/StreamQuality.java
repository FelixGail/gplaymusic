package com.github.felixgail.gplaymusic.model;

public enum StreamQuality {
    LOW("lo"),
    MEDIUM("med"),
    HIGH("hi");

    private String rep;

    StreamQuality(String q) {
        this.rep = q;
    }

    @Override
    public String toString() {
        return this.rep;
    }
}

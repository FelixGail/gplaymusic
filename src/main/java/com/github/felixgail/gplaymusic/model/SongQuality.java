package com.github.felixgail.gplaymusic.model;

public enum SongQuality {
    LOW("lo"),
    MEDIUM("med"),
    HIGH("hi");

    private String rep;

    SongQuality(String q) {
        this.rep = q;
    }

    @Override
    public String toString() {
        return this.rep;
    }
}

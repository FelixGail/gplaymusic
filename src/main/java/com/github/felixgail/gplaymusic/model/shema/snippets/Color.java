package com.github.felixgail.gplaymusic.model.shema.snippets;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Color implements Serializable {

    @Expose
    private int red;
    @Expose
    private int blue;
    @Expose
    private int green;

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }
}

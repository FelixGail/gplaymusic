package com.github.felixgail.gplaymusic.model.shema.snippets;

import com.google.gson.annotations.Expose;

import java.awt.Color;
import java.io.Serializable;

public class ColorStyles implements Serializable {

    @Expose
    private Color primary;
    @Expose
    private Color scrim;
    @Expose
    private Color accent;

    public Color getPrimary() {
        return primary;
    }

    public Color getScrim() {
        return scrim;
    }

    public Color getAccent() {
        return accent;
    }
}

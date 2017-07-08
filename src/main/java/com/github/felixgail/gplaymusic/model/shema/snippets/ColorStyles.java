package com.github.felixgail.gplaymusic.model.shema.snippets;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class ColorStyles implements Serializable {

    @Expose
    private Color primary;
    @Expose
    private String scrim;
    @Expose
    private String accent;

    public Color getPrimary() {
        return primary;
    }

    public void setPrimary(Color primary) {
        this.primary = primary;
    }

    public String getScrim() {
        return scrim;
    }

    public void setScrim(String scrim) {
        this.scrim = scrim;
    }

    public String getAccent() {
        return accent;
    }

    public void setAccent(String accent) {
        this.accent = accent;
    }
}

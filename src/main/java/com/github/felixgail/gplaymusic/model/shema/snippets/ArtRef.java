package com.github.felixgail.gplaymusic.model.shema.snippets;

import com.google.gson.annotations.Expose;

public class ArtRef {
    @Expose
    private String url;
    @Expose
    private String aspectRatio;
    @Expose
    private boolean autogen;
    @Expose
    private ColorStyles colorStyles;

    public ColorStyles getColorStyles() {
        return colorStyles;
    }

    public void setColorStyles(ColorStyles colorStyles) {
        this.colorStyles = colorStyles;
    }

    public String getUrl() {
        return url;
    }

    public String getAspectRatio() {
        return aspectRatio;
    }

    public boolean isAutogen() {
        return autogen;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAspectRatio(String aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public void setAutogen(boolean autogen) {
        this.autogen = autogen;
    }
}

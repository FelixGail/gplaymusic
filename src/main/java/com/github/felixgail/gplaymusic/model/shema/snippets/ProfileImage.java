package com.github.felixgail.gplaymusic.model.shema.snippets;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class ProfileImage implements Serializable {
    @Expose
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

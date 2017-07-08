package com.github.felixgail.gplaymusic.model.shema;

import com.github.felixgail.gplaymusic.model.shema.snippets.Thumbnail;
import com.github.felixgail.gplaymusic.model.search.ResultType;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class Video implements Result, Serializable {
    public final static ResultType RESULT_TYPE = ResultType.VIDEO;

    @Expose
    private String id;
    @Expose
    private String title;
    @Expose
    private List<Thumbnail> thumbnails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Thumbnail> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(List<Thumbnail> thumbnails) {
        this.thumbnails = thumbnails;
    }
}

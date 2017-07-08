package com.github.felixgail.gplaymusic.model.search.results.snippets;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class Genre implements Serializable {

    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private List<String> children;
    @Expose
    private String parentId;
    @Expose
    private List<ArtRef> images;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<ArtRef> getImages() {
        return images;
    }

    public void setImages(List<ArtRef> images) {
        this.images = images;
    }
}

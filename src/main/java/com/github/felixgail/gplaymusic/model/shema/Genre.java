package com.github.felixgail.gplaymusic.model.shema;

import com.github.felixgail.gplaymusic.model.shema.snippets.ArtRef;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

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

    public String getName() {
        return name;
    }

    //TODO: return lo genres
    public Optional<List<String>> getChildren() {
        return Optional.ofNullable(children);
    }

    //TODO: return genre
    public Optional<String> getParentId() {
        return Optional.ofNullable(parentId);
    }

    public Optional<List<ArtRef>> getImages() {
        return Optional.ofNullable(images);
    }

}

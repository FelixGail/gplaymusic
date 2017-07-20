package com.github.felixgail.gplaymusic.model.shema;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class PodcastGenre implements Serializable {

    @Expose
    private String id;
    @Expose
    private String displayName;
    @Expose
    private List<PodcastGenre> subgroups = new LinkedList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<PodcastGenre> getSubgroups() {
        return subgroups;
    }

    public void setSubgroups(List<PodcastGenre> subgroups) {
        this.subgroups = subgroups;
    }
}

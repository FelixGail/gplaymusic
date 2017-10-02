package com.github.felixgail.gplaymusic.model.shema;

import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.interfaces.Result;
import com.github.felixgail.gplaymusic.model.shema.snippets.Thumbnail;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

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

    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    public Optional<List<Thumbnail>> getThumbnails() {
        return Optional.ofNullable(thumbnails);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Video) && ((Video) o).getId().equals(this.id);
    }

    @Override
    public ResultType getResultType() {
        return RESULT_TYPE;
    }
}

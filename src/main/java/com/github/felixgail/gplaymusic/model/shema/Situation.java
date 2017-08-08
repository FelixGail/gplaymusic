package com.github.felixgail.gplaymusic.model.shema;

import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.interfaces.Result;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class Situation implements Result, Serializable {
    public final static ResultType RESULT_TYPE = ResultType.SITUATION;

    @Expose
    private String description;
    @Expose
    private String id;
    @Expose
    private String imageUrl;
    @Expose
    private String title;
    @Expose
    private String wideImageUrl;
    @Expose
    private List<Station> stations;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWideImageUrl() {
        return wideImageUrl;
    }

    public void setWideImageUrl(String wideImageUrl) {
        this.wideImageUrl = wideImageUrl;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Situation) && ((Situation) o).getId().equals(this.id);
    }

    @Override
    public ResultType getResultType() {
        return RESULT_TYPE;
    }
}

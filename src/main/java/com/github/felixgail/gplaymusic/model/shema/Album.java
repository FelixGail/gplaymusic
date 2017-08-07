package com.github.felixgail.gplaymusic.model.shema;

import com.github.felixgail.gplaymusic.model.interfaces.Result;
import com.github.felixgail.gplaymusic.model.search.ResultType;
import com.github.felixgail.gplaymusic.model.shema.snippets.Attribution;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Album implements Result, Serializable {
    public final static ResultType RESULT_TYPE = ResultType.ALBUM;

    @Expose
    private String name;
    @Expose
    private String albumArtist;
    @Expose
    @SerializedName("albumArtRef")
    private String albumArtUrl;
    @Expose
    private String albumId;
    @Expose
    private String artist;
    @Expose
    private List<String> artistId;
    @Expose
    private int year;
    @Expose
    private String explicitType;
    @Expose
    @SerializedName("description_attribution")
    private Attribution descriptionAttribution;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public String getAlbumArtUrl() {
        return albumArtUrl;
    }

    public void setAlbumArtUrl(String albumArtUrl) {
        this.albumArtUrl = albumArtUrl;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public List<String> getArtistId() {
        return artistId;
    }

    public void setArtistId(List<String> artistId) {
        this.artistId = artistId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getExplicitType() {
        return explicitType;
    }

    public void setExplicitType(String explicitType) {
        this.explicitType = explicitType;
    }

    public Attribution getDescriptionAttribution() {
        return descriptionAttribution;
    }

    public void setDescriptionAttribution(Attribution descriptionAttribution) {
        this.descriptionAttribution = descriptionAttribution;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Album) && ((Album) o).getAlbumId().equals(this.albumId);
    }

    @Override
    public ResultType getResultType() {
        return RESULT_TYPE;
    }
}

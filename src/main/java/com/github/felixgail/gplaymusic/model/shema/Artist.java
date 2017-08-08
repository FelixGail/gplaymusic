package com.github.felixgail.gplaymusic.model.shema;

import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.interfaces.Result;
import com.github.felixgail.gplaymusic.model.shema.snippets.ArtRef;
import com.github.felixgail.gplaymusic.model.shema.snippets.Attribution;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Artist implements Result, Serializable {
    public final static ResultType RESULT_TYPE = ResultType.ARTIST;

    @Expose
    private String name;
    @Expose
    @SerializedName("artistArtRef")
    private String artistArtUrl;
    @Expose
    private List<ArtRef> artistArtRefs;
    @Expose
    private String artistId;
    @Expose
    @SerializedName("artist_bio_attribution")
    private Attribution artistBioAttribution;
    @Expose
    @SerializedName("related_artists")
    private List<Artist> relatedArtists;
    @Expose
    @SerializedName("total_albums")
    private int totalAlbums;
    @Expose
    private List<Track> topTracks;

    public List<Artist> getRelatedArtists() {
        return relatedArtists;
    }

    public void setRelatedArtists(List<Artist> relatedArtists) {
        this.relatedArtists = relatedArtists;
    }

    public int getTotalAlbums() {
        return totalAlbums;
    }

    public void setTotalAlbums(int totalAlbums) {
        this.totalAlbums = totalAlbums;
    }

    public List<Track> getTopTracks() {
        return topTracks;
    }

    public void setTopTracks(List<Track> topTracks) {
        this.topTracks = topTracks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtistArtUrl() {
        return artistArtUrl;
    }

    public void setArtistArtUrl(String artistArtUrl) {
        this.artistArtUrl = artistArtUrl;
    }

    public List<ArtRef> getArtistArtRefs() {
        return artistArtRefs;
    }

    public void setArtistArtRefs(List<ArtRef> artistArtRefs) {
        this.artistArtRefs = artistArtRefs;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public Attribution getArtistBioAttribution() {
        return artistBioAttribution;
    }

    public void setArtistBioAttribution(Attribution artistBioAttribution) {
        this.artistBioAttribution = artistBioAttribution;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Artist) && ((Artist) o).getArtistId().equals(this.artistId);
    }

    @Override
    public ResultType getResultType() {
        return RESULT_TYPE;
    }
}

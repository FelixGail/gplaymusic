package com.github.felixgail.gplaymusic.model.search.results.snippets;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class StationSeed implements Serializable{

    @Expose
    private String seedType;
    @Expose
    private String albumId;
    @Expose
    private String artistId;
    @Expose
    private String genreId;
    @Expose
    private String trackId;
    @Expose
    private String trackLockerId;
    @Expose
    private String curatedStationId;
    @Expose
    private MetadataSeed metadataSeed;

    public String getSeedType() {
        return seedType;
    }

    public void setSeedType(String seedType) {
        this.seedType = seedType;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getTrackLockerId() {
        return trackLockerId;
    }

    public void setTrackLockerId(String trackLockerId) {
        this.trackLockerId = trackLockerId;
    }

    public String getCuratedStationId() {
        return curatedStationId;
    }

    public void setCuratedStationId(String curatedStationId) {
        this.curatedStationId = curatedStationId;
    }

    public MetadataSeed getMetadataSeed() {
        return metadataSeed;
    }

    public void setMetadataSeed(MetadataSeed metadataSeed) {
        this.metadataSeed = metadataSeed;
    }
}

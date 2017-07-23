package com.github.felixgail.gplaymusic.model.shema;

import com.github.felixgail.gplaymusic.model.search.ResultType;
import com.github.felixgail.gplaymusic.model.shema.snippets.ArtRef;
import com.github.felixgail.gplaymusic.model.shema.snippets.StationSeed;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Station implements Result, Serializable {
    public final static ResultType RESULT_TYPE = ResultType.STATION;

    @Expose
    private String name;
    @Expose
    private String imageUrl;
    @Expose
    private boolean deleted;
    @Expose
    private String lastModifiedTimestamp;
    @Expose
    private String recentTimestamp;
    @Expose
    private String clientId;
    @Expose
    private String sessionToken;
    @Expose
    private StationSeed seed;
    @Expose
    private List<StationSeed> stationSeeds;
    @Expose
    private String id;
    @Expose
    private String description;
    @Expose
    private List<Track> tracks = new LinkedList<>();
    @Expose
    @SerializedName("imageUrls")
    private List<ArtRef> imageArtRefs;
    @Expose
    private List<ArtRef> compositeArtRefs;
    @Expose
    private List<String> contentTypes;
    @Expose
    private String byline;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public void setLastModifiedTimestamp(String lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    public String getRecentTimestamp() {
        return recentTimestamp;
    }

    public void setRecentTimestamp(String recentTimestamp) {
        this.recentTimestamp = recentTimestamp;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public StationSeed getSeed() {
        return seed;
    }

    public void setSeed(StationSeed seed) {
        this.seed = seed;
    }

    public List<StationSeed> getStationSeeds() {
        return stationSeeds;
    }

    public void setStationSeeds(List<StationSeed> stationSeeds) {
        this.stationSeeds = stationSeeds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public List<ArtRef> getImageArtRefs() {
        return imageArtRefs;
    }

    public void setImageArtRefs(List<ArtRef> imageArtRefs) {
        this.imageArtRefs = imageArtRefs;
    }

    public List<ArtRef> getCompositeArtRefs() {
        return compositeArtRefs;
    }

    public void setCompositeArtRefs(List<ArtRef> compositeArtRefs) {
        this.compositeArtRefs = compositeArtRefs;
    }

    public List<String> getContentTypes() {
        return contentTypes;
    }

    public void setContentTypes(List<String> contentTypes) {
        this.contentTypes = contentTypes;
    }

    public String getByline() {
        return byline;
    }

    public void setByline(String byline) {
        this.byline = byline;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    @Override
    public ResultType getResultType() {
        return RESULT_TYPE;
    }
}

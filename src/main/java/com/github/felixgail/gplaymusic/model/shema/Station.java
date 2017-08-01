package com.github.felixgail.gplaymusic.model.shema;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.requestbodies.mutations.MutationFactory;
import com.github.felixgail.gplaymusic.model.requestbodies.mutations.Mutator;
import com.github.felixgail.gplaymusic.model.search.ResultType;
import com.github.felixgail.gplaymusic.model.shema.snippets.ArtRef;
import com.github.felixgail.gplaymusic.model.shema.snippets.StationSeed;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Station implements Result, Serializable {
    public final static ResultType RESULT_TYPE = ResultType.STATION;
    public final static String BATCH_URL = "radio/editstation";

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

    Station(String name, StationSeed seed, List<Track> tracks) {
        this.name = name;
        this.seed = seed;
        this.tracks = tracks;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public String getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public String getRecentTimestamp() {
        return recentTimestamp;
    }

    public String getClientId() {
        return clientId;
    }

    public StationSeed getSeed() {
        return seed;
    }

    public List<StationSeed> getStationSeeds() {
        return stationSeeds;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public List<ArtRef> getImageArtRefs() {
        return imageArtRefs;
    }

    public List<ArtRef> getCompositeArtRefs() {
        return compositeArtRefs;
    }

    public List<String> getContentTypes() {
        return contentTypes;
    }

    public String getByline() {
        return byline;
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

    public void delete()
        throws IOException {
        GPlayMusic.getApiInstance().deleteStations(this);
    }

    public static Station create(@NotNull StationSeed seed, String name, boolean includeTracks, int numEntries)
            throws IOException {
        Mutator mutator = new Mutator(MutationFactory.getAddStationMutation(name, seed, includeTracks, numEntries));
        MutationResponse response = GPlayMusic.getApiInstance().makeBatchCall(BATCH_URL, mutator);
        String id = response.getItems().get(0).getId();
        return null;
    }
}

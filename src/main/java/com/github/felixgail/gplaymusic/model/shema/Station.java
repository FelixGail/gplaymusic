package com.github.felixgail.gplaymusic.model.shema;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.interfaces.Result;
import com.github.felixgail.gplaymusic.model.requestbodies.ListStationTracksRequest;
import com.github.felixgail.gplaymusic.model.requestbodies.mutations.MutationFactory;
import com.github.felixgail.gplaymusic.model.requestbodies.mutations.Mutator;
import com.github.felixgail.gplaymusic.model.shema.snippets.ArtRef;
import com.github.felixgail.gplaymusic.model.shema.snippets.StationSeed;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

    Station(final String name, final StationSeed seed, final List<Track> tracks) {
        this.name = name;
        this.seed = seed;
        this.tracks = tracks;
    }

    public static Station create(final StationSeed seed, final String name, final boolean includeTracks, final int numEntries)
            throws IOException {
        final Mutator mutator = new Mutator(MutationFactory.getAddStationMutation(name, seed, includeTracks, numEntries));
        final MutationResponse response = GPlayMusic.getApiInstance().getService().makeBatchCall(BATCH_URL, mutator);
        response.getItems().get(0).getId();
        return null;
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

    /**
     * Get Tracks for this Station.<br>
     * <b>Keep in mind that this can return an empty list, if this station is created on an empty playlist.</b>
     *
     * @param numEntries     number of tracks that will be returned.
     * @param recentlyPlayed a list of tracks that have recently been played. tracks from this list will be excluded from the response
     * @param newCall        true if a new call shall be dispatched. false if the list from a previous call is to be returned.
     *                       Careful: Will return null if no call has been made.
     * @return A list of tracks for this station.
     */
    public List<Track> getTracks(int numEntries, List<Track> recentlyPlayed, boolean newCall) throws IOException {
        if (!newCall) {
            return tracks;
        }
        ListStationTracksRequest request = new ListStationTracksRequest(this, numEntries, recentlyPlayed);
        Optional<List<Track>> trackOptional =
                Optional.of(GPlayMusic.getApiInstance().getService().getFilledStations(request)
                        .execute().body().toList().get(0).tracks);
        return trackOptional.orElse(new LinkedList<>());
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

    public void setSessionToken(final String sessionToken) {
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
}

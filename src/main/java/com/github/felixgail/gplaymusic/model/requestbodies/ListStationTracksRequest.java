package com.github.felixgail.gplaymusic.model.requestbodies;

import com.github.felixgail.gplaymusic.model.shema.Station;
import com.github.felixgail.gplaymusic.model.shema.Track;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ListStationTracksRequest extends PagingRequest implements Serializable {
    @Expose
    private int contentFilter = 1;
    @Expose
    private List<StationRequest> stations;

    /**
     * Create a request to return a list of Tracks for a Station.
     *
     * @param station              A Station the {@link Track}s will be received for.
     * @param numEntries           number of {@link Track}s that will be requested. Max. 1000.
     * @param recentlyPlayedTracks A List of Tracks that have recently been played. Tracks from this list will not be
     *                             in the result.
     */
    public ListStationTracksRequest(Station station, int numEntries, List<Track> recentlyPlayedTracks) {
        this(station, numEntries, recentlyPlayedTracks, null, -1);
    }

    /**
     * Create a request to return a list of Tracks for a Station.
     *
     * @param station              A Station the {@link Track}s will be received for.
     * @param numEntries           number of {@link Track}s that will be requested. Max. 1000.
     * @param recentlyPlayedTracks A List of Tracks that have recently been played. Tracks from this list will not be
     *                             in the result.
     */
    public ListStationTracksRequest(Station station, int numEntries, List<Track> recentlyPlayedTracks, String nextPageToken, int maxResults) {
        super(nextPageToken, maxResults);
        stations = Collections.singletonList(new StationRequest(station, numEntries, recentlyPlayedTracks));
    }

    public int getContentFilter() {
        return contentFilter;
    }

    public List<StationRequest> getStations() {
        return stations;
    }

    private class StationRequest implements Serializable {
        @Expose
        private int numEntries;
        @Expose
        private String radioId;
        @Expose
        private List<String> recentlyPlayed;

        StationRequest(Station station, int numEntries, List<Track> recentlyPlayedTracks) {
            this.radioId = station.getId();
            this.numEntries = numEntries;
            if (recentlyPlayedTracks != null) {
                this.recentlyPlayed = new LinkedList<>();
                for (Track track : recentlyPlayedTracks) {
                    this.recentlyPlayed.add(track.getID());
                }
            }
        }
    }
}

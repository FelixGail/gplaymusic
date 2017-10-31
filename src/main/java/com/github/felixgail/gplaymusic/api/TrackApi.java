package com.github.felixgail.gplaymusic.api;

import com.github.felixgail.gplaymusic.cache.LibraryTrackCache;
import com.github.felixgail.gplaymusic.model.Track;
import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.requests.SearchTypes;

import java.io.IOException;
import java.util.List;

public class TrackApi implements SubApi{
    private GPlayMusic mainApi;
    private LibraryTrackCache libraryTrackCache;

    TrackApi(GPlayMusic api) {
        this.mainApi = api;
        this.libraryTrackCache  =new LibraryTrackCache(api);
    }

    /**
     * Provides convenience by wrapping the {@link GPlayMusic#search(String, int, SearchTypes)} method and limiting
     * the content types to Tracks only.
     *
     * @return Returns a list of tracks returned by the google play service.
     */
    public List<Track> search(String query, int maxResults) throws IOException{
        return mainApi.search(query, maxResults, new SearchTypes(ResultType.TRACK))
                .getTracks();
    }

    public Track getTrack(String trackID) throws IOException {
        Track track = null;
        if (trackID.startsWith("T")) {
            track = mainApi.getService().fetchTrack(trackID).execute().body();
        } else {
            track = libraryTrackCache.find(trackID).orElseThrow(() ->
                    new IllegalArgumentException(String.format("No track with id '%s' found.", trackID)));
        }
        if (track == null || track.getID() == null) {
            throw new IOException(String.format("'%s' did not return a valid track", trackID));
        }
        return track;
    }

    /**
     * Library tracks can only be fetched as whole. To shorten wait times, collected songs are cached.
     * Please consider updating the cache (asynchronously) when using the library over a long period of time, or when
     * new songs could be added to the library during runtime.
     * <br>
     * If outside access to the library is expected during runtime, disabling caching via {@link #useCache(boolean)}
     * should also be considered.
     */
    public void updateCache() throws IOException {
        libraryTrackCache.update();
    }

    /**
     * Enables/Disables caching of library tracks.
     */
    public void useCache(boolean useCache) {
        libraryTrackCache.setUseCache(useCache);
    }

    @Override
    public GPlayMusic getMainApi() {
        return mainApi;
    }
}

package com.github.felixgail.gplaymusic.api;


import com.github.felixgail.gplaymusic.api.exceptions.NetworkException;
import com.github.felixgail.gplaymusic.model.abstracts.ListenNowItem;
import com.github.felixgail.gplaymusic.model.config.Config;
import com.github.felixgail.gplaymusic.model.enums.Provider;
import com.github.felixgail.gplaymusic.model.enums.StreamQuality;
import com.github.felixgail.gplaymusic.model.requestbodies.*;
import com.github.felixgail.gplaymusic.model.requestbodies.mutations.Mutator;
import com.github.felixgail.gplaymusic.model.search.SearchResponse;
import com.github.felixgail.gplaymusic.model.search.SearchTypes;
import com.github.felixgail.gplaymusic.model.shema.*;
import com.github.felixgail.gplaymusic.model.shema.listennow.ListenNowSituation;
import com.github.felixgail.gplaymusic.util.language.Language;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.*;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public interface GPlayService {

    @GET("sj/v2.5/query")
    Call<SearchResponse> search(@Query("q") String query,
                                @Query("max-results") int maxResults,
                                @Query("ct") SearchTypes searchTypes);

    @GET("sj/v2.5/config?dv=0&tier=ff")
    Call<Config> config(@Query("hl") Locale locale);

    @GET("music/{provider}?net=mob&pt=e")
    Call<Void> getTrackLocationMJCK(@Header("X-Device-ID") String androidID,
                                    @Path("provider") Provider provider,
                                    @Query("opt") StreamQuality quality,
                                    @Query("slt") String salt,
                                    @Query("sig") String signature,
                                    @Query("mjck") String trackID,
                                    @QueryMap Map<String, String> kwargs);

    @GET("music/{provider}?net=mob&pt=e")
    Call<Void> getTrackLocationSongId(@Header("X-Device-ID") String androidID,
                                      @Path("provider") Provider provider,
                                      @Query("opt") StreamQuality quality,
                                      @Query("slt") String salt,
                                      @Query("sig") String signature,
                                      @Query("songid") String trackID,
                                      @QueryMap Map<String, String> kwargs);

    @GET("sj/v2.5/devicemanagementinfo")
    Call<ListResult<DeviceInfo>> getDevices();

    @POST("sj/v2.5/ephemeral/top")
    Call<ListResult<Track>> getPromotedTracks();

    @POST("sj/v2.5/radio/station")
    Call<ListResult<Station>> listStations();

    @POST("sj/v2.5/radio/station")
    Call<ListResult<Station>> listStations(@Body PagingRequest body);

    @POST("sj/v2.5/playlistfeed")
    Call<ListResult<Playlist>> listPlaylists();

    @POST("sj/v2.5/playlistfeed")
    Call<ListResult<Playlist>> listPlaylists(@Body PagingRequest body);

    @GET("sj/v2.5/podcast/browse")
    Call<ListResult<PodcastSeries>> listBrowsePodcastSeries(@Query("id") String genre);

    @GET("sj/v2.5/listennow/getlistennowitems")
    Call<ListResult<ListenNowItem>> listListenNowItems();

    @POST("sj/v2.5/listennow/situations")
    Call<ListenNowSituation> listListenNowSituations(@Body TimeZoneOffset offset);

    /**
     * As far as my understanding goes, this simply returns a list containing
     * every {@link PlaylistEntry} from every {@link Playlist}
     * that is {@link Playlist.PlaylistType#USER_GENERATED}.<br>
     * <p>
     * Entries from {@link Playlist.PlaylistType#SHARED} playlists that
     * the user is subscribed to are <b>not</b> included. To contents from such a playlist use
     * TODO
     * <p>
     * The Server has no option to return the contents of a single Playlist.
     *
     * @return the {@link Call} to request a list of {@link PlaylistEntry}
     */
    @POST("sj/v2.5/plentryfeed")
    Call<ListResult<PlaylistEntry>> listPrivatePlaylistEntries();

    /**
     * As far as my understanding goes, this simply returns a list containing
     * every {@link PlaylistEntry} from every {@link Playlist}
     * that is {@link Playlist.PlaylistType#USER_GENERATED}.<br>
     * <p>
     * Entries from {@link Playlist.PlaylistType#SHARED} playlists that
     * the user is subscribed to are <b>not</b> included. To contents from such a playlist use
     * TODO
     * <p>
     * The Server has no option to return the contents of a single Playlist.
     *
     * @return the {@link Call} to request a list of {@link PlaylistEntry}
     */
    @POST("sj/v2.5/plentryfeed")
    Call<ListResult<PlaylistEntry>> listPrivatePlaylistEntries(@Body PagingRequest body);

    @POST("sj/v2.5/plentries/shared")
    Call<SharedPlaylistEntryListResult> listSharedPlaylistEntries(@Body SharedPlaylistRequest request);

    @POST
    Call<MutationResponse> batchCall(@Url String path, @Body Mutator mutator);

    @POST("sj/v2.5/radio/stationfeed")
    Call<ListResult<Station>> getFilledStations(@Body ListStationTracksRequest request);

    @POST("sj/v2.5/trackstats")
    Call<MutationResponse> incremetPlaycount(@Body IncrementPlaycountRequest request);

    default MutationResponse makeBatchCall(String path, Mutator body)
            throws IOException {
        Response<MutationResponse> response = batchCall("sj/v2.5/" + path, body).execute();
        if (!response.body().checkSuccess()) {
            NetworkException exception = new NetworkException(400, Language.get("network.GenericError"));
            exception.setResponse(response.raw());
            throw exception;
        }
        return response.body();
    }
}

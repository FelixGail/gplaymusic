package com.github.felixgail.gplaymusic.api;


import com.github.felixgail.gplaymusic.model.enums.Provider;
import com.github.felixgail.gplaymusic.model.enums.StreamQuality;
import com.github.felixgail.gplaymusic.model.config.Config;
import com.github.felixgail.gplaymusic.model.requestbodies.TimeZoneOffset;
import com.github.felixgail.gplaymusic.model.requestbodies.mutations.Mutator;
import com.github.felixgail.gplaymusic.model.search.SearchResponse;
import com.github.felixgail.gplaymusic.model.search.SearchTypes;
import com.github.felixgail.gplaymusic.model.shema.*;
import com.github.felixgail.gplaymusic.model.abstracts.ListenNowItem;
import com.github.felixgail.gplaymusic.model.shema.listennow.ListenNowSituation;
import retrofit2.Call;
import retrofit2.http.*;

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

    @POST("sj/v2.5/playlistfeed")
    Call<ListResult<Playlist>> listPlaylists();

    @GET("sj/v2.5/podcast/browse")
    Call<ListResult<PodcastSeries>> listBrowsePodcastSeries(@Query("id") String genre);

    @GET("sj/v2.5/listennow/getlistennowitems")
    Call<ListResult<ListenNowItem>> listListenNowItems();

    @POST("sj/v2.5/listennow/situations")
    Call<ListenNowSituation> listListenNowSituations(@Body TimeZoneOffset offset);

    /**
     * As far as my understanding goes, this simply returns a list of {@link PlaylistEntry}
     * randomly selected from the users playlists
     *
     * @return the {@link Call} to request a list of {@link PlaylistEntry}
     */
    @POST("sj/v2.5/plentryfeed")
    Call<ListResult<PlaylistEntry>> listPlaylistEntries();

    @POST("sj/v2.5/{path}")
    Call<MutationResponse> batchCall(@Path("path") String path, @Body Mutator mutator);
}

package com.github.felixgail.gplaymusic.api;


import com.github.felixgail.gplaymusic.exceptions.NetworkException;
import com.github.felixgail.gplaymusic.model.Album;
import com.github.felixgail.gplaymusic.model.Artist;
import com.github.felixgail.gplaymusic.model.Config;
import com.github.felixgail.gplaymusic.model.DeviceInfo;
import com.github.felixgail.gplaymusic.model.MutationResponse;
import com.github.felixgail.gplaymusic.model.Playlist;
import com.github.felixgail.gplaymusic.model.PlaylistEntry;
import com.github.felixgail.gplaymusic.model.PodcastSeries;
import com.github.felixgail.gplaymusic.model.Station;
import com.github.felixgail.gplaymusic.model.Track;
import com.github.felixgail.gplaymusic.model.enums.Provider;
import com.github.felixgail.gplaymusic.model.enums.StreamQuality;
import com.github.felixgail.gplaymusic.model.listennow.ListListenNowItemResponse;
import com.github.felixgail.gplaymusic.model.listennow.ListenNowSituation;
import com.github.felixgail.gplaymusic.model.requests.IncrementPlaycountRequest;
import com.github.felixgail.gplaymusic.model.requests.ListStationTracksRequest;
import com.github.felixgail.gplaymusic.model.requests.PagingRequest;
import com.github.felixgail.gplaymusic.model.requests.SearchTypes;
import com.github.felixgail.gplaymusic.model.requests.SharedPlaylistRequest;
import com.github.felixgail.gplaymusic.model.requests.TimeZoneOffset;
import com.github.felixgail.gplaymusic.model.requests.mutations.Mutator;
import com.github.felixgail.gplaymusic.model.responses.GenreResponse;
import com.github.felixgail.gplaymusic.model.responses.ListResult;
import com.github.felixgail.gplaymusic.model.responses.SearchResponse;
import com.github.felixgail.gplaymusic.model.responses.SharedPlaylistEntryListResult;
import com.github.felixgail.gplaymusic.util.language.Language;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

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

  @POST("sj/v2.5/trackfeed")
  Call<ListResult<Track>> listTracks();

  @POST("sj/v2.5/trackfeed")
  Call<ListResult<Track>> listTracks(@Body PagingRequest body);

  @GET("sj/v2.5/podcast/browse")
  Call<ListResult<PodcastSeries>> listBrowsePodcastSeries(@Query("id") String genre);

  @GET("sj/v2.5/listennow/getlistennowitems")
  Call<ListListenNowItemResponse> listListenNowItems();

  @POST("sj/v2.5/listennow/situations")
  Call<ListenNowSituation> getListenNowSituation(@Body TimeZoneOffset offset);

  /**
   * As far as my understanding goes, this simply returns a list containing
   * every {@link PlaylistEntry} from every {@link Playlist}
   * that is {@link Playlist.PlaylistType#USER_GENERATED}.<br>
   * <p>
   * Entries from {@link Playlist.PlaylistType#SHARED} playlists that
   * the user is subscribed to are <b>not</b> included. To fetch contents from such a playlist use
   * {@link #listSharedPlaylistEntries(SharedPlaylistRequest)}.
   * <p>
   * The Server has no option to return the contents of a single private Playlist.
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
   * the user is subscribed to are <b>not</b> included. To fetch contents from such a playlist use
   * {@link #listSharedPlaylistEntries(SharedPlaylistRequest)}.
   * <p>
   * The Server has no option to return the contents of a single private Playlist.
   *
   * @return the {@link Call} to request a list of {@link PlaylistEntry}
   */
  @POST("sj/v2.5/plentryfeed")
  Call<ListResult<PlaylistEntry>> listPrivatePlaylistEntries(@Body PagingRequest body);

  @POST("sj/v2.5/plentries/shared")
  Call<SharedPlaylistEntryListResult> listSharedPlaylistEntries(@Body SharedPlaylistRequest request);

  @POST
  Call<MutationResponse> batchCall(@Url String path, @Body Mutator mutator);

  @POST("sj/v2.5/radio/stationfeed?")
  Call<ListResult<Station>> getFilledStations(@Body ListStationTracksRequest request);

  @POST("sj/v2.5/trackstats")
  Call<MutationResponse> incremetPlaycount(@Body IncrementPlaycountRequest request);

  @GET("sj/v2.5/explore/genres")
  Call<GenreResponse> getGenres(@Query("parent-genre") String parentGenre);

  @GET("sj/v2.5/fetchalbum")
  Call<Album> getAlbum(@Query("nid") String albumID, @Query("include-tracks") boolean includeTracks);

  @GET("sj/v2.5/fetchtrack")
  Call<Track> fetchTrack(@Query("nid") String trackId);

  /**
   * Fetches for an artist by {@code artistID}.
   *
   * @param artistID      {@link Artist#getArtistId()} of the artist searched for.
   * @param includeAlbums whether albums of the artist shall be included in the response.
   * @param numTopTracks  response includes up to provided number of most heard songs in response
   * @param numRelArtist  response includes up to provided number of similar artist in response
   * @return An executable call which returns an artist on execution.
   */
  @GET("sj/v2.5/fetchartist")
  Call<Artist> getArtist(@Query("nid") String artistID, @Query("include-albums") boolean includeAlbums,
                         @Query("num-top-tracks") int numTopTracks,
                         @Query("num-related-artist") int numRelArtist);

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

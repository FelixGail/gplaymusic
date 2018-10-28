package com.github.felixgail.gplaymusic.api;

import com.github.felixgail.gplaymusic.exceptions.InitializationException;
import com.github.felixgail.gplaymusic.exceptions.NetworkException;
import com.github.felixgail.gplaymusic.model.Album;
import com.github.felixgail.gplaymusic.model.Artist;
import com.github.felixgail.gplaymusic.model.Config;
import com.github.felixgail.gplaymusic.model.DeviceInfo;
import com.github.felixgail.gplaymusic.model.Genre;
import com.github.felixgail.gplaymusic.model.Model;
import com.github.felixgail.gplaymusic.model.PodcastSeries;
import com.github.felixgail.gplaymusic.model.Track;
import com.github.felixgail.gplaymusic.model.listennow.ListenNowItem;
import com.github.felixgail.gplaymusic.model.listennow.ListenNowSituation;
import com.github.felixgail.gplaymusic.model.listennow.ListenNowStation;
import com.github.felixgail.gplaymusic.model.requests.SearchTypes;
import com.github.felixgail.gplaymusic.model.requests.TimeZoneOffset;
import com.github.felixgail.gplaymusic.model.responses.ListResult;
import com.github.felixgail.gplaymusic.model.responses.Result;
import com.github.felixgail.gplaymusic.model.responses.SearchResponse;
import com.github.felixgail.gplaymusic.util.TokenProvider;
import com.github.felixgail.gplaymusic.util.deserializer.ConfigDeserializer;
import com.github.felixgail.gplaymusic.util.deserializer.ListenNowStationDeserializer;
import com.github.felixgail.gplaymusic.util.deserializer.ModelPostProcessor;
import com.github.felixgail.gplaymusic.util.deserializer.ResultDeserializer;
import com.github.felixgail.gplaymusic.util.interceptor.ErrorInterceptor;
import com.github.felixgail.gplaymusic.util.interceptor.LoggingInterceptor;
import com.github.felixgail.gplaymusic.util.interceptor.RequestInterceptor;
import com.github.felixgail.gplaymusic.util.language.Language;
import com.google.gson.GsonBuilder;
import io.gsonfire.GsonFireBuilder;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import svarzee.gps.gpsoauth.AuthToken;

/**
 * The main API, wrapping calls to the service. Use the {@link GPlayMusic.Builder} to create a new
 * instance.
 */
public final class GPlayMusic {

  private GPlayService service;
  private Config config;
  private RequestInterceptor interceptor;
  private GenreApi genreApi;
  private PlaylistApi playlistApi;
  private PlaylistEntryApi playlistEntryApi;
  private StationApi stationApi;
  private TrackApi trackApi;

  private GPlayMusic(GPlayService service, RequestInterceptor interceptor) {
    this.service = service;
    this.interceptor = interceptor;
    this.genreApi = new GenreApi(this);
    this.playlistEntryApi = new PlaylistEntryApi(this);
    this.playlistApi = new PlaylistApi(this, playlistEntryApi);
    this.stationApi = new StationApi(this);
    this.trackApi = new TrackApi(this);
  }

  public Config getConfig() {
    return config;
  }

  private void setConfig(Config config) {
    this.config = config;
  }

  public Album getAlbum(String albumID, boolean includeTracks) throws IOException {
    return getService().getAlbum(albumID, includeTracks).execute().body();
  }

  /**
   * Fetches for an artist by {@code artistID}.
   *
   * @param artistID {@link Artist#getArtistId()} of the artist searched for.
   * @param includeAlbums whether albums of the artist shall be included in the response.
   * @param numTopTracks response includes up to provided number of most heard songs in response
   * @param numRelArtist response includes up to provided number of similar artist in response
   * @return An executable call which returns an artist on execution.
   */
  public Artist getArtist(String artistID, boolean includeAlbums, int numTopTracks,
      int numRelArtist)
      throws IOException {
    return getService().getArtist(artistID, includeAlbums, numTopTracks, numRelArtist)
        .execute().body();
  }

  /**
   * This method will return the service used to make calls to google play, and therefore allows for
   * low level and asynchronous calls. Be sure to check the response for error codes.
   *
   * @return {@link GPlayService} instance used by the current API.
   */
  public GPlayService getService() {
    return this.service;
  }

  /**
   * Queries Google Play Music for content. Content can be every combination of {@link SearchTypes}
   * enum.
   *
   * @param query Query String
   * @param maxResults Limits the results. Keep in mind that higher numbers increase loading time.
   * @param types Content types that should be queried for
   * @return A SearchResponse instance holding all content returned
   * @throws IOException Throws an IOException on severe failures (no internet connection...) or a
   * {@link NetworkException} on request failures.
   */
  public SearchResponse search(String query, int maxResults, SearchTypes types)
      throws IOException {
    return getService().search(query, maxResults, types).execute().body();
  }

  /**
   * Provides convenience by wrapping the {@link #search(String, int, SearchTypes)} method and
   * setting the maxResults parameter to 50.
   */
  public SearchResponse search(String query, SearchTypes types)
      throws IOException {
    return search(query, 50, types);
  }

  /**
   * Fetches a list of all devices connected with the current google play account.
   *
   * @return A DeviceList instance containing all devices connected to the current Google Play
   * account.
   * @throws IOException Throws an IOException on severe failures (no internet connection...) or a
   * {@link NetworkException} on request failures.
   */
  public ListResult<DeviceInfo> getRegisteredDevices()
      throws IOException {
    return getService().getDevices().execute().body();
  }

  /**
   * Returns a list of promoted {@link Track}s. Which tracks are in the returned list is determined
   * by google
   *
   * @throws IOException on severe failures (no internet connection...) or a {@link
   * NetworkException} on request failures.
   */
  public List<Track> getPromotedTracks()
      throws IOException {
    return getService().getPromotedTracks().execute().body().toList();
    //TODO: set api
  }

  public List<PodcastSeries> listPodcastSeries(Genre genre)
      throws IOException {
    return service.listBrowsePodcastSeries(genre.getId()).execute().body().toList();
  }

  /**
   * Returns a selection of {@link ListenNowItem}s consisting of {@link ListenNowStation} and {@link
   * com.github.felixgail.gplaymusic.model.listennow.ListenNowAlbum}.
   */
  public List<ListenNowItem> listListenNowItems() throws IOException {
    return service.listListenNowItems().execute().body().getListenNowItems();
  }

  /**
   * Returns the current {@link ListenNowStation} for your timezone.
   */
  public ListenNowSituation getListenNowSituation() throws IOException {
    return service.getListenNowSituation(new TimeZoneOffset()).execute().body();
  }

  /**
   * Returns the current {@link ListenNowStation} with a set offset.
   *
   * @param offsetInSeconds the offset in seconds to UTC.
   */
  public ListenNowSituation getListenNowSituation(int offsetInSeconds) throws IOException {
    return service.getListenNowSituation(new TimeZoneOffset(String.valueOf(offsetInSeconds)))
        .execute().body();
  }

  public GenreApi getGenreApi() {
    return genreApi;
  }

  public PlaylistApi getPlaylistApi() {
    return playlistApi;
  }

  public PlaylistEntryApi getPlaylistEntryApi() {
    return playlistEntryApi;
  }

  public StationApi getStationApi() {
    return stationApi;
  }

  public TrackApi getTrackApi() {
    return trackApi;
  }

  /**
   * Changes the token used to authenticate the client.
   *
   * @param token a new valid token to access the google service
   */
  public void changeToken(@NotNull AuthToken token) {
    interceptor.setToken(token);
  }

  /**
   * Use this class to create a {@link GPlayMusic} instance.
   */
  public final static class Builder {

    private OkHttpClient.Builder httpClientBuilder;
    private AuthToken authToken;
    private Locale locale = Locale.US;
    private String androidID;
    private ErrorInterceptor.InterceptorBehaviour
        interceptorBehaviour = ErrorInterceptor.InterceptorBehaviour.THROW_EXCEPTION;
    private boolean debug = false;

    /**
     * Used while building the {@link GPlayMusic} instance. If no {@link OkHttpClient.Builder} is
     * provided via {@link #setHttpClientBuilder(OkHttpClient.Builder)} the instance returned by
     * this method will be used for building.
     *
     * @return Returns the default {@link OkHttpClient.Builder} instance
     */
    public static OkHttpClient.Builder getDefaultHttpBuilder() {
      ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
          .tlsVersions(TlsVersion.TLS_1_2)
          .cipherSuites(
              CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
              CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
              CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
          .build();
      return new OkHttpClient.Builder()
          .connectionSpecs(Collections.singletonList(spec));
    }

    public boolean isDebug() {
      return debug;
    }

    /**
     * If set to <em>true</em>, will log every request and response made by the Client.<br> <b> Be
     * careful as this can easily leak personal information. Be sure to always check the output.
     * </b>
     */
    public GPlayMusic.Builder setDebug(boolean debug) {
      this.debug = debug;
      return this;
    }

    /**
     * Set a custom {@link OkHttpClient.Builder} to build the {@link GPlayMusic} instance with. If
     * left untouched the Builder will use the default instance accessible through {@link
     * #getDefaultHttpBuilder()}.
     *
     * @return This {@link Builder} instance.
     */
    public Builder setHttpClientBuilder(OkHttpClient.Builder builder) {
      this.httpClientBuilder = builder;
      return this;
    }

    /**
     * Set an {@link AuthToken} to access the Google Play Service. This method has to be called
     * before building the {@link GPlayMusic} instance.
     *
     * @param token An {@link AuthToken} either saved from a prior session or created through the
     * {@link TokenProvider#provideToken(String, String, String)} method.
     * @return This {@link Builder} instance.
     */
    public Builder setAuthToken(AuthToken token) {
      this.authToken = token;
      return this;
    }

    /**
     * Set a local to use during calls to the Google Play Service. Defaults to {@link Locale#US}.
     *
     * @return This {@link Builder} instance.
     */
    public Builder setLocale(Locale locale) {
      this.locale = locale;
      return this;
    }

    /**
     * Set a Behaviour for the {@link ErrorInterceptor} appended to the {@link
     * OkHttpClient.Builder}. Throws Exceptions on default. May cause instability when set to {@link
     * com.github.felixgail.gplaymusic.util.interceptor.ErrorInterceptor.InterceptorBehaviour#LOG}.
     *
     * @return This {@link Builder} instance.
     */
    public Builder setInterceptorBehaviour(
        ErrorInterceptor.InterceptorBehaviour interceptorBehaviour) {
      this.interceptorBehaviour = interceptorBehaviour;
      return this;
    }

    /**
     * Set an AndroidID used for stream calls to the Google Play Service. In general the IMEI used
     * for logging in should be sufficient. If logged in with a MAC use {@link
     * GPlayMusic#getRegisteredDevices()} for a list of connected devices or leave empty. On empty
     * the {@link #build()}-method will use search online for a suitable device.
     *
     * @return This {@link Builder} instance.
     */
    public Builder setAndroidID(String androidID) {
      this.androidID = androidID;
      return this;
    }

    /**
     * Builds a new {@link GPlayMusic} instance with the customizations set to this builder. Make
     * sure to call {@link #setAuthToken(AuthToken)} before building with this method.
     *
     * @return Returns a new {@link GPlayMusic} instance
     */
    public GPlayMusic build() {
      try {
        if (this.authToken == null) {
          throw new InitializationException(Language.get("api.init.EmptyToken"));
        }

        if (this.httpClientBuilder == null) {
          this.httpClientBuilder = getDefaultHttpBuilder();
        }

        RequestInterceptor parameterInterceptor = new RequestInterceptor(authToken);

        this.httpClientBuilder
            .addInterceptor(parameterInterceptor)
            .addInterceptor(new ErrorInterceptor(this.interceptorBehaviour))
            .followRedirects(false);
        if (this.debug) {
          this.httpClientBuilder.addInterceptor(new LoggingInterceptor());
        }

        OkHttpClient httpClient = this.httpClientBuilder.build();

        ModelPostProcessor postProcessor = new ModelPostProcessor();
        GsonBuilder builder = new GsonFireBuilder()
            .registerPostProcessor(Model.class, postProcessor)
            .createGsonBuilder()
            .registerTypeAdapter(Result.class, new ResultDeserializer())
            .registerTypeAdapter(ListenNowStation.class, new ListenNowStationDeserializer());

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://mclients.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create(builder.create()))
            .client(httpClient)
            .build();

        GPlayMusic gPlay = new GPlayMusic(retrofit.create(GPlayService.class),
            parameterInterceptor);
        postProcessor.setApi(gPlay);
        retrofit2.Response<Config> configResponse;
        configResponse = gPlay.getService().config(this.locale).execute();
        if (!configResponse.isSuccessful()) {
          throw new InitializationException(Language.get("network.GenericError"),
              NetworkException.parse(configResponse.raw()));
        }
        Config config = configResponse.body();
        if (config == null) {
          throw new InitializationException(Language.get("api.init.EmptyConfig"));
        }
        config.setLocale(locale);
        Language.setLocale(locale);
        gPlay.setConfig(config);

        parameterInterceptor.addParameter("dv", "0")
            .addParameter("hl", locale.toString())
            .addParameter("tier", config.getSubscription().getValue());

        if (androidID == null) {
          Optional<DeviceInfo> optional =
              gPlay.getRegisteredDevices().toList().stream()
                  .filter(deviceInfo -> (deviceInfo.getType().equals("ANDROID")))
                  .findFirst();
          if (optional.isPresent()) {
            config.setAndroidID(optional.get().getId());
          } else {
            throw new InitializationException(Language.get("api.init.NoAndroidId"));
          }
        } else {
          config.setAndroidID(androidID);
        }
        return gPlay;
      } catch (IOException e) {
        throw new InitializationException(e);
      }
    }

  }
}

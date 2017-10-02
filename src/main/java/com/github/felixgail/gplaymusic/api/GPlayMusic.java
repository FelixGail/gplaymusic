package com.github.felixgail.gplaymusic.api;

import com.github.felixgail.gplaymusic.api.exceptions.InitializationException;
import com.github.felixgail.gplaymusic.api.exceptions.NetworkException;
import com.github.felixgail.gplaymusic.model.abstracts.ListenNowItem;
import com.github.felixgail.gplaymusic.model.config.Config;
import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.interfaces.PagingHandler;
import com.github.felixgail.gplaymusic.model.interfaces.Result;
import com.github.felixgail.gplaymusic.model.requestbodies.PagingRequest;
import com.github.felixgail.gplaymusic.model.requestbodies.TimeZoneOffset;
import com.github.felixgail.gplaymusic.model.requestbodies.mutations.MutationFactory;
import com.github.felixgail.gplaymusic.model.requestbodies.mutations.Mutator;
import com.github.felixgail.gplaymusic.model.search.SearchResponse;
import com.github.felixgail.gplaymusic.model.search.SearchTypes;
import com.github.felixgail.gplaymusic.model.shema.DeviceInfo;
import com.github.felixgail.gplaymusic.model.shema.Genre;
import com.github.felixgail.gplaymusic.model.shema.ListResult;
import com.github.felixgail.gplaymusic.model.shema.Playlist;
import com.github.felixgail.gplaymusic.model.shema.PlaylistEntry;
import com.github.felixgail.gplaymusic.model.shema.PodcastSeries;
import com.github.felixgail.gplaymusic.model.shema.Station;
import com.github.felixgail.gplaymusic.model.shema.Track;
import com.github.felixgail.gplaymusic.model.shema.listennow.ListenNowSituation;
import com.github.felixgail.gplaymusic.model.shema.listennow.ListenNowStation;
import com.github.felixgail.gplaymusic.util.deserializer.ColorDeserializer;
import com.github.felixgail.gplaymusic.util.deserializer.ConfigDeserializer;
import com.github.felixgail.gplaymusic.util.deserializer.ListenNowStationDeserializer;
import com.github.felixgail.gplaymusic.util.deserializer.ResultDeserializer;
import com.github.felixgail.gplaymusic.util.interceptor.ErrorInterceptor;
import com.github.felixgail.gplaymusic.util.interceptor.LoggingInterceptor;
import com.github.felixgail.gplaymusic.util.interceptor.ParameterInterceptor;
import com.github.felixgail.gplaymusic.util.language.Language;
import com.google.gson.GsonBuilder;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import svarzee.gps.gpsoauth.AuthToken;

import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * The main API, wrapping calls to the service.
 * Use the {@link GPlayMusic.Builder} to create a new instance.
 */
public final class GPlayMusic {
  private static GPlayMusic instance;
  private GPlayService service;
  private Config config;

  private GPlayMusic(GPlayService service) {
    this.service = service;
    instance = this;
  }

  /**
   * @return Returns the last initiated api instance or a {@link InitializationException} if none was initialized.
   */
  public static GPlayMusic getApiInstance() {
    if (instance == null) {
      throw new InitializationException(Language.get("api.NotInitialized"));
    }
    return instance;
  }

  public Config getConfig() {
    return config;
  }

  private void setConfig(Config config) {
    this.config = config;
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
   * Queries Google Play Music for content.
   * Content can be every combination of {@link SearchTypes} enum.
   *
   * @param query      Query String
   * @param maxResults Limits the results. Keep in mind that higher numbers increase loading time.
   * @param types      Content types that should be queried for
   * @return A SearchResponse instance holding all content returned
   * @throws IOException Throws an IOException on severe failures (no internet connection...)
   *                     or a {@link NetworkException} on request failures.
   */
  public SearchResponse search(String query, int maxResults, SearchTypes types)
      throws IOException {
    return getService().search(query, maxResults, types).execute().body();
  }

  /**
   * Provides convenience by wrapping the {@link #search(String, int, SearchTypes)} method and setting the maxResults
   * parameter to 50.
   */
  public SearchResponse search(String query, SearchTypes types)
      throws IOException {
    return search(query, 50, types);
  }

  /**
   * Provides convenience by wrapping the {@link #search(String, int, SearchTypes)} method and limiting
   * the content types to Tracks only.
   *
   * @return Returns a list of tracks returned by the google play service.
   */
  public List<Track> searchTracks(String query, int maxResults)
      throws IOException {
    return search(query, maxResults, new SearchTypes(ResultType.TRACK)).getTracks();
  }

  /**
   * Fetches a list of all devices connected with the current google play account.
   *
   * @return A DeviceList instance containing all devices connected to the current Google Play account.
   * @throws IOException Throws an IOException on severe failures (no internet connection...)
   *                     or a {@link NetworkException} on request failures.
   */
  public ListResult<DeviceInfo> getRegisteredDevices()
      throws IOException {
    return getService().getDevices().execute().body();
  }

  /**
   * Returns a list of promoted {@link Track}s.
   * Which tracks are in the returned list is determined by google
   *
   * @throws IOException on severe failures (no internet connection...)
   *                     or a {@link NetworkException} on request failures.
   */
  public List<Track> getPromotedTracks()
      throws IOException {
    return getService().getPromotedTracks().execute().body().toList();
  }

  public void deletePlaylistEntries(PlaylistEntry... entries)
      throws IOException {
    deletePlaylistEntries(Arrays.asList(entries));
  }

  public void deletePlaylistEntries(Collection<PlaylistEntry> entries) throws IOException {
    Mutator mutator = new Mutator();
    entries.forEach(e -> mutator.addMutation(MutationFactory.getDeletePlaylistEntryMutation(e)));
    service.makeBatchCall(PlaylistEntry.BATCH_URL, mutator);
    Playlist.getCache().remove(entries);
  }

  public void deletePlaylists(Playlist... playlists)
      throws IOException {
    Mutator mutator = new Mutator();
    for (Playlist playlist : playlists) {
      mutator.addMutation(MutationFactory.getDeletePlaylistMutation(playlist));
    }
    service.makeBatchCall(Playlist.BATCH_URL, mutator);
  }

  public void deleteStations(Station... stations)
      throws IOException {
    Mutator mutator = new Mutator();
    for (Station station : stations) {
      mutator.addMutation(MutationFactory.getDeleteStationMutation(station));
    }
    service.makeBatchCall(Station.BATCH_URL, mutator);
  }

  public List<Station> listStations()
      throws IOException {
    return service.listStations().execute().body().toList();
  }

  public List<Playlist> listPlaylists()
      throws IOException {
    return new PagingHandler<Playlist>() {
      @Override
      public ListResult<Playlist> getChunk(String nextPageToken) throws IOException {
        return service.listPlaylists(new PagingRequest(nextPageToken, -1)).execute().body();
      }
    }.getAll();
  }

  public List<PodcastSeries> listPodcastSeries(Genre genre)
      throws IOException {
    return service.listBrowsePodcastSeries(genre.getId()).execute().body().toList();
  }

  public List<ListenNowItem> listListenNowItems() throws IOException {
    return service.listListenNowItems().execute().body().getListenNowItems();
  }

  public ListenNowSituation getListenNowSituation() throws IOException {
    return service.getListenNowSituation(new TimeZoneOffset()).execute().body();
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
     * provided via {@link #setHttpClientBuilder(OkHttpClient.Builder)} the instance returned by this method will
     * be used for building.
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
     * If set to <em>true</em>, will log every request and response made by the Client.<br>
     * <b>
     * Be careful as this can easily leak personal information. Be sure to always check the output.
     * </b>
     */
    public GPlayMusic.Builder setDebug(boolean debug) {
      this.debug = debug;
      return this;
    }

    /**
     * Set a custom {@link OkHttpClient.Builder} to build the {@link GPlayMusic} instance with.
     * If left untouched the Builder will use the default instance accessible
     * through {@link #getDefaultHttpBuilder()}.
     *
     * @return This {@link Builder} instance.
     */
    public Builder setHttpClientBuilder(OkHttpClient.Builder builder) {
      this.httpClientBuilder = builder;
      return this;
    }

    /**
     * Set an {@link AuthToken} to access the Google Play Service.
     * This method has to be called before building the {@link GPlayMusic} instance.
     *
     * @param token An {@link AuthToken} either saved from a prior session or created
     *              through the {@link TokenProvider#provideToken(String, String, String)} method.
     * @return This {@link Builder} instance.
     */
    public Builder setAuthToken(AuthToken token) {
      this.authToken = token;
      return this;
    }

    /**
     * Set a local to use during calls to the Google Play Service.
     * Defaults to {@link Locale#US}.
     *
     * @return This {@link Builder} instance.
     */
    public Builder setLocale(Locale locale) {
      this.locale = locale;
      return this;
    }

    /**
     * Set a Behaviour for the {@link ErrorInterceptor} appended to the {@link OkHttpClient.Builder}.
     * Throws Exceptions on default. May cause instability when set to {@link com.github.felixgail.gplaymusic.util.interceptor.ErrorInterceptor.InterceptorBehaviour#LOG}.
     *
     * @return This {@link Builder} instance.
     */
    public Builder setInterceptorBehaviour(ErrorInterceptor.InterceptorBehaviour interceptorBehaviour) {
      this.interceptorBehaviour = interceptorBehaviour;
      return this;
    }

    /**
     * Set an AndroidID used for stream calls to the Google Play Service.
     * In general the IMEI used for logging in should be sufficient.
     * If logged in with a MAC use {@link GPlayMusic#getRegisteredDevices()} for a list of
     * connected devices or leave empty.
     * On empty the {@link #build()}-method will use search online for a suitable device.
     *
     * @return This {@link Builder} instance.
     */
    public Builder setAndroidID(String androidID) {
      this.androidID = androidID;
      return this;
    }

    /**
     * @return Returns an Interceptor adding headers needed for communication with the
     * Google Play Service
     */
    private Interceptor getHeaderInterceptor() {
      return chain -> {
        final Request request = chain.request().newBuilder()
            .addHeader("Authorization", "GoogleLogin auth=" + this.authToken.getToken())
            .addHeader("Content-Type", "application/json")
            .build();

        return chain.proceed(request);
      };
    }

    /**
     * Builds a new {@link GPlayMusic} instance with the customizations set to this builder.
     * Make sure to call {@link #setAuthToken(AuthToken)} before building with this method.
     *
     * @return Returns a new {@link GPlayMusic} instance
     */
    public GPlayMusic build() {
      try {
        if (this.authToken == null) {
          throw new InitializationException(Language.get("api.init.EmptyToken"));
        }
        GsonBuilder gsonBuilder = new GsonBuilder()
            .registerTypeAdapter(Result.class, new ResultDeserializer())
            .registerTypeAdapter(Config.class, new ConfigDeserializer())
            .registerTypeAdapter(ListenNowStation.class, new ListenNowStationDeserializer())
            .registerTypeAdapter(Color.class, new ColorDeserializer());

        if (this.httpClientBuilder == null) {
          this.httpClientBuilder = getDefaultHttpBuilder();
        }

        ParameterInterceptor parameterInterceptor = new ParameterInterceptor();

        this.httpClientBuilder
            .addInterceptor(getHeaderInterceptor())
            .addInterceptor(parameterInterceptor)
            .addInterceptor(new ErrorInterceptor(this.interceptorBehaviour))
            .followRedirects(false);
        if (this.debug) {
          this.httpClientBuilder.addInterceptor(new LoggingInterceptor());
        }

        OkHttpClient httpClient = this.httpClientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://mclients.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
            .client(httpClient)
            .build();

        GPlayMusic gPlay = new GPlayMusic(retrofit.create(GPlayService.class));
        retrofit2.Response<Config> configResponse = null;
        configResponse = gPlay.getService().config(this.locale).execute();
        if (!configResponse.isSuccessful()) {
          throw new InitializationException(Language.get("network.GenericError"), NetworkException.parse(configResponse.raw()));
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

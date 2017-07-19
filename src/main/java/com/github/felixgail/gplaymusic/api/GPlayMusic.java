package com.github.felixgail.gplaymusic.api;

import com.github.felixgail.gplaymusic.api.exceptions.InitializationException;
import com.github.felixgail.gplaymusic.api.exceptions.NetworkException;
import com.github.felixgail.gplaymusic.model.Provider;
import com.github.felixgail.gplaymusic.model.SongQuality;
import com.github.felixgail.gplaymusic.model.SubscriptionType;
import com.github.felixgail.gplaymusic.model.abstracts.Signable;
import com.github.felixgail.gplaymusic.model.config.Config;
import com.github.felixgail.gplaymusic.model.search.ResultType;
import com.github.felixgail.gplaymusic.model.search.SearchResponse;
import com.github.felixgail.gplaymusic.model.search.SearchTypes;
import com.github.felixgail.gplaymusic.model.shema.*;
import com.github.felixgail.gplaymusic.util.deserializer.ConfigDeserializer;
import com.github.felixgail.gplaymusic.util.deserializer.ResultDeserializer;
import com.github.felixgail.gplaymusic.util.interceptor.ErrorInterceptor;
import com.github.felixgail.gplaymusic.util.interceptor.ParameterInterceptor;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import svarzee.gps.gpsoauth.AuthToken;

import java.io.IOException;
import java.util.*;

/**
 * The main API, wrapping calls to the service.
 * Use the {@link GPlayMusic.Builder} to create a new instance.
 */
public final class GPlayMusic {
    private static final Map<String, String> EMPTY_MAP = new HashMap<>();
    private static final Map<String, String> STATION_MAP = new HashMap<>();

    static {
        STATION_MAP.put("audio_formats", "mp3");
    }

    private GPlayService service;
    private Config config;

    private GPlayMusic(GPlayService service) {
        this.service = service;
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
     * Returns a URL to download the song in set quality.
     * URL will only be valid for 1 minute.
     * You will likely need to handle redirects.
     * <br>
     * <b>Please note that this function is available for Subscribers only.
     * On free accounts use getStationTrackURL.</b>
     *
     * @param title   title to download
     * @param quality quality of the stream
     * @return temporary url to the title
     * @throws IOException Throws an IOException on severe failures (no internet connection...)
     *                     or a {@link NetworkException} on request failures.
     */
    public String getTrackURL(Track title, SongQuality quality)
            throws IOException {
        if (getConfig().getSubscription() == SubscriptionType.FREE) {
            throw new IOException("Function not allowed for Free users");
        }
        return urlFetcher(title, quality, Provider.STREAM, EMPTY_MAP);
    }

    /**
     * Fetch the URL from a free Station.
     * Make sure the provided {@link Station} does not return Null on {@link Station#getSessionToken()}.
     * <br>
     * <b>Subscribers should use the {@link #getTrackURL(Track, SongQuality)}</b> method, which is easier to handle.
     * TODO: provide way to get session token.
     *
     * @param title   Title of the Song. This song has to be inside {@link Station#getTracks()}
     *                or the Server will respond with an error.
     *                To get a Station with Tracks call TODO!
     * @param station A station created by TODO.
     *                that contains the song queried for.
     * @param quality - quality of the stream
     * @return a url to download songs from.
     * @throws IOException on severe failures (no internet connection...)
     *                     or a {@link NetworkException} on request failures.
     */
    public String getStationTrackURL(Track title, FilledStation station, SongQuality quality)
            throws IOException {
        if (getConfig().getSubscription() == SubscriptionType.SUBSCRIBED) {
            return getTrackURL(title, quality);
        }
        Map<String, String> map = new HashMap<>();
        map.putAll(STATION_MAP);
        map.put("wentryid", title.getNid());
        map.put("sesstok", station.getSessionToken());
        return urlFetcher(title, quality, Provider.STATION, map);
    }

    /**
     * Wrapper for the {@link GPlayService#getTrackLocationMJCK(String, Provider, SongQuality, String, String, String, Map)}
     * and {@link GPlayService#getTrackLocationSongId(String, Provider, SongQuality, String, String, String, Map)}
     * methods. Determines which call to use and which parameters to add.
     *
     * @param signable A Childclass of singable as a {@link com.github.felixgail.gplaymusic.model.abstracts.Signable.Signature}
     *                 is needed for each url fetch
     * @param quality  Quality of the stream
     * @param provider Provider of the Signable. Determines wich url path to use (mplay,wplay,fplay)
     * @param kwargs   Map for additional query arguments. E.g. session token for stations
     * @return the url to the provided signable. expires after 1 minute.
     * @throws IOException - on severe failures (no internet connection...)
     *                     or a {@link NetworkException} on request failures.
     */
    private String urlFetcher(Signable signable, SongQuality quality,
                              Provider provider, Map<String, String> kwargs)
            throws IOException {
        Track.Signature sig = signable.getSignature();
        if (signable.getID().matches("^[TD]\\S*$")) {
            return getService().getTrackLocationMJCK(getConfig().getAndroidID(), provider,
                    quality, sig.getSalt(), sig.getSignature(), signable.getID(), kwargs
            ).execute().headers().get("Location");
        } else {
            return getService().getTrackLocationSongId(getConfig().getAndroidID(), provider,
                    quality, sig.getSalt(), sig.getSignature(), signable.getID(), kwargs
            ).execute().headers().get("Location");
        }
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
                    throw new InitializationException("AuthToken is not allowed to be empty. Use TokenProvider.provideToken" +
                            " if you need to generate one.");
                }
                GsonBuilder gsonBuilder = new GsonBuilder()
                        .registerTypeAdapter(Result.class, new ResultDeserializer())
                        .registerTypeAdapter(Config.class, new ConfigDeserializer());

                if (this.httpClientBuilder == null) {
                    this.httpClientBuilder = getDefaultHttpBuilder();
                }

                ParameterInterceptor parameterInterceptor = new ParameterInterceptor();

                OkHttpClient httpClient = this.httpClientBuilder
                        .addInterceptor(getHeaderInterceptor())
                        .addInterceptor(parameterInterceptor)
                        .addInterceptor(new ErrorInterceptor(this.interceptorBehaviour))
                        .followRedirects(false)
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://mclients.googleapis.com/")
                        .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                        .client(httpClient)
                        .build();

                GPlayMusic gPlay = new GPlayMusic(retrofit.create(GPlayService.class));
                retrofit2.Response<Config> configResponse = null;
                configResponse = gPlay.getService().config(this.locale).execute();
                if (!configResponse.isSuccessful()) {
                    throw new InitializationException("Network returned an error:", NetworkException.parse(configResponse.raw()));
                }
                Config config = configResponse.body();
                if (config == null) {
                    throw new InitializationException("Configuration Response empty.");
                }
                config.setLocale(locale);
                gPlay.setConfig(config);

                parameterInterceptor.addParameter("dv", "0")
                        .addParameter("hl", locale.toString())
                        .addParameter("tier", config.getSubscription().toString());

                if (androidID == null) {
                    Optional<DeviceInfo> optional =
                            gPlay.getRegisteredDevices().stream()
                                    .filter(deviceInfo -> (deviceInfo.getType().equals("ANDROID")))
                                    .findFirst();
                    if (optional.isPresent()) {
                        config.setAndroidID(optional.get().getId());
                    } else {
                        throw new InitializationException("#getAndroidID() returned null and" +
                                " a connected device could not be determined.");
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

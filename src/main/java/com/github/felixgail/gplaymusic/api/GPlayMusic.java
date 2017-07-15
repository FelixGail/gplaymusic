package com.github.felixgail.gplaymusic.api;

import com.github.felixgail.gplaymusic.api.exceptions.InitializationException;
import com.github.felixgail.gplaymusic.model.SongQuality;
import com.github.felixgail.gplaymusic.model.config.Config;
import com.github.felixgail.gplaymusic.api.exceptions.NetworkException;
import com.github.felixgail.gplaymusic.model.search.ResultType;
import com.github.felixgail.gplaymusic.model.search.SearchResponse;
import com.github.felixgail.gplaymusic.model.search.SearchTypes;
import com.github.felixgail.gplaymusic.model.shema.DeviceList;
import com.github.felixgail.gplaymusic.model.shema.Result;
import com.github.felixgail.gplaymusic.model.shema.Track;
import com.github.felixgail.gplaymusic.util.deserializer.ConfigDeserializer;
import com.github.felixgail.gplaymusic.util.deserializer.DeviceListDeserializer;
import com.github.felixgail.gplaymusic.util.deserializer.ResultDeserializer;
import com.github.felixgail.gplaymusic.util.interceptor.ErrorInterceptor;
import com.github.felixgail.gplaymusic.util.interceptor.ParameterInterceptor;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;
import svarzee.gps.gpsoauth.AuthToken;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public final class GPlayMusic {

    private GPlayService service;
    private Config config;

    private GPlayMusic(GPlayService service) {
        this.service  = service;
    }

    private void setConfig(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    /**
     * This method will return the service used to make calls to google play, and therefore allows for
     * low level and asynchronous calls. Be sure to check the response for error codes.
     * @return GPlayService object
     */
    public GPlayService getService() {
        return this.service;
    }

    public SearchResponse search(String query, int maxResults, SearchTypes types)
    throws IOException
    {
        return getService().search(query, maxResults, types).execute().body();
    }

    public SearchResponse search(String query, SearchTypes types)
    throws IOException
    {
        return search(query, 50, types);
    }

    public List<Track> searchTracks(String query, int maxResults)
        throws IOException
    {
        return search(query, maxResults, new SearchTypes(ResultType.TRACK)).getTracks();
    }

    public DeviceList getRegisteredDevices()
    throws IOException
    {
        return getService().getDevices().execute().body();
    }

    /**
     * Returns a URL to download the song in set quality.
     * URL will only be valid for 1 minute.
     * You will likely need to handle redirects.
     *
     * @param title title to download
     * @param quality quality of the stream
     * @return temporary url to the title
     * @throws IOException Throws an IOException on severe failures (no internet connection...)
     * or a NetworkException on request failures.
     */
    public String getTrackURL(Track title, SongQuality quality)
        throws IOException
    {
        Track.Signature sig = title.createSignature();
        return getService().getTrackLocation(getConfig().getAndroidID(),
                quality, sig.getSalt(), sig.getSignature(), title.getStoreId()
                ).execute().headers().get("Location");
    }

    public final static class Builder {

        private OkHttpClient.Builder httpClientBuilder;
        private AuthToken authToken;
        private Locale locale = Locale.US;
        private String androidID;
        private ErrorInterceptor.InterceptorBehaviour
                interceptorBehaviour = ErrorInterceptor.InterceptorBehaviour.THROW_EXCEPTION;

        public Builder setHttpClientBuilder(OkHttpClient.Builder builder) {
            this.httpClientBuilder = builder;
            return this;
        }

        public Builder setAuthToken(AuthToken token) {
            this.authToken = token;
            return this;
        }

        public Builder setLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public Builder setInterceptorBehaviour(ErrorInterceptor.InterceptorBehaviour interceptorBehaviour) {
            this.interceptorBehaviour = interceptorBehaviour;
            return this;
        }

        public Builder setAndroidID(String androidID) {
            this.androidID = androidID;
            return this;
        }

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

        private Interceptor getHeaderInterceptor(){
            return chain -> {
                final Request request = chain.request().newBuilder()
                        .addHeader("Authorization", "GoogleLogin auth=" + this.authToken.getToken())
                        .addHeader("Content-Type", "application/json")
                        .build();

                return chain.proceed(request);
            };
        }

        public GPlayMusic build() {
            try {
                if (this.authToken == null) {
                    throw new InitializationException("AuthToken is not allowed to be empty. Use TokenProvider.provideToken" +
                            " if you need to generate one.");
                }
                GsonBuilder gsonBuilder = new GsonBuilder()
                        .registerTypeAdapter(Result.class, new ResultDeserializer())
                        .registerTypeAdapter(Config.class, new ConfigDeserializer())
                        .registerTypeAdapter(DeviceList.class, new DeviceListDeserializer());

                if (this.httpClientBuilder == null) {
                    this.httpClientBuilder = getDefaultHttpBuilder();
                }

                ParameterInterceptor parameterInterceptor = new ParameterInterceptor();

                OkHttpClient httpClient = this.httpClientBuilder
                        .addInterceptor(getHeaderInterceptor())
                        .addInterceptor(new ErrorInterceptor(this.interceptorBehaviour))
                        .addInterceptor(parameterInterceptor)
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
                    config.setAndroidID(
                            gPlay.getService().getDevices()
                                    .execute().body().getFirstAndroid().getId());
                } else {
                    config.setAndroidID(androidID);
                }
                return gPlay;
            }catch (IOException e) {
                throw new InitializationException(e);
            }
        }

    }
}

package com.github.felixgail.gplaymusic.api;

import com.github.felixgail.gplaymusic.api.exceptions.InitializationException;
import com.github.felixgail.gplaymusic.model.config.Config;
import com.github.felixgail.gplaymusic.api.exceptions.NetworkException;
import com.github.felixgail.gplaymusic.model.shema.Result;
import com.github.felixgail.gplaymusic.util.deserializer.ConfigDeserializer;
import com.github.felixgail.gplaymusic.util.deserializer.ResultDeserializer;
import com.github.felixgail.gplaymusic.util.interceptor.ErrorInterceptor;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;
import svarzee.gps.gpsoauth.AuthToken;

import java.io.IOException;
import java.util.Collections;
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

    public final static class Builder {

        private OkHttpClient.Builder httpClientBuilder;
        private AuthToken authToken;
        private Locale locale = Locale.US;

        private ErrorInterceptor.InterceptorBehaviour interceptorBehaviour = ErrorInterceptor.InterceptorBehaviour.THROW_EXCEPTION;

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

        public void setInterceptorBehaviour(ErrorInterceptor.InterceptorBehaviour interceptorBehaviour) {
            this.interceptorBehaviour = interceptorBehaviour;
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

            OkHttpClient httpClient = this.httpClientBuilder
                    .addInterceptor(getHeaderInterceptor())
                    .addInterceptor(new ErrorInterceptor(this.interceptorBehaviour))
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://mclients.googleapis.com/sj/v2.5/")
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                    .client(httpClient)
                    .build();

            GPlayMusic gPlay = new GPlayMusic(retrofit.create(GPlayService.class));
            retrofit2.Response<Config> configResponse = null;
            try {
                configResponse = gPlay.getService().config(this.locale).execute();
                if (!configResponse.isSuccessful()) {
                    throw new InitializationException("Network returned an error:", NetworkException.parse(configResponse.errorBody().charStream()));
                }
            } catch (IOException e) {
                throw new InitializationException("Service Returned an error during initialization: "+ e.getMessage());
            }
            Config config = configResponse.body();
            if (config == null) {
                throw new InitializationException("Configuration Response empty.");
            }
            config.setLocale(locale);
            gPlay.setConfig(config);
            return gPlay;
        }

    }
}

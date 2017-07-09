package com.github.felixgail.gplaymusic.api;

import com.github.felixgail.gplaymusic.model.config.ConfigResponse;
import com.github.felixgail.gplaymusic.model.shema.Result;
import com.github.felixgail.gplaymusic.util.Deserializer.ConfigDeserializer;
import com.github.felixgail.gplaymusic.util.Deserializer.ResultDeserializer;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import svarzee.gps.gpsoauth.AuthToken;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.IOException;
import java.util.Collections;

public class GPlayMusic {

    private GPlayService service;

    public GPlayMusic(final AuthToken token){
        service = createService(createHttpClient(token));
    }

    public GPlayMusic(final String user, final String password, final String androidID)
        throws IOException, Gpsoauth.TokenRequestFailed {
        service = createService(createHttpClient(TokenProvider.provideToken(user, password, androidID)));
    }

    private static GPlayService createService(OkHttpClient httpClient) {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(Result.class, new ResultDeserializer())
                .registerTypeAdapter(ConfigResponse.class, new ConfigDeserializer());


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://mclients.googleapis.com/sj/v2.5/")
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .client(httpClient)
                .build();

        return retrofit.create(GPlayService.class);
    }

    private static OkHttpClient createHttpClient(final AuthToken token) {
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();
        Interceptor headerInterceptor = chain -> {
            final Request request = chain.request().newBuilder()
                    .addHeader("Authorization", "GoogleLogin auth=" + token.getToken())
                    .addHeader("Content-Type", "application/json")
                    .build();

            return chain.proceed(request);
        };

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(spec))
                .addInterceptor(headerInterceptor)
                .addInterceptor(loggingInterceptor)
                .build();
    }

    private Response call(Call c, boolean needsSubscription)
            throws IOException, BadRequestException {
        Response response = c.execute();
        if (!response.isSuccessful()) {
            throw new BadRequestException(Integer.toString(response.code()), c.request());
        }
        return response;
    }

    /**
     * This method will return the service used to make calls to google play, and therefore allows for
     * low level and asynchronous calls. Be sure to check for Exceptions yourself.
     * @return GPlayService object
     */
    public GPlayService getService() {
        return this.service;
    }
}

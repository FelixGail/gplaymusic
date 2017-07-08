package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.model.search.results.Result;
import com.github.felixgail.gplaymusic.util.ResultDeserializer;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import svarzee.gps.gpsoauth.AuthToken;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.IOException;
import java.util.Collections;

public class Api {

    public static GPlayService CreateService(final String user, final String password, final String androidID)
            throws IOException, Gpsoauth.TokenRequestFailed
    {
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();

        final AuthToken token = TokenProvider.provideToken(user, password, androidID);

        Interceptor headerInterceptor = new Interceptor() {
            public Response intercept(Chain chain) throws IOException {
                final Request request = chain.request().newBuilder()
                        .addHeader("Authorization", "GoogleLogin auth="+token.getToken())
                        .addHeader("Content-Type", "application/json")
                        .build();

                return chain.proceed(request);
            }
        };

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(spec))
                .addInterceptor(headerInterceptor)
                .addInterceptor(loggingInterceptor)
                .build();

        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(Result.class, new ResultDeserializer());


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://mclients.googleapis.com/sj/v2.5/")
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .client(httpClient)
                .build();

        return retrofit.create(GPlayService.class);
    }
}

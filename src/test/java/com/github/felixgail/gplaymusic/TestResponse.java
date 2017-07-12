package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.Before;
import svarzee.gps.gpsoauth.AuthToken;

import java.util.Locale;

public class TestResponse{

    @Before
    public void createClass()
    {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            GPlayMusic api = new GPlayMusic.Builder()
                    .setAuthToken(new AuthToken(""))
                    .setHttpClientBuilder(GPlayMusic.Builder.getDefaultHttpBuilder().addInterceptor(loggingInterceptor))
                    .setLocale(Locale.US)
                    .build();
    }

}

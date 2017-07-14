package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.api.TokenProvider;
import com.github.felixgail.gplaymusic.model.search.ResultType;
import com.github.felixgail.gplaymusic.model.search.SearchTypes;
import com.github.felixgail.gplaymusic.model.shema.Track;
import com.github.felixgail.gplaymusic.util.TestUtil;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.Test;
import svarzee.gps.gpsoauth.AuthToken;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TestResponse{

    @Test
    public void createClass()
    {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        try {
            AuthToken token;
            if (!TestUtil.PROPS.getProperty("auth.token").isEmpty()) {
                token = new AuthToken(TestUtil.PROPS.getProperty("auth.token"));
                System.out.printf("Using existing token: %s\n", token.getToken());
            } else {
                System.out.println("Asking for new Token.");
                token = TokenProvider.provideToken(
                        TestUtil.PROPS.getProperty("auth.username"),
                        TestUtil.PROPS.getProperty("auth.password"),
                        TestUtil.PROPS.getProperty("auth.android_id")
                );
                System.out.printf("New token: %s\n", token.getToken());
            }
            GPlayMusic api = new GPlayMusic.Builder()
                    .setAuthToken(token)
                    .setHttpClientBuilder(GPlayMusic.Builder.getDefaultHttpBuilder().addInterceptor(loggingInterceptor))
                    .setLocale(Locale.US)
                    .build();
            List<Track> songs = api.getService()
                    .search("Imagine", new SearchTypes(ResultType.TRACK),
                            api.getConfig()).execute().body().getTracks();
            Track.Signature sig = songs.get(0).createSignature();
            System.out.printf("Track: %s\nSig: %s\nSalt: %s\n", songs.get(0).getStoreId(),
                    sig.getSignature(), sig.getSalt());
        } catch (IOException | Gpsoauth.TokenRequestFailed e) {
            e.printStackTrace();
        }
    }

}

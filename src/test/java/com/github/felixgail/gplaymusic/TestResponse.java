package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.api.TokenProvider;
import com.github.felixgail.gplaymusic.model.SongQuality;
import com.github.felixgail.gplaymusic.model.search.ResultType;
import com.github.felixgail.gplaymusic.model.search.SearchTypes;
import com.github.felixgail.gplaymusic.model.shema.Track;
import com.github.felixgail.gplaymusic.util.TestUtil;
import com.github.felixgail.gplaymusic.util.interceptor.ErrorInterceptor;
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
                    .setLocale(Locale.US)
                    .setAndroidID(TestUtil.PROPS.getProperty("auth.android_id"))
                    .setInterceptorBehaviour(ErrorInterceptor.InterceptorBehaviour.LOG)
                    .build();
            List<Track> songs = api.getService()
                    .search("Tie a yellow ribbon",50, new SearchTypes(ResultType.TRACK))
                    .execute().body().getTracks();
            Track song = songs.get(0);
            Track.Signature sig = song.createSignature();
            System.out.printf("Track: %s\nSig: %s\nSalt: %s\nTitle: %s\nArtist: %s\n",
                    song.getStoreId(), sig.getSignature(), sig.getSalt(),
                    song.getTitle(), song.getArtist());
            String url = api.getTrackURL(song, SongQuality.HIGH);
            System.out.println(url);
        } catch (IOException | Gpsoauth.TokenRequestFailed e) {
            e.printStackTrace();
        }
    }

}

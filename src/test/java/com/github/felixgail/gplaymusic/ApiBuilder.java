package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.api.TokenProvider;
import com.github.felixgail.gplaymusic.util.TestUtil;
import org.junit.BeforeClass;
import svarzee.gps.gpsoauth.AuthToken;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.IOException;

public class ApiBuilder {

    @BeforeClass
    public static void loginToService() throws IOException, Gpsoauth.TokenRequestFailed {
        AuthToken token;
        if (TestUtil.TOKEN.isValid() && !TestUtil.TOKEN.get().isEmpty()) {
            System.out.println("Using existing token.");
            token = TokenProvider.provideToken(TestUtil.TOKEN.get());
        } else {
            System.out.println("New Token required.");
            TestUtil.assumeFilled(TestUtil.USERNAME, TestUtil.PASSWORD, TestUtil.ANDROID_ID);
            token = TokenProvider.provideToken(TestUtil.USERNAME.get(),
                    TestUtil.PASSWORD.get(), TestUtil.ANDROID_ID.get());
            TestUtil.set(TestUtil.TOKEN_KEY, token.getToken());
        }
        new GPlayMusic.Builder().setAuthToken(token).build();
    }

}

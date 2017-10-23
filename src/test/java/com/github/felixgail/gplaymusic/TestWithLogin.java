package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.exceptions.InitializationException;
import com.github.felixgail.gplaymusic.util.TestUtil;
import com.github.felixgail.gplaymusic.util.TokenProvider;
import okhttp3.OkHttpClient;
import svarzee.gps.gpsoauth.AuthToken;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TestWithLogin {

  static void loginToService(TestUtil.Property username, TestUtil.Property password,
                             TestUtil.Property androidID, TestUtil.Property token)
      throws IOException, Gpsoauth.TokenRequestFailed {
    AuthToken authToken;
    boolean usingExistingToken = false;
    if (token.isValid() && !token.get().isEmpty()) {
      System.out.println("Using existing token.");
      authToken = TokenProvider.provideToken(token.get());
      usingExistingToken = true;
    } else {
      TestUtil.assumeFilled(username, password, androidID);
      System.out.println("New Token required.");
      authToken = TokenProvider.provideToken(username.get(),
          password.get(), androidID.get());
      TestUtil.set(token.getKey(), authToken.getToken());
    }
    try {
      //Investigate: While cleanly running on pc. CircleCI will throw TimeOutExceptions. Increase readTimeout for now.
      OkHttpClient.Builder builder =
          GPlayMusic.Builder.getDefaultHttpBuilder().readTimeout(30, TimeUnit.SECONDS);
      new GPlayMusic.Builder()
          .setAuthToken(authToken)
          .setDebug(false)
          .setHttpClientBuilder(builder)
          .build();
    } catch (InitializationException e) {
      if (usingExistingToken) {
        //Retry as token could be invalid.
        TestUtil.set(token.getKey(), ""); //Reset Token
        loginToService(username, password, androidID, token);
      } else {
        throw e;
      }
    }
  }

}

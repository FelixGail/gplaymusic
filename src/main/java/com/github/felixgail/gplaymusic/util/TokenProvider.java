package com.github.felixgail.gplaymusic.util;

import okhttp3.OkHttpClient;
import svarzee.gps.gpsoauth.AuthToken;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.IOException;

public class TokenProvider {
  private static long lastTokenFetched;

  /**
   * Provides a token to access <b>Google Play Music</b>
   *
   * @param user      the username or email of the account <em>(e.g.: max.master@gmail.com)</em>
   * @param password  Password of the account. If you are using two-factor authentication,
   *                  you will need to create an app password
   *                  <a href="https://support.google.com/accounts/answer/185833">here</a>.
   * @param androidID An android ID connected with your google account.
   * @return an {@link svarzee.gps.gpsoauth.AuthToken}. This token does not expire, therefore changing the String
   * inside {@link svarzee.gps.gpsoauth.AuthToken#getToken()} could fasten your next startup.
   * @throws IOException                 on network or connectivity issues
   * @throws Gpsoauth.TokenRequestFailed on invalid credentials
   */
  public static AuthToken provideToken(final String user, final String password,
                                       final String androidID)
      throws IOException, Gpsoauth.TokenRequestFailed {
    lastTokenFetched = System.currentTimeMillis();
    OkHttpClient client = new OkHttpClient();
    Gpsoauth auth = new Gpsoauth(client);
    // clientSig taken from https://github.com/simon-weber/gmusicapi/blob/develop/gmusicapi/session.py#L199
    return auth.login(user, password, androidID, "sj", "com.google.android.music",
        "AIzaSyARTC1h-_puqO0PHCHUoj1BTDjuAOxNVA8");
  }

  /**
   * Generate a token from a saved one. Use this function with care, as PlayMusic tokens have no expiry date - but
   * can be invalidated if (for example) the same android id was used in the meantime to generate a new token.
   *
   * @param token Token string recovered from {@link AuthToken#getToken()};
   * @return a new Token, recovered from the saved string.
   */
  public static AuthToken provideToken(final String token) {
    return new AuthToken(token);
  }

  /**
   * Returns the time of the last {@link #provideToken(String, String, String)} call in Milliseconds.
   */
  public static long getLastTokenFetched() {
    return lastTokenFetched;
  }
}

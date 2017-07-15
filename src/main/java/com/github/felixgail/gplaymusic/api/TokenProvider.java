package com.github.felixgail.gplaymusic.api;

import okhttp3.OkHttpClient;
import svarzee.gps.gpsoauth.AuthToken;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.IOException;

public class TokenProvider {

    /**
     * Provides a token to access <b>Google Play Music</b>
     * @param user the username or email of the account <em>(e.g.: max.master@gmail.com)</em>
     * @param password Password of the account. If you are using two-factor authentication,
     *                 you will need to create an app password
     *                 <a href="https://support.google.com/accounts/answer/185833">here</a>.
     * @param androidID An android ID connected with your google account.
     * @return an {@link svarzee.gps.gpsoauth.AuthToken}. This token does not expire, therefore changing the String
     * inside {@link svarzee.gps.gpsoauth.AuthToken#getToken()} could fasten your next startup.
     * @throws IOException on network or connectivity issues
     * @throws Gpsoauth.TokenRequestFailed on invalid credentials
     */
    public static AuthToken provideToken(final String user, final String password, final String androidID)
            throws IOException, Gpsoauth.TokenRequestFailed {
        OkHttpClient client = new OkHttpClient();
        Gpsoauth auth = new Gpsoauth(client);
        // clientSig taken from https://github.com/simon-weber/gmusicapi/blob/develop/gmusicapi/session.py#L199
        return auth.login(user, password, androidID, "sj", "com.google.android.music",
                "AIzaSyARTC1h-_puqO0PHCHUoj1BTDjuAOxNVA8");
    }


}

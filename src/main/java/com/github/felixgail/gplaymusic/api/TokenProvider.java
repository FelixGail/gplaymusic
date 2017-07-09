package com.github.felixgail.gplaymusic.api;

import okhttp3.OkHttpClient;
import svarzee.gps.gpsoauth.AuthToken;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.IOException;

class TokenProvider {

    static AuthToken provideToken(final String user, final String password, final String androidID)
            throws IOException, Gpsoauth.TokenRequestFailed {
        OkHttpClient client = new OkHttpClient();
        Gpsoauth auth = new Gpsoauth(client);
        // clientSig taken from https://github.com/simon-weber/gmusicapi/blob/develop/gmusicapi/session.py#L199
        return auth.login(user, password, androidID, "sj", "com.google.android.music",
                "AIzaSyARTC1h-_puqO0PHCHUoj1BTDjuAOxNVA8");
    }


}

package com.github.felixgail.gplaymusic.api.exceptions;

import com.github.felixgail.gplaymusic.model.shema.NetworkError;
import okhttp3.Response;

import java.io.IOException;

public class ResponseException extends IOException{
    private Response response;
    private NetworkError networkError;

    public ResponseException(Response r, NetworkError e, String msg) {
        super(msg);
        response = r;
        networkError = e;
    }

    public Response getResponse() {
        return response;
    }

    public NetworkError getError() {
        return networkError;
    }
}

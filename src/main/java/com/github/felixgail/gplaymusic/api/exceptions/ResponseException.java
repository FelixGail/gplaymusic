package com.github.felixgail.gplaymusic.api.exceptions;

import com.github.felixgail.gplaymusic.model.shema.Error;
import okhttp3.Response;

import java.io.IOException;

public class ResponseException extends IOException{
    private Response response;
    private Error error;

    public ResponseException(Response r, Error e, String msg) {
        super(msg);
        response = r;
        error = e;
    }

    public Response getResponse() {
        return response;
    }

    public Error getError() {
        return error;
    }
}

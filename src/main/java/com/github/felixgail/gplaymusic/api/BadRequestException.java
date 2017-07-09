package com.github.felixgail.gplaymusic.api;

import okhttp3.Request;

public class BadRequestException extends IllegalArgumentException{

    private Request request;

    public BadRequestException(Request r) {
        super();
        request = r;
    }

    public BadRequestException(String msg, Request r){
        super(msg);
        request = r;
    }

    public Request getRequest() {
        return request;
    }
}

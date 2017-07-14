package com.github.felixgail.gplaymusic.api.exceptions;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import okhttp3.Call;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;

public class NetworkException extends IOException implements Serializable{

    private class ErrorHelper implements Serializable{
        @Expose
        private int code;
        @Expose
        private String message;

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    @Expose
    @SerializedName("error")
    private ErrorHelper helper = new ErrorHelper();

    private Call call;


    public int getCode() {
        return helper.getCode();
    }

    public String getMessage() {
        return helper.getMessage();
    }

    public Call getCall() {
        return call;
    }

    public void setCall(Call call) {
        this.call = call;
    }

    @Override
    public String toString() {
        return String.format("%d: %s", getCode(), getMessage());
    }

    private final static Gson gson = new Gson();
    public static NetworkException parse(Reader stream) {
        return gson.fromJson(stream, NetworkException.class);
    }
}

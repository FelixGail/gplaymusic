package com.github.felixgail.gplaymusic.api.exceptions;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import okhttp3.Call;
import okhttp3.Response;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;

/**
 * Will be thrown when the network responds with an error.
 * It will contain the error code and (if available) a message.
 */
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

        public void setCode(int code) {
            this.code = code;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @Expose
    @SerializedName("error")
    private ErrorHelper helper = new ErrorHelper();

    private Call call;

    public NetworkException(int code, String message){
        helper = new ErrorHelper();
        helper.setCode(code);
        helper.setMessage(message);
    }


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
    public static NetworkException parse(Response response) {
        try {
            return gson.fromJson(response.body().charStream(), NetworkException.class);
        } catch (JsonSyntaxException e) {
            return new NetworkException(response.code(), response.message());
        }
    }
}

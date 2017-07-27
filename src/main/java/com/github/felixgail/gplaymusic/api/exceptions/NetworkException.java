package com.github.felixgail.gplaymusic.api.exceptions;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Will be thrown when the network responds with an error.
 * It will contain the error code and (if available) a message.
 */
public class NetworkException extends IOException implements Serializable {

    private final static Gson gson = new Gson();
    @Expose
    @SerializedName("error")
    private ErrorHelper helper = new ErrorHelper();

    private Response response;

    public NetworkException(int code, String message) {
        helper = new ErrorHelper();
        helper.setCode(code);
        helper.setMessage(message);
    }

    public static NetworkException parse(Response response) {
        try {
            return gson.fromJson(response.body().charStream(), NetworkException.class);
        } catch (JsonSyntaxException e) {
            return new NetworkException(response.code(), response.message());
        }
    }

    public int getCode() {
        return helper.getCode();
    }

    public String getMessage() {
        return helper.getMessage();
    }


    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    private String getRequestInformation() {

        if (response != null) {
            Request request = response.request();
            Buffer buffer = new Buffer();
            buffer.writeUtf8("Request Information:\n")
                    .writeUtf8("URL: ").writeUtf8(request.url().toString()).writeUtf8(System.lineSeparator())
                    .writeUtf8("Method: ").writeUtf8(request.method()).writeUtf8(System.lineSeparator());
            for (Map.Entry<String, List<String>> entry :
                    request.headers().toMultimap().entrySet()){
                buffer.writeUtf8(entry.getKey()).writeUtf8(": ");
                for (String value : entry.getValue()) {
                    buffer.writeUtf8("\t");
                    if (value.startsWith("GoogleLogin")) {
                        buffer.writeUtf8("<ommited>");
                    } else {
                        buffer.writeUtf8(value);
                    }
                    buffer.writeUtf8(System.lineSeparator());
                }
            }
            if (request.body() != null) {
                try {
                    buffer.writeUtf8("\nRequest body:\n");
                    request.body().writeTo(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return buffer.readUtf8();
        }
        return "";
    }

    @Override
    public String toString() {
        return String.format("A NetworkException occured: \nError Code: %d \nMessage: %s \n\n%s\n",
                getCode(), getMessage(), getRequestInformation());
    }

    private class ErrorHelper implements Serializable {
        @Expose
        private int code;
        @Expose
        private String message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}

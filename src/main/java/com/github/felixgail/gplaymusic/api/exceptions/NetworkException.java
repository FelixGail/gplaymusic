package com.github.felixgail.gplaymusic.api.exceptions;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

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

    private Request request;

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

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    private String getRequestInformation() {
        if (request != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("More Request Information:\n")
                    .append("URL: ").append(request.url()).append(System.lineSeparator());
            for (Map.Entry<String, List<String>> entry :
                    request.headers().toMultimap().entrySet()){
                sb.append(entry.getKey()).append(": ");
                for (String value : entry.getValue()) {
                    sb.append("\t");
                    if (value.startsWith("GoogleLogin")) {
                        sb.append("<ommited>");
                    } else {
                        sb.append(value);
                    }
                    sb.append(System.lineSeparator());
                }
            }
            return sb.toString();
        }
        return "";
    }

    @Override
    public String toString() {
        return String.format("%d: %s\n\n%s", getCode(), getMessage(), getRequestInformation());
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

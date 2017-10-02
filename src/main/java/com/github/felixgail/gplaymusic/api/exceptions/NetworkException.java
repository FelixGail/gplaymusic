package com.github.felixgail.gplaymusic.api.exceptions;

import com.github.felixgail.gplaymusic.util.NetworkPrettyPrinter;
import com.github.felixgail.gplaymusic.util.language.Language;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import okhttp3.Response;

import java.io.IOException;
import java.io.Serializable;

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

  @Override
  public String getMessage() {
    return helper.getMessage();
  }


  public Response getResponse() {
    return response;
  }

  public NetworkException setResponse(Response response) {
    this.response = response;
    return this;
  }

  private String getRequestInformation() {

    if (response != null) {
      return NetworkPrettyPrinter.getRequestPrint(response.request());
    }
    return "";
  }

  @Override
  public String toString() {
    return String.format("%s:\nError Code: %d \nMessage: %s \n\n%s\n\n%s\n", Language.get("network.GenericError"),
        getCode(), getMessage(), getRequestInformation(), NetworkPrettyPrinter.getResponsePrint(response));
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

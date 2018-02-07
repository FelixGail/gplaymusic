package com.github.felixgail.gplaymusic.util.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import svarzee.gps.gpsoauth.AuthToken;

public class RequestInterceptor implements Interceptor {

  private final Map<String, String> parameters;
  private AuthToken token;

  public RequestInterceptor(@NotNull AuthToken token) {
    parameters = new HashMap<>();
    this.token = token;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request original = chain.request();
    HttpUrl originalHttpUrl = original.url();

    HttpUrl.Builder httpBuilder = originalHttpUrl.newBuilder();

    for (Map.Entry<String, String> entry : parameters.entrySet()) {
      httpBuilder.addQueryParameter(entry.getKey(), entry.getValue());
    }

    HttpUrl url = httpBuilder.build();

    // Request customization: add request headers
    Request request = original.newBuilder()
        .url(url)
        .addHeader("Authorization", "GoogleLogin auth=" + this.token.getToken())
        .addHeader("Content-Type", "application/json")
        .build();

    return chain.proceed(request);
  }

  public Map<String, String> getParameters() {
    return parameters;
  }

  public RequestInterceptor addParameter(String key, String value) {
    this.parameters.put(key, value);
    return this;
  }

  public RequestInterceptor removeParameter(String key) {
    this.parameters.remove(key);
    return this;
  }

  public RequestInterceptor setToken(@NotNull AuthToken token) {
    this.token = token;
    return this;
  }
}

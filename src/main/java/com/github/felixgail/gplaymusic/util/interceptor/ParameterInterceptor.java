package com.github.felixgail.gplaymusic.util.interceptor;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ParameterInterceptor implements Interceptor {

    private Map<String, String> parameters;

    public ParameterInterceptor() {
        parameters = new HashMap<>();
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
        Request.Builder requestBuilder = original.newBuilder()
                .url(url);

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public ParameterInterceptor setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    public ParameterInterceptor addParameter(String key, String value) {
        this.parameters.put(key, value);
        return this;
    }

    public ParameterInterceptor removeParameter(String key) {
        this.parameters.remove(key);
        return this;
    }
}

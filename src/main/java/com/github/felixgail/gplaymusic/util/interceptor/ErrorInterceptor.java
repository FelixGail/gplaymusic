package com.github.felixgail.gplaymusic.util.interceptor;

import com.github.felixgail.gplaymusic.api.exceptions.ResponseException;
import com.github.felixgail.gplaymusic.model.shema.Error;
import com.google.gson.Gson;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

public class ErrorInterceptor implements Interceptor{
    private Gson gson;

    public ErrorInterceptor() {
        gson = new Gson();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (!response.isSuccessful() && response.body() !=null) {
            Error error = gson.fromJson(response.body().charStream(), Error.class);
            throw new ResponseException(response,error, "");
        }
        return response;
    }
}

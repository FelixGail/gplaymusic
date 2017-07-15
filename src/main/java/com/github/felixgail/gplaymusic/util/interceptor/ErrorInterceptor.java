package com.github.felixgail.gplaymusic.util.interceptor;

import com.github.felixgail.gplaymusic.api.exceptions.NetworkException;
import com.google.gson.Gson;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;
import java.util.logging.Logger;

public class ErrorInterceptor implements Interceptor{
    private static final Logger logger = Logger.getLogger(ErrorInterceptor.class.getName());
    private Gson gson;
    private InterceptorBehaviour behaviour;

    public enum InterceptorBehaviour {
        LOG, THROW_EXCEPTION
    }

    public ErrorInterceptor(InterceptorBehaviour b) {
        this.behaviour = b;
        gson = new Gson();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (response.code() >= 400) {
            NetworkException networkException = NetworkException.parse(response);
            if (behaviour == InterceptorBehaviour.THROW_EXCEPTION) {
                throw networkException;
            } else {
                logger.warning(networkException.toString());
            }
        }
        return response;
    }
}

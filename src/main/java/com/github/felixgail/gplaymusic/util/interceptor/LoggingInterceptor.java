package com.github.felixgail.gplaymusic.util.interceptor;

import com.github.felixgail.gplaymusic.util.NetworkPrettyPrinter;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.logging.Logger;

public class LoggingInterceptor implements Interceptor {
    Logger logger = Logger.getGlobal();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        logger.info(String.format("%s\n\n%s", NetworkPrettyPrinter.getRequestPrint(request),
                NetworkPrettyPrinter.getResponsePrint(response)));
        return response;
    }
}

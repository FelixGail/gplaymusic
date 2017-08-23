package com.github.felixgail.gplaymusic.util;

import com.github.felixgail.gplaymusic.util.language.Language;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class NetworkPrettyPrinter {

    public static String getRequestPrint(Request request) {
        if (request != null) {
            Buffer buffer = new Buffer();
            buffer.writeUtf8("Request Information\n")
                    .writeUtf8("URL: ").writeUtf8(request.url().toString()).writeUtf8(System.lineSeparator())
                    .writeUtf8("Method: ").writeUtf8(request.method()).writeUtf8(System.lineSeparator());
            for (Map.Entry<String, List<String>> entry :
                    request.headers().toMultimap().entrySet()) {
                buffer.writeUtf8(entry.getKey()).writeUtf8(": ");
                for (String value : entry.getValue()) {
                    buffer.writeUtf8("\t");
                    if (value.startsWith("GoogleLogin")) {
                        buffer.writeUtf8(Language.get("network.print.Ommited"));
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
                } catch (IOException | NullPointerException e) {
                    buffer.writeUtf8("\n\n").writeUtf8(Language.get("exception.Generic"))
                            .writeUtf8(e.getMessage());
                }
            }
            return buffer.readUtf8();
        }
        return "";
    }

    public static String getResponsePrint(Response response) {
        if (response != null) {
            Buffer buffer = new Buffer();
            buffer.writeUtf8("Response Information:\n");
            buffer.writeUtf8("Code: ").writeUtf8(String.valueOf(response.code()));
            buffer.writeUtf8("\nHeaders:\n");
            response.headers().toMultimap().forEach((key, value) -> {
                buffer.writeUtf8("\t").writeUtf8(key).writeUtf8(":\n");
                value.forEach(v -> buffer.writeUtf8("\t\t").writeUtf8(v).writeUtf8("\n"));
            });
            buffer.writeUtf8("\n\nBody:\n");
            try {
                BufferedSource source = response.body().source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                String body = source.buffer().clone().readUtf8();
                buffer.writeUtf8(body);
            } catch (IOException | NullPointerException e) {
                buffer.writeUtf8("\n\n").writeUtf8(Language.get("exception.Generic"))
                        .writeUtf8(e.getMessage());
            }
            return buffer.readUtf8();
        }
        return "";
    }
}

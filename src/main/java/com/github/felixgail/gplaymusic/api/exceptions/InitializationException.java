package com.github.felixgail.gplaymusic.api.exceptions;

public class InitializationException extends RuntimeException{

    public InitializationException(String msg, NetworkException e) {
        super(String.format("%s:\n%s", msg, e));
    }

    public InitializationException(String msg) {
        super(msg);
    }
}

package com.github.felixgail.gplaymusic.api.exceptions;

import com.github.felixgail.gplaymusic.model.shema.Error;

public class InitializationException extends RuntimeException{

    public InitializationException(String msg, Error e) {
        super(String.format("%s:\n%s", msg, e));
    }

    public InitializationException(String msg) {
        super(msg);
    }
}

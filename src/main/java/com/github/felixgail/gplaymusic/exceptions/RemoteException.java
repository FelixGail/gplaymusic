package com.github.felixgail.gplaymusic.exceptions;

public class RemoteException extends RuntimeException {
    public RemoteException(String msg) {
        super(msg);
    }

    public RemoteException(Exception e) {
        super(e);
    }
}

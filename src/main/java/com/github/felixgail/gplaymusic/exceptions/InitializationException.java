package com.github.felixgail.gplaymusic.exceptions;

/**
 * Will be thrown during initialization of {@link com.github.felixgail.gplaymusic.api.GPlayMusic}
 * if an error occurs.
 */
public class InitializationException extends RuntimeException {

  public InitializationException(String msg, NetworkException e) {
    super(String.format("%s:\n%s", msg, e));
  }

  public InitializationException(String msg) {
    super(msg);
  }

  public InitializationException(Exception e) {
    super(e);
  }
}

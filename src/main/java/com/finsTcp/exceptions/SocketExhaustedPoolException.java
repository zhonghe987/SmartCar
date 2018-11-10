package com.FinsTCP.exceptions;

/**
 * This exception will be thrown when the Jedis client isn't able to retrieve a connection from the
 * pool, since all the connections are being used (a.k.a. an "exhausted" pool).
 */
public class SocketExhaustedPoolException extends SocketException {

  public SocketExhaustedPoolException(String message) {
    super(message);
  }

  public SocketExhaustedPoolException(Throwable e) {
    super(e);
  }

  public SocketExhaustedPoolException(String message, Throwable cause) {
    super(message, cause);
  }
}
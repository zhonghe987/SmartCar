package com.FinsTCP.exceptions;

public class SocketConnectionException extends SocketException {
  private static final long serialVersionUID = 3878126572474819403L;

  public SocketConnectionException(String message) {
    super(message);
  }

  public SocketConnectionException(Throwable cause) {
    super(cause);
  }

  public SocketConnectionException(String message, Throwable cause) {
    super(message, cause);
  }
}
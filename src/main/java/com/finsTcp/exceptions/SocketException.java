package com.FinsTCP.exceptions;

public class SocketException extends RuntimeException {
  private static final long serialVersionUID = -2946266495682282677L;

  public SocketException(String message) {
    super(message);
  }

  public SocketException(Throwable e) {
    super(e);
  }

  public SocketException(String message, Throwable cause) {
    super(message, cause);
  }
}
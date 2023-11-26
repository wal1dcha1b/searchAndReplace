package com.infor.exceptions;

public class LibraryException extends RuntimeException {

  private static final long serialVersionUID = 20220414L;

  public LibraryException() {
    super();
  }

  public LibraryException(String message) {
    super(message);
  }

  public LibraryException(Throwable cause) {
    super(cause);
  }

  public LibraryException(String message, Throwable cause) {
    super(message, cause);
  }
}

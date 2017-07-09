package com.mehdi.deezertechnicaltest.data.exception;

/**
 * Exception throw when an error occur during data parsing.
 */
public class ParsingException extends Exception {

  public ParsingException(String message) {
    super(message);
  }

  public ParsingException(Throwable cause) {
    super(cause);
  }
}

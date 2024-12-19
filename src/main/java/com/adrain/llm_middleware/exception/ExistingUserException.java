package com.adrain.llm_middleware.exception;

public class ExistingUserException extends RuntimeException {

  public ExistingUserException(String message) {
    super(message);
  }

}

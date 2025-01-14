package com.adrain.llm_middleware.exception;

public class ResponseNotFoundException extends RuntimeException {

  public ResponseNotFoundException(String message) {
    super(message);
  }
  
}

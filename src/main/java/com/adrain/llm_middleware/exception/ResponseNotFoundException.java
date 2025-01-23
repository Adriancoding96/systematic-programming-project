package com.adrain.llm_middleware.exception;

import com.adrain.llm_middleware.model.Response;

/**
 * Exception thrown when a requested {@link Response} is not found in the database.
 * This exception extends {@link RuntimeException}.
 *
 * @see RuntimeException
 */
public class ResponseNotFoundException extends RuntimeException {

  public ResponseNotFoundException(String message) {
    super(message);
  }
  
}

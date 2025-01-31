package com.adrain.llm_middleware.exception;

/**
 * Exception thrown when a requested {@link User} is not found in the database.
 * This exception extends {@link RuntimeException}.
 *
 * @see RuntimeException
 */
public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String message) {
    super(message);
  }
  
}

package com.adrain.llm_middleware.exception;

/**
 * Exception thrown when an request attempts to create or register a {@link User} that already exists.
 * This exception extends {@link RuntimeException} and is used to indicate that a user with the same
 * identifier already exists in the database.
 *
 * @see RuntimeException
 */
public class ExistingUserException extends RuntimeException {

  public ExistingUserException(String message) {
    super(message);
  }

}

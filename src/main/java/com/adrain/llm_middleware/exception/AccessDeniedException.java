package com.adrain.llm_middleware.exception;

/**
 * Exception thrown when access to a resource is denied due to insufficient permissions or authorization.
 * This exception extends {@link RuntimeException} and is used when a {@link User} does not have
 * the necessary privileges to perform a specific operation.
 *
 * @see RuntimeException
 */
public class AccessDeniedException extends RuntimeException {

  public AccessDeniedException(String message) {
    super(message);
  }

}

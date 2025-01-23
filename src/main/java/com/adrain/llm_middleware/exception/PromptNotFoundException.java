package com.adrain.llm_middleware.exception;

import com.adrain.llm_middleware.model.Prompt;

public class PromptNotFoundException extends RuntimeException {

/**
 * Exception thrown when a requested {@link Prompt} is not found in the database.
 * This exception extends {@link RuntimeException}.
 *
 * @see RuntimeException
 */
  public PromptNotFoundException(String message) {
    super(message);
  }
  
}

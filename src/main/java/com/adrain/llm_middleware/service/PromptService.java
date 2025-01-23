package com.adrain.llm_middleware.service;

import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.record.prompt.PromptRequest;
import com.adrain.llm_middleware.record.prompt.PromptResponse;

/**
 * Service interface for handling {@link Prompt} related requests.
 * This interface defines a method for creating a new {@link Prompt} based on the passed http request.
 *
 * @see PromptResponse
 * @see PromptRequest
 */
public interface PromptService {


  /**
   * Creates a new {@link Prompt} based on the http request.
   *
   * @param request the request containing the details for the new {@link Prompt}
   * @return the response containing the result of the {@link Prompt} creation
   */
  PromptResponse newPrompt(PromptRequest request);
  
}

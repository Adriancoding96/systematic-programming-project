package com.adrain.llm_middleware.service;

import java.util.List;

import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.model.Response;
import com.adrain.llm_middleware.record.prompt.PromptRequest;
import com.adrain.llm_middleware.record.prompt.PromptResponse;
import com.adrain.llm_middleware.record.response.ResponseRecord;

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

  /**
   * Fetches all {@link Response}s as {@link ResponseRecord} from datavase.
   *
   * @return a list of {@link ResponseRecord}s mapped from {@link Response} entities.
   */
  List<PromptResponse> getAllResponses();

  /**
   * Fetches {@link Response} from the database by id amd returns it as {@link ResponseRecord}.
   *
   * @return a fethced {@link Response} as {@link ResponseRecord}.
   */
  ResponseRecord getResponseById(Long id);


  /**
   * Deletes {@link Response} from the database.
   */
  void deleteResponseById(Long id);
  
}

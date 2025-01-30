package com.adrain.llm_middleware.service;

import java.util.List;

import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.record.prompt.PromptRecord;
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

  /**
   * Fetches all {@link Prompt}s as {@link PromptRecord} from database.
   *
   * @return a list of {@link PromptRecord}s mapped from {@link Prompt} entities.
   */
  List<PromptRecord> getAllPrompts();

  /**
   * Fetches all {@link Prompt}s as {@link PromptRecord} from database by {@link User} email.
   *
   * @param email the email of the end user.
   * @return a list of {@link PromptRecord}s mapped from {@link Prompt} entities.
   */
  List<PromptRecord> getAllPromptsByUserEmail(String email);

  /**
   * Fetches {@link Prompt} from the database by id amd returns it as {@link PromptRecord}.
   *
   * @return a fethced {@link Prompt} as {@link PromptRecord}.
   */
  PromptRecord getPromptById(Long id);


  /**
   * Updates {@link Prompt} using id and {@link PromptRecord}.
   *
   * @param id the id of the existing {@link Prompt}
   * @param record the dto containing the update data.
   */
  void updatePrompt(Long id, PromptRecord record);

  /**
   * Deletes {@link Prompt} from the database.
   */
  void deletePromptById(Long id);
  
}

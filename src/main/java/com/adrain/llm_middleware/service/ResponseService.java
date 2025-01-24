package com.adrain.llm_middleware.service;

import java.util.List;

import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.model.Response;
import com.adrain.llm_middleware.model.User;
import com.adrain.llm_middleware.record.response.ResponseRecord;

/**
 * Service interface for handling {@link Response} related http requests.
 * This interface defines methods for creating, retrieving, searching, and deleting {@link Response}s.
 *
 * @see ResponseRecord
 * @see Response
 */
public interface ResponseService {

  /**
   * Creates a new {@link Response} based on the provided {@link ResponseRecord}.
   *
   * @param record the record containing the details for the new {@link Response}
   */
  void newResponse(ResponseRecord record);

  /**
   * Retrieves all {@link Response}s from database.
   *
   * @return a list of {@link ResponseRecord}
   */
  List<ResponseRecord> getAllResponses();

  /**
   * Retrieves all {@link Response}s associated with the current {@link User} email.
   *
   * @return a list of {@link ResponseRecord} objects associated with the current {@link User}
   */
  List<ResponseRecord> getAllResponsesByUserEmail();

  /**
   * Searches for {@link Response}s based on a substring of the response body and the
   * current {@link User} email.
   *
   * @param responseBody the substring of the response body to search for
   * @return a list of {@link ResponseRecord} objects with a response body matching substring
   */
  List<ResponseRecord> findResponsesByResponseBodyAndUserEmail(String responseBody);

  /**
   * Retrieves a {@link Respose} by its id.
   *
   * @param id the id response
   * @return the {@link ResponseRecord} with the specified id
   */
  ResponseRecord getResponseById(Long id);

  /**
   * Retrieves a {@link Response} by the id of its realted {@link Prompt}.
   *
   * @param id the id of the {@link Prompt}
   * @return the {@link Response} associated with the specified {@link Prompt} id
   */
  Response getResponseByPromptId(Long id);

  /**
   * Deletes a {@link Response} by its unique identifier.
   *
   * @param id the id of the {@link Repsonse} to delete
   */
  void deleteResponseById(Long id);
}

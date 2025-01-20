package com.adrain.llm_middleware.service.impl;

import java.util.List;

import com.adrain.llm_middleware.exception.ResponseNotFoundException;
import com.adrain.llm_middleware.mapper.ResponseMapper;
import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.model.Response;
import com.adrain.llm_middleware.record.response.ResponseRecord;
import com.adrain.llm_middleware.repository.ResponseRepository;
import com.adrain.llm_middleware.security.AuthenticationFacade;
import com.adrain.llm_middleware.service.ResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ResponseServiceImpl is the implementation of {@link ResponseService} that handles CRUD operations for {@link Response}
 * entities in the database. It facilitates creating, retrieving, and deleting responses associated with a user or prompt.
 * <p>
 *     Utilizes {@link ResponseRepository}, {@link ResponseMapper}, and {@link AuthenticationFacade} for
 *     repository operations, mapping between entities and records, and authentication context retrieval respectively.
 * </p>
 */
@Service
public class ResponseServiceImpl implements ResponseService {

  private final ResponseRepository responseRepository;
  private final ResponseMapper responseMapper;
  private final AuthenticationFacade authenticationFacade;

  @Autowired
  public ResponseServiceImpl(ResponseRepository repository, ResponseMapper mapper, AuthenticationFacade authenticationFacade) {
    this.responseRepository = repository;
    this.responseMapper = mapper;
    this.authenticationFacade = authenticationFacade;
  }

   /**
   * Saves a new {@link Response} entity to the database.
   * <p>
   *     Maps the provided {@link ResponseRecord} to a {@link Response} entity and persists it in the database.
   * </p>
   *
   * @param record The {@link ResponseRecord} containing the response data to be saved.
   */
  public void newResponse(ResponseRecord record) {
    responseRepository.save(responseMapper.toResponse(record));
  }

  /**
   * Retrieves all {@link ResponseRecord} objects associated with the authenticated user.
   * <p>
   *     Fetches the users email from the authentication context and retrieves all corresponding responses from the database.
   *     Converts each {@link Response} entity to a {@link ResponseRecord}.
   * </p>
   *
   * @return A {@link List} of {@link ResponseRecord} objects associated with the authenticated user.
   */
  public List<ResponseRecord> getAllResponsesByUserEmail() {
    /*
    String email = authenticationFacade.getAuthentication().getName();
    List<Response> responses = repository.findAllByUserEmail(email); 
    return responses.stream()
      .map(mapper::toRecord)
      .collect(Collectors.toList());
      */
    return null;
  }

  /**
   * Retrieves a {@link ResponseRecord} by its id.
   * <p>
   *     Fetches a {@link Response} entity from the database using its id and converts it to a {@link ResponseRecord}.
   *     Throws a {@link ResponseNotFoundException} if the entity is not found.
   * </p>
   *
   * @param id The ID of the {@link Response} to be retrieved.
   * @return The corresponding {@link ResponseRecord}.
   * @throws ResponseNotFoundException if no {@link Response} with the given ID is found.
   */
  public ResponseRecord getResponseById(Long id) {
    Response response = responseRepository.findById(id).
      orElseThrow(() -> new ResponseNotFoundException("Could not find response in database with id: " + id));
    return responseMapper.toRecord(response); 
  }

  /**
   * Retrieves a {@link Response} by its associated {@link Prompt} id.
   * <p>
   *     Fetches a {@link Response} entity from the database using the id of its related {@link Prompt}.
   *     Throws a {@link ResponseNotFoundException} if the entity is not found.
   * </p>
   *
   * @param id The if of the {@link Prompt} associated with the {@link Response}.
   * @return The mapped {@link Response} entity.
   * @throws ResponseNotFoundException if no {@link Response} associated with the given {@link Prompt} id is found.
   */
  @Override
  public Response getResponseByPromptId(Long id) {
    return responseRepository.findByPromptId(id)
      .orElseThrow(() -> new ResponseNotFoundException("Could not find response in database related to a prompt with id: " + id));
  }

  /**
   * Deletes a {@link Response} entity from the database by its id.
   * <p>
   *     Removes the {@link Response} entity with the specified id from the database.
   * </p>
   *
   * @param id The id of the {@link Response} to be deleted.
   */
  public void deleteResponseById(Long id) {
    responseRepository.deleteById(id);
  }
}

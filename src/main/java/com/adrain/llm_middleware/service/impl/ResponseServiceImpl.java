package com.adrain.llm_middleware.service.impl;

import java.util.List;

import com.adrain.llm_middleware.exception.ResponseNotFoundException;
import com.adrain.llm_middleware.mapper.ResponseMapper;
import com.adrain.llm_middleware.model.Response;
import com.adrain.llm_middleware.record.response.ResponseRecord;
import com.adrain.llm_middleware.repository.ResponseRepository;
import com.adrain.llm_middleware.security.AuthenticationFacade;
import com.adrain.llm_middleware.service.ResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResponseServiceImpl implements ResponseService {

  private final ResponseRepository repository;
  private final ResponseMapper mapper;
  private final AuthenticationFacade authenticationFacade;

  @Autowired
  public ResponseServiceImpl(ResponseRepository repository, ResponseMapper mapper, AuthenticationFacade authenticationFacade) {
    this.repository = repository;
    this.mapper = mapper;
    this.authenticationFacade = authenticationFacade;
  }

  /*
   * Save response sent from client, method simply maps the record
   * to a entity and saves it to database.
   *
   * @param record: contains response data.
   * */
  public void newResponse(ResponseRecord record) {
    repository.save(mapper.toResponse(record));
  }

  /*
   * Fetches all responses by user email. Email is provided
   * through api authentication context.
   *
   * @return responseRecords: contains records coneverted from responses
   * */
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

  /*
   * Fetchers response by id from database, converts it to a record and
   * returns it to calling method.
   *
   * @param id: id of table row.
   * @returns record: returns response record.
   * @throws: throws a runtime exception if optional is empty.
   * */
  public ResponseRecord getResponseById(Long id) {
    Response response = repository.findById(id).
      orElseThrow(() -> new ResponseNotFoundException("Could not find response in database with id: " + id));
    return mapper.toRecord(response); 
  }

  @Override
  public Response getResponseByPromptId(Long id) {
    return repository.findByPromptId(id)
      .orElseThrow(() -> new ResponseNotFoundException("Could not find response in database related to a prompt with id: " + id));
  }

  /*
   * Deletes table row by id.
   *
   * @param id: id of response table row.
   * */
  public void deleteResponseById(Long id) {
    repository.deleteById(id);
  }
}

package com.adrain.llm_middleware.service.impl;

import java.util.List;

import com.adrain.llm_middleware.mapper.ResponseMapper;
import com.adrain.llm_middleware.model.Response;
import com.adrain.llm_middleware.record.response.ResponseRecord;
import com.adrain.llm_middleware.repository.ResponseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResponseServiceImpl {

  private final ResponseRepository repository;
  private final ResponseMapper mapper;


  @Autowired
  public ResponseServiceImpl(ResponseRepository repository, ResponseMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  /*
   * Save response sent from client, method simply maps the record
   * to a entity and saves it to database.
   *
   * @param record: contains response data.
   * */
  void newResponse(ResponseRecord record) {
    repository.save(mapper.toResponse(record));
  }

  //TODO implement authorization web context before implementing this method.
  List<ResponseRecord> getAllResponsesByUserId() {
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
  ResponseRecord getResponseById(Long id) {
    Response response = repository.findById(id).
      orElseThrow(() -> new RuntimeException("Could not find response in database with id: " + id));
    return mapper.toRecord(response); 
  }

  /*
   * Deletes table row by id.
   *
   * @param id: id of response table row.
   * */
  void deleteResponseById(Long id) {
    repository.deleteById(id);
  }
}

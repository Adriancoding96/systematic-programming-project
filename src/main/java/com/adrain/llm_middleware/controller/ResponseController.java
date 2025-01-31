package com.adrain.llm_middleware.controller;

import java.util.List;

import com.adrain.llm_middleware.model.Response;
import com.adrain.llm_middleware.model.User;
import com.adrain.llm_middleware.record.response.ResponseRecord;
import com.adrain.llm_middleware.service.ResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller responsible for handling {@link Response} requests.
 * This controller provides endpoints for creating, retrieving, searching, and deleting
 * responses.
 *
 * @see RestController
 * @see ResponseService
 * @see ResponseRecord
 */
@RestController
@RequestMapping("/api/response")
public class ResponseController {

  private final ResponseService service;

  @Autowired
  public ResponseController(ResponseService service) {
    this.service = service;
  }

  /**
   * Handles requests to create a new {@link Response}.
   *
   * @param record the response data to be persisted
   * @return a {@link ResponseEntity} with HTTP status 200 (OK) upon successful creation
   */
  @PostMapping("/new")
  public ResponseEntity<Void> newResponse(@RequestBody ResponseRecord record) {
    service.newResponse(record);
    return ResponseEntity.ok().build();
  }

  /**
   * Retrieves all {@link Response} mapped as {@link ResponseRecord}
   * from the database.
   *
   * @return a {@link ResponseEntity} containing a list of {@link ResponseRecord} objects
   */
  @GetMapping
  public ResponseEntity<List<ResponseRecord>> getAllResponses() {
    List<ResponseRecord> responses = service.getAllResponses();
    return ResponseEntity.ok(responses);
  }

  /**
   * Retrieves all {@link Response} mapped as {@link ResponseRecord}
   * associated with the current {@link User}.
   *
   * @return a {@link ResponseEntity} containing a list of {@link ResponseRecord} objects
   */
  @GetMapping("/user")
  public ResponseEntity<List<ResponseRecord>> getAllResponsesByUser() {
    List<ResponseRecord> responses = service.getAllResponsesByUserEmail();
    return ResponseEntity.ok(responses);
  }

  /**
   * Searches for {@link Response} based on a query string.
   *
   * @param query the search query string
   * @return a {@link ResponseEntity} containing a list of {@link ResponseRecord} objects matching the query
   */
  @GetMapping("/search")
  public ResponseEntity<List<ResponseRecord>> searchResponses(@RequestParam String query) {
    List<ResponseRecord> responses = service.findResponsesByResponseBodyAndUserEmail(query);
    return ResponseEntity.ok(responses);
  }

  /**
   * Retrieves a @link {@link Respone} by its unique identifier.
   *
   * @param id the primary key of the response row
   * @return a {@link ResponseEntity} containing the {@link ResponseRecord} with the specified id
   */
  @GetMapping("/{id}")
  public ResponseEntity<ResponseRecord> getResponseById(@PathVariable Long id) {
    ResponseRecord response = service.getResponseById(id);
    return ResponseEntity.ok(response);
  }

  /**
   * Updates a @link {@link Respone} by its unique identifier and {@link ResponseRecord}.
   *
   * @param id the primary key of the response row
   * @param record the dto containing update data
   * @return a {@link ResponseEntity} containing the {@link ResponseRecord} with the specified id
   */
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateResponse(@PathVariable Long id, @RequestBody ResponseRecord record) {
    service.updateResponse(id, record);
    return ResponseEntity.ok().build();
  }



  /**
   * Deletes a @link {@link Respone} by its unique identifier.
   *
   * @param id the primary key of the response row
   * @return a {@link ResponseEntity} with HTTP status 200 (OK) upon successful deletion
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteResponseById(@PathVariable Long id) {
    service.deleteResponseById(id);
    return ResponseEntity.ok().build();
  }
}

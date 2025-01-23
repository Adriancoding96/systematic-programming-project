package com.adrain.llm_middleware.controller;

import java.util.List;

import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.record.prompt.PromptRecord;
import com.adrain.llm_middleware.record.prompt.PromptRequest;
import com.adrain.llm_middleware.record.prompt.PromptResponse;
import com.adrain.llm_middleware.service.PromptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller responsible for handling {@link Prompt} requests.
 * This controller provides endpoints for creating, retrieving, searching, and deleting
 * {@link Prompt}s.
 *
 * @see RestController
 * @see PromptService 
 * @see PromptRequest
 * @see PromptRecord
 */
@RestController
@RequestMapping("/api/prompt")
public class PromptController {

  private final PromptService promptService;

  @Autowired
  public PromptController(PromptService promptService) {
    this.promptService = promptService;
  }


  /**
   * Handles requests to create a new {@link Prompt}.
   *
   * @param record the {@link Prompt} request record {@link PromptRequest} containig 
   * {@link Prompt} data.
   * @return a {@link PromptResponse} with HTTP status 200 (OK) upon successful creation
   */
  @PostMapping("/new")
  public ResponseEntity<PromptResponse> newPrompt(@RequestBody PromptRequest request) {
    return ResponseEntity.ok(promptService.newPrompt(request)); 
  }

  /**
   * Handles requests to fetch all {@link Prompt} entities from database.
   *
   * @return a list of {@link PromptRecord} with HTTP status 200 (OK) upon successful creation
   */
  @GetMapping
  public ResponseEntity<List<PromptRecord>> getAllPrompts() {
    return ResponseEntity.ok(promptService.getAllPrompts());
  }

  /**
   * Handles requests to fetch a {@link Prompt} by id.
   *
   * @return a {@link PromptRecord} with HTTP status 200 (OK) upon successful creation
   */
  @GetMapping("/{id}")
  public ResponseEntity<PromptRecord> getPromptById(@PathVariable Long id) {
    return ResponseEntity.ok(promptService.getPromptById(id));
  }

  /**
   * Handles requests to delete a {@link Prompt} by id.
   *
   * @return a {@code Void} response with HTTP status 200 (OK) upon successful creation
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePromptById(@PathVariable Long id) {
    promptService.deletePromptById(id);
    return ResponseEntity.ok().build();
  }

  

}

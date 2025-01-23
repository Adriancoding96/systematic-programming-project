package com.adrain.llm_middleware.controller;

import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.record.prompt.PromptRecord;
import com.adrain.llm_middleware.record.prompt.PromptRequest;
import com.adrain.llm_middleware.record.prompt.PromptResponse;
import com.adrain.llm_middleware.service.PromptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

  

}

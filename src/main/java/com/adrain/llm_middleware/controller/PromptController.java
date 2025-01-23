package com.adrain.llm_middleware.controller;

import com.adrain.llm_middleware.model.Prompt;
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
 * REST controller for handling {@link Prompt} requests.
 * This controller provides endpoints for creating new prompts.
 *
 * @see RestController
 * @see PromptService
 * @see PromptRequest
 * @see PromptResponse
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
   * This method accepts a {@link PromptRequest} and 
   * returning a {@link PromptResponse} on completion.
   *
   * @param request the request containing the prompt details
   * @return a {@link ResponseEntity} containing the {@link PromptResponse} with the result
   */
  @PostMapping("/new")
  public ResponseEntity<PromptResponse> newPrompt(@RequestBody PromptRequest request) {
    return ResponseEntity.ok(promptService.newPrompt(request)); 
  }

}

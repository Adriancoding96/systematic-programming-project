package com.adrain.llm_middleware.controller;

import com.adrain.llm_middleware.record.Prompt.PromptRequest;
import com.adrain.llm_middleware.record.Prompt.PromptResponse;
import com.adrain.llm_middleware.service.PromptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prompt")
public class PromptController {

  private final PromptService promptService;

  @Autowired
  public PromptController(PromptService promptService) {
    this.promptService = promptService;
  }

  @PostMapping("/new")
  public ResponseEntity<PromptResponse> newPrompt(@RequestBody PromptRequest request) {
    return ResponseEntity.ok(promptService.newPrompt(request)); 
  }

}

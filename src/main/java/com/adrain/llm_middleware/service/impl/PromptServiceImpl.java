package com.adrain.llm_middleware.service.impl;

import com.adrain.llm_middleware.api.OpenAiClient;
import com.adrain.llm_middleware.repository.PromptRepository;
import com.adrain.llm_middleware.service.PromptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromptServiceImpl implements PromptService {

  private final PromptRepository promptRepository;
  private final OpenAiClient openAiClient;

  @Autowired
  public PromptServiceImpl(PromptRepository promptRepository, OpenAiClient openAiClient) {
    this.promptRepository = promptRepository;
    this.openAiClient = openAiClient;
  } 
  
}

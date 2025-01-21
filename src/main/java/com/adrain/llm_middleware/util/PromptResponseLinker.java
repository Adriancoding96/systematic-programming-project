package com.adrain.llm_middleware.util;

import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.repository.PromptRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//TODO Most likely temporary need to improve service layer structure,
//This class is to temporarly avoid circular dependencies between
//PromptService and ResponseService
@Component
public class PromptResponseLinker {

  private PromptRepository promptRepository;

  @Autowired
  public PromptResponseLinker(PromptRepository promptRepository){
    this.promptRepository = promptRepository;
  }

  public Prompt getPromptById(Long id) {
    return promptRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Could not find prompt with id: " + id));
  } 
  
}

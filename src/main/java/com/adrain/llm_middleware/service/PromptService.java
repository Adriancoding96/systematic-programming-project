package com.adrain.llm_middleware.service;

import com.adrain.llm_middleware.record.prompt.PromptRequest;
import com.adrain.llm_middleware.record.prompt.PromptResponse;

public interface PromptService {

  PromptResponse newPrompt(PromptRequest request);
  
}

package com.adrain.llm_middleware.service;

import com.adrain.llm_middleware.record.Prompt.PromptRequest;
import com.adrain.llm_middleware.record.Prompt.PromptResponse;
import com.adrain.llm_middleware.record.api.OpenAiResponse;

public interface PromptService {

  PromptResponse newPrompt(PromptRequest request);

  OpenAiResponse getResponse(String prompt);
  
}

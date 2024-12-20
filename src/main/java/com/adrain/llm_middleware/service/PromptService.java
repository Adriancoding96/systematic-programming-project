package com.adrain.llm_middleware.service;

import com.adrain.llm_middleware.record.api.OpenAiResponse;

public interface PromptService {

  OpenAiResponse getResponse(String prompt);
  
}

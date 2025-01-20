package com.adrain.llm_middleware.mapper;

import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.record.prompt.PromptRequest;
import com.adrain.llm_middleware.record.prompt.PromptResponse;

import org.springframework.stereotype.Component;

/**
 * Component to map {@link PromptRequest} && {@link PromptResponse} to
 * {@link Prompt} model
 * */
@Component
public class PromptMapper {

 
  /**
   * 
   * Maps a {@link PromptRequest} to a {@link Prompt}.
   * Currently only the prompt string is mapped as the response is
   * not yet available, and the user is fethed through application context in
   * the service implementation
   *
   * @param request the request from the end user
   * @return the prompt mapped from the request
   *
   * */
  public Prompt toPromptFromRequest(PromptRequest request) {
    Prompt prompt = new Prompt();
    prompt.setPrompt(request.prompt());
    return prompt;
  }
  
}

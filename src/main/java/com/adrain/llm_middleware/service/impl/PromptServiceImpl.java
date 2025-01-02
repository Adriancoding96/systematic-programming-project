package com.adrain.llm_middleware.service.impl;

import java.util.List;

import com.adrain.llm_middleware.api.OpenAiClient;
import com.adrain.llm_middleware.record.api.OpenAiResponse;
import com.adrain.llm_middleware.record.prompt.PromptRequest;
import com.adrain.llm_middleware.record.prompt.PromptResponse;
import com.adrain.llm_middleware.repository.PromptRepository;
import com.adrain.llm_middleware.service.PromptService;
import com.adrain.llm_middleware.util.KeywordSearcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class PromptServiceImpl implements PromptService {

  private final PromptRepository promptRepository;
  private final OpenAiClient openAiClient;
  private final KeywordSearcher keywordSearcher;

  @Autowired
  public PromptServiceImpl(PromptRepository promptRepository, OpenAiClient openAiClient) {
    this.promptRepository = promptRepository;
    this.openAiClient = openAiClient;
    this.keywordSearcher = KeywordSearcher.getInstance();
  }

  /*
   * Method extracts response from llm api and returns it to PromptController
   *
   * @param request: record that contains prompt text
   * @return response: returns record that contains response text and keywords found in response
   * */
  @Override
  public PromptResponse newPrompt(PromptRequest request) {
    OpenAiResponse fullResponse = getResponse(request.prompt());
    List<String> keywords = keywordSearcher.getKeywords(fullResponse.choices().get(0).message().content());
    return new PromptResponse(fullResponse.choices().get(0).message().content(), keywords);
     
  }

  /*
   * Method calls opanai api client which returns a response if successfull
   *
   * @param prompt: contains prompt from user
   * @return response: contains responses from openai api and meta data
   * */
  private OpenAiResponse getResponse(String prompt) {
    Mono<OpenAiResponse> monoResponse = openAiClient.getCompletion(prompt);
    return monoResponse.block();
  }

}

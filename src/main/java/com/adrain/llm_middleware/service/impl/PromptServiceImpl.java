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

/**
 * PromptServiceImpl is the implementation of {@link PromptService} that handles creating new {@link PromptResponse}
 * objects by communicating with OpenAI API and extracting keywords from the resulting text.
 * <p>
 *     Uses {@link PromptRepository}, {@link OpenAiClient}, and {@link KeywordSearcher} for
 *     repository operations, OpenAI requests, and keyword extraction respectively.
 * </p>
 */
@Service
public class PromptServiceImpl implements PromptService {

  private final PromptRepository promptRepository;
  private final OpenAiClient openAiClient;
  private final KeywordSearcher keywordSearcher;

  @Autowired
  public PromptServiceImpl(PromptRepository promptRepository, OpenAiClient openAiClient, KeywordSearcher keywordSearcher) {
    this.promptRepository = promptRepository;
    this.openAiClient = openAiClient;
    this.keywordSearcher = keywordSearcher;
  }

  /**
   * Creates a new prompt response by sending the given prompt to the OpenAI API
   * and extracting keywords from the returned content.
   * <p>
   *     Verifies that:
   * </p>
   * <ul>
   *   <li>A valid completion is retrieved from the OpenAI service.</li>
   *   <li>Keywords are extracted from the completion text.</li>
   * </ul>
   *
   * @param request The {@link PromptRequest} containing the prompt text.
   * @return A {@link PromptResponse} containing the completion text and the extracted keywords.
   */
  @Override
  public PromptResponse newPrompt(PromptRequest request) {
    OpenAiResponse fullResponse = getResponse(request.prompt());
    List<String> keywords = keywordSearcher.getKeywords(fullResponse.choices().get(0).message().content());
    return new PromptResponse(fullResponse.choices().get(0).message().content(), keywords);
     
  }

  /**
   * Retrieves a response from OpenAI using the provided prompt.
   * <p>
   *     Blocks on the reactive response to obtain the {@link OpenAiResponse}.
   * </p>
   *
   * @param prompt The prompt text to be sent to OpenAI.
   * @return The response provided by the OpenAI service.
   */
  private OpenAiResponse getResponse(String prompt) {
    Mono<OpenAiResponse> monoResponse = openAiClient.getCompletion(prompt);
    return monoResponse.block();
  }

}

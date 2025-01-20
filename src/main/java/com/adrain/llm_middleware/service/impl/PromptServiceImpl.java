package com.adrain.llm_middleware.service.impl;

import java.util.List;
import java.util.stream.Stream;

import com.adrain.llm_middleware.api.OpenAiClient;
import com.adrain.llm_middleware.mapper.PromptMapper;
import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.model.User;
import com.adrain.llm_middleware.record.api.OpenAiResponse;
import com.adrain.llm_middleware.record.prompt.PromptRequest;
import com.adrain.llm_middleware.record.prompt.PromptResponse;
import com.adrain.llm_middleware.repository.PromptRepository;
import com.adrain.llm_middleware.service.PromptService;
import com.adrain.llm_middleware.service.UserService;
import com.adrain.llm_middleware.util.KeywordMatcher;
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
  private final PromptMapper promptMapper;
  private final UserService userService;
  private final KeywordMatcher keywordMatcher;

  @Autowired
  public PromptServiceImpl(PromptRepository promptRepository, OpenAiClient openAiClient, KeywordSearcher keywordSearcher,
      PromptMapper promptMapper, UserService userService, KeywordMatcher keywordMatcher) {
    this.promptRepository = promptRepository;
    this.openAiClient = openAiClient;
    this.keywordSearcher = keywordSearcher;
    this.promptMapper = promptMapper;
    this.userService = userService;
    this.keywordMatcher = keywordMatcher;
  }

  /**
   * Creates a new {@Link Prompt} response by sending the given prompt to the OpenAI API
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
    savePrompt(request);
    OpenAiResponse fullResponse = getResponse(request.prompt());
    List<String> keywords = keywordSearcher.getKeywords(fullResponse.choices().get(0).message().content());
    return new PromptResponse(fullResponse.choices().get(0).message().content(), keywords);
  }

  /**
   * Checks if a {@link Prompt} exists in database belonging to authenticated user, using
   * {@link UserService} to fetch user by security context, and {@link KeywordMatcher} to
   * calculate similarity score. If a {@link Prompt} exitsts with a similarity score higher
   * then 0.8 {@link Prompt} will not be null and return true, otherwise method will return
   * false.
   * <p>
   *     Checks if:
   * </p>
   * <ul>
   *   <li>{@link Prompt} belonging to user with similarity score over 0.8 exists in database.</li>
   * </ul>
   *
   * @param prompt The {@link Prompt} containing the prompt text.
   * @return A {@primitive boolean} false if {@link Prompt} does not exist, true if it does.
   */
  private boolean checkIfPromptWithHighSimilarityExistsInDatabase(Prompt prompt) {
    User user = userService.getUserBySecurityContext();
    Stream<Prompt> promptStream = promptRepository.findAllByUserEmail(user.getEmail());
    Prompt existingPromt = keywordMatcher.checkSimilarityOfTextAndStream(prompt.getPrompt(), promptStream);
    if(existingPromt == null) return false;
    return true;
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


  /**
   * Saves {@link Prompt} to the database based on {@link PromptRequest}
   * content & authenticated {@link User} from security context.
   *
   * @param request contains prompt request data.
   */
  private void savePrompt(PromptRequest request) {
    Prompt prompt = promptMapper.toPromptFromRequest(request);
    User user = userService.getUserBySecurityContext();
    //Todo figure out what to do if user is not present
    prompt.setUser(user);
    promptRepository.save(prompt);
  }

}

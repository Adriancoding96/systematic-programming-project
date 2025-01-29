package com.adrain.llm_middleware.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.transaction.Transactional;

import com.adrain.llm_middleware.api.OpenAiClient;
import com.adrain.llm_middleware.exception.PromptNotFoundException;
import com.adrain.llm_middleware.mapper.PromptMapper;
import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.model.Response;
import com.adrain.llm_middleware.model.User;
import com.adrain.llm_middleware.record.api.OpenAiResponse;
import com.adrain.llm_middleware.record.prompt.PromptRecord;
import com.adrain.llm_middleware.record.prompt.PromptRequest;
import com.adrain.llm_middleware.record.prompt.PromptResponse;
import com.adrain.llm_middleware.repository.PromptRepository;
import com.adrain.llm_middleware.service.PromptService;
import com.adrain.llm_middleware.service.ResponseService;
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
 *     Uses {@link PromptRepository}, {@link OpenAiClient}, {@link KeywordSearcher} and, {@link ResponseService} for
 *     repository operations, OpenAI requests, keyword extraction and, handling related {@link Response}s.
 * </p>
 *
 * @see PromptService
 * @see Prompt
 * @see PromptRequest
 * @see PromptResponse
 * @see OpenAiClient
 * @see PromptRepository
 * @see KeywordSearcher
 * @see ResponseService
 */
@Service
public class PromptServiceImpl implements PromptService {

  private final PromptRepository promptRepository;
  private final OpenAiClient openAiClient;
  private final KeywordSearcher keywordSearcher;
  private final PromptMapper promptMapper;
  private final UserService userService;
  private final KeywordMatcher keywordMatcher;
  private final ResponseService responseService;

  @Autowired
  public PromptServiceImpl(PromptRepository promptRepository, OpenAiClient openAiClient, KeywordSearcher keywordSearcher,
      PromptMapper promptMapper, UserService userService, KeywordMatcher keywordMatcher, ResponseService responseService) {
    this.promptRepository = promptRepository;
    this.openAiClient = openAiClient;
    this.keywordSearcher = keywordSearcher;
    this.promptMapper = promptMapper;
    this.userService = userService;
    this.keywordMatcher = keywordMatcher;
    this.responseService = responseService;
    
  }

  /**
   * Method either gets a {@link Response} from database, or openai depening if
   * a {@link Prompt} with similarity score 0.8 or higher exists in database by user.
   * If similar {@link Prompt} does not exist in database, prompt is persisted to database.
   * <p>
   *     Checks if:
   * </p>
   * <ul>
   *   <li>{@link Prompt exists in database}.</li>
   * </ul>
   *
   * @param request The {@link PromptRequest} containing the prompt text.
   * @return A {@link PromptResponse} containing the completion text and the extracted keywords from database or openai.
   */
  @Transactional
  @Override
  public PromptResponse newPrompt(PromptRequest request) {
    Prompt prompt = promptMapper.toPromptFromRequest(request);
    Prompt existingPrompt = getPromptWithHighSimilarityScoreIfExistsInDatabase(prompt);
    if(existingPrompt != null){
      Response response = responseService.getResponseByPromptId(existingPrompt.getId());
      return new PromptResponse(response.getResponseBody(), response.getMetaData(), prompt.getUuid());
    } else {
      Prompt savedPrompt = savePrompt(request);
      return sendPromptToOpenAi(request, savedPrompt.getUuid());
    }

  }

  /**
   * Creates a new {@Link Prompt} response by sending the given prompt to the OpenAI API
   * and extracting keywords from the returned content.
   * <p>
   *     Checks if:
   * </p>
   * <ul>
   *   <li>A valid completion is retrieved from the OpenAI service.</li>
   *   <li>Keywords are extracted from the completion text.</li>
   * </ul>
   *
   * @param request The {@link PromptRequest} containing the prompt text.
   * @param promptUuid needed for constructing the {@link PromptResponse} record.
   * @return A {@link PromptResponse} containing the completion text and the extracted keywords.
   */
  private PromptResponse sendPromptToOpenAi(PromptRequest request, String promptUuid) {
    OpenAiResponse fullResponse = getResponse(request.prompt());
    List<String> keywords = keywordSearcher.getKeywords(fullResponse.choices().get(0).message().content());
    return new PromptResponse(fullResponse.choices().get(0).message().content(), keywords, promptUuid);
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
   * @return a {@primitive boolean} false if {@link Prompt} does not exist, true if it does.
   */
  @Transactional
  private Prompt getPromptWithHighSimilarityScoreIfExistsInDatabase(Prompt prompt) {
    User user = userService.getUserBySecurityContext();
    Stream<Prompt> promptStream = promptRepository.findAllByUserEmail(user.getEmail());
    Prompt existingPrompt = keywordMatcher.checkSimilarityOfTextAndStream(prompt.getPrompt(), promptStream);
    return existingPrompt;
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
   * @return savedPrompt containing the persisted {@link Prompt}
   */
  private Prompt savePrompt(PromptRequest request) {
    Prompt prompt = promptMapper.toPromptFromRequest(request);
    User user = userService.getUserBySecurityContext();
    //Todo figure out what to do if user is not present
    prompt.setUser(user);
    return promptRepository.save(prompt);
  }

  /**
   * Fetches all {@link Prompt}s as {@link PromptRecord} from database.
   *
   * @return a list of {@link PromptRecord}s mapped from {@link Prompt} entities.
   */
  public List<PromptRecord> getAllPrompts() {
    return promptRepository.findAll()
      .stream()
      .map(promptMapper::toRecordFromPrompt)
      .collect(Collectors.toList());
  }

  /**
   * Fetches all {@link Prompt}s as {@link PromptRecord} from database by {@link User} email.
   *
   * @return a list of {@link PromptRecord}s mapped from {@link Prompt} entities.
   */
  public List<PromptRecord> getAllPromptsByUserEmail(String email) {
    return promptRepository.findAllByUserEmail(email)
      .map(promptMapper::toRecordFromPrompt)
      .collect(Collectors.toList());
  }

  /**
   * Fetches {@link Prompt} from the database by id amd returns it as {@link PromptRecord}.
   *
   * @return a fethced {@link Prompt} as {@link PromptRecord}.
   */
  public PromptRecord getPromptById(Long id) {
    Prompt prompt = promptRepository.findById(id)
      .orElseThrow(() -> new PromptNotFoundException("Prompt cpuld not be found in database with id: " + id));
    return promptMapper.toRecordFromPrompt(prompt);
  }


  /**
   * Deletes {@link Response} from the database.
   */
  public void deletePromptById(Long id) {
    promptRepository.deleteById(id);
  }
 

}

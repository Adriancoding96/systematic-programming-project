package com.adrain.llm_middleware.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.adrain.llm_middleware.api.OpenAiClient;
import com.adrain.llm_middleware.mapper.PromptMapper;
import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.model.Response;
import com.adrain.llm_middleware.model.User;
import com.adrain.llm_middleware.record.prompt.PromptRecord;
import com.adrain.llm_middleware.record.prompt.PromptRequest;
import com.adrain.llm_middleware.record.prompt.PromptResponse;
import com.adrain.llm_middleware.repository.PromptRepository;
import com.adrain.llm_middleware.service.impl.PromptServiceImpl;
import com.adrain.llm_middleware.util.KeywordMatcher;
import com.adrain.llm_middleware.util.KeywordSearcher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Unit tests for the {@link PromptServiceImpl} class.
 *
 * <p>This test class uses the {@link MockitoExtension} to create mock instances of
 * the dependencies required by {@link PromptServiceImpl}, isolating the service
 * from external components such as {@link PromptRepository}, {@link OpenAiClient},
 * and {@link UserService}. The goal is to verify the business logic and interactions
 * with these dependencies in a controlled (mocked) environment.</p>
 *
 * <p>The following key components are mocked in this test:
 * <ul>
 *   <li>{@link PromptRepository} – Data access layer for {@link Prompt} entities.</li>
 *   <li>{@link OpenAiClient} – External client for OpenAI-related operations.</li>
 *   <li>{@link KeywordSearcher} and {@link KeywordMatcher} – For text analysis and similarity checks.</li>
 *   <li>{@link PromptMapper} – For converting between {@link Prompt} entities, DTOs, and records.</li>
 *   <li>{@link UserService} – For retrieving the current authenticated user and user details.</li>
 *   <li>{@link ResponseService} – For retrieving responses linked to existing prompts.</li>
 * </ul>
 * </p>
 *
 * <p>The tests cover these scenarios:
 * <ul>
 *   <li>{@link #testNewPrompt_whenSimilarPromptExistsInDatabase()} – Verifies that existing prompts
 *       are handled correctly when creating a new {@link Prompt}, and that a response is retrieved
 *       if a similar prompt already exists.</li>
 *   <li>{@link #testGetAllPrompts()} – Ensures all prompts in the repository are returned and properly
 *       mapped to {@link PromptRecord} objects.</li>
 *   <li>{@link #testGetAllPromptsByUserEmail()} – Ensures only prompts associated with a specific
 *       user email are returned.</li>
 *   <li>{@link #testGetPromptById()} – Ensures a prompt can be retrieved by its ID and mapped to
 *       a corresponding {@link PromptRecord}.</li>
 *   <li>{@link #testDeletePromptById()} – Ensures a prompt is properly deleted by its ID.</li>
 * </ul>
 * </p>
 *
 * <p>This class also sets up a mock {@link SecurityContextHolder} to simulate the
 * authenticated user's context, ensuring methods that depend on user information
 * work as expected.</p>
 *
 * @see PromptService
 * @see PromptServiceImpl
 * @see PromptRepository
 * @see PromptMapper
 * @see UserService
 * @see ResponseService
 * @see MockitoExtension
 */
@ExtendWith(MockitoExtension.class)
class PromptServiceTest {

  @Mock
  private PromptRepository promptRepository;
  @Mock
  private OpenAiClient openAiClient;
  @Mock
  private KeywordSearcher keywordSearcher;
  @Mock
  private PromptMapper promptMapper;
  @Mock
  private UserService userService;
  @Mock
  private KeywordMatcher keywordMatcher;
  @Mock
  private ResponseService responseService;

  @InjectMocks
  private PromptServiceImpl promptService;

  @BeforeEach
  void setupSecurityContext() {
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("adrian@example.com", null,
        List.of());
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  /**
   * Tests that when a prompt similar to the one requested already exists in the database,
   * {@link PromptServiceImpl#newPrompt(PromptRequest)} returns an appropriate
   * {@link PromptResponse} containing existing data instead of creating a duplicate record.
   *
   * <p>This test does the following:
   * <ul>
   *   <li>Mocks {@link UserService#getUserBySecurityContext()} to retrieve a {@link User}.</li>
   *   <li>Mocks the {@link PromptMapper} and {@link PromptRepository} to simulate
   *       existing prompt data for the authenticated user.</li>
   *   <li>Mocks the {@link ResponseService} to return a stored {@link Response}
   *       associated with the existing prompt.</li>
   * </ul>
   * </p>
   */
  @Test
  public void testNewPrompt_whenSimilarPromptExistsInDatabase() {
    PromptRequest request = new PromptRequest("How do i not cause stack overflow???", "gpt-3.5-turbo");

    Prompt prompt = new Prompt();
    prompt.setPrompt("How do i not cause stack overflow???");
    prompt.setUuid("12345");

    User user = new User();
    user.setName("Adrian");
    user.setEmail("adrian@example.com");
    user.setPassword("verysecure123");

    Response response = new Response();
    response.setResponseBody("The stack can't stack that high");
    response.setMetaData(List.of("stack"));

    when(userService.getUserBySecurityContext()).thenReturn(user);
    when(promptMapper.toPromptFromRequest(request)).thenReturn(prompt);
    when(promptRepository.findAllByUserEmail(user.getEmail())).thenReturn(Stream.of(prompt));
    when(keywordMatcher.checkSimilarityOfTextAndStream(anyString(), any())).thenReturn(prompt);
    when(responseService.getResponseByPromptId(null)).thenReturn(response);

    PromptResponse result = promptService.newPrompt(request);

    assertNotNull(result, "Expected non-null result from newPrompt");
  }

  /**
   * Tests {@link PromptServiceImpl#getAllPrompts()} to ensure that all stored prompts
   * are returned and mapped correctly to {@link PromptRecord} objects.
   *
   * <p>This test does the following:
   * <ul>
   *   <li>Mocks {@link PromptRepository#findAll()} to return a list of prompts.</li>
   *   <li>Verifies that each {@link Prompt} is mapped to a {@link PromptRecord} using
   *       {@link PromptMapper#toRecordFromPrompt(Prompt)}.</li>
   * </ul>
   * </p>
   */
  @Test
  public void testGetAllPrompts() {
    Prompt prompt1 = new Prompt();
    prompt1.setPrompt("How do i write unit tests faster?");
    prompt1.setUuid("12345");

    Prompt prompt2 = new Prompt();
    prompt2.setPrompt("How do i run unit tests faster?");
    prompt1.setUuid("54321");

    when(promptRepository.findAll()).thenReturn(List.of(prompt1, prompt2));
    when(promptMapper.toRecordFromPrompt(prompt1)).thenReturn(new PromptRecord("How do i write unit tests faster?", "12345"));
    when(promptMapper.toRecordFromPrompt(prompt2)).thenReturn(new PromptRecord("How do i run unit tests faster?", "54321"));

    List<PromptRecord> result = promptService.getAllPrompts();
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("How do i write unit tests faster?", result.get(0).prompt());
    assertEquals("12345", result.get(0).uuid());
    assertEquals("How do i run unit tests faster?", result.get(1).prompt());
    assertEquals("54321", result.get(1).uuid());

  }

  /**
   * Tests {@link PromptServiceImpl#getAllPromptsByUserEmail(String)} to ensure that
   * only prompts associated with a specific user email are returned.
   *
   * <p>This test does the following:
   * <ul>
   *   <li>Mocks {@link PromptRepository#findAllByUserEmail(String)} to return a stream of
   *       prompts belonging to the given email.</li>
   *   <li>Verifies that the returned list of {@link PromptRecord} objects is correctly
   *       mapped and matches the expected size.</li>
   * </ul>
   * </p>
   */
  @Test
  public void testGetAllPromptsByUserEmail() {
    String email = "adrian@example.com";
    Prompt prompt1 = new Prompt();
    prompt1.setPrompt("How do i write unit tests faster?");
    prompt1.setUuid("12345");

    Prompt prompt2 = new Prompt();
    prompt2.setPrompt("How do i run unit tests faster?");
    prompt1.setUuid("54321");

    when(promptRepository.findAllByUserEmail(email)).thenReturn(Stream.of(prompt1, prompt2));
    when(promptMapper.toRecordFromPrompt(prompt1)).thenReturn(new PromptRecord("How do i write unit tests faster?", "12345"));
    when(promptMapper.toRecordFromPrompt(prompt2)).thenReturn(new PromptRecord("How do i run unit tests faster?", "54321"));

    List<PromptRecord> result = promptService.getAllPromptsByUserEmail(email);
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("How do i write unit tests faster?", result.get(0).prompt());
    assertEquals("12345", result.get(0).uuid());
    assertEquals("How do i run unit tests faster?", result.get(1).prompt());
    assertEquals("54321", result.get(1).uuid());
  }


  /**
   * Tests {@link PromptServiceImpl#getPromptById(Long)} to ensure a prompt
   * can be retrieved by its identifier and mapped correctly to a {@link PromptRecord}.
   *
   * <p>This test does the following:
   * <ul>
   *   <li>Mocks {@link PromptRepository#findById(Long)} to return an existing prompt.</li>
   *   <li>Verifies the prompt is mapped to the expected {@link PromptRecord} fields.</li>
   * </ul>
   * </p>
   */
  @Test
  public void testGetPromptById() {
    Prompt prompt = new Prompt();
    prompt.setPrompt("Hello this is a prompt");
    prompt.setUuid("7890");

    when(promptRepository.findById(1L)).thenReturn(Optional.of(prompt));
    when(promptMapper.toRecordFromPrompt(prompt)).thenReturn(new PromptRecord("Hello this is a prompt", "7890"));

    PromptRecord result = promptService.getPromptById(1L);
    assertNotNull(result);
    assertEquals("Hello this is a prompt", result.prompt());
    assertEquals("7890", result.uuid());
  }

  /**
   * Tests {@link PromptServiceImpl#deletePromptById(Long)} to ensure that a prompt
   * is properly deleted from the repository by its ID.
   *
   * <p>This test does the following:
   * <ul>
   *   <li>Mocks {@link PromptRepository#deleteById(Long)} with no errors thrown.</li>
   *   <li>Verifies that the method is indeed called once with the correct ID.</li>
   * </ul>
   * </p>
   */
  @Test
  public void testDeletePromptById() {
    doNothing().when(promptRepository).deleteById(1L);
    promptService.deletePromptById(1L);
    verify(promptRepository, times(1)).deleteById(1L);
  }

}

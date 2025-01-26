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

  @Test
  public void testDeletePromptById() {
    doNothing().when(promptRepository).deleteById(1L);
    promptService.deletePromptById(1L);
    verify(promptRepository, times(1)).deleteById(1L);
  }

}

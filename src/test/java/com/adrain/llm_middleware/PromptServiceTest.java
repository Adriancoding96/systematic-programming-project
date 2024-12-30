package com.adrain.llm_middleware;

import java.util.List;

import com.adrain.llm_middleware.api.OpenAiClient;
import com.adrain.llm_middleware.record.Prompt.PromptRequest;
import com.adrain.llm_middleware.record.Prompt.PromptResponse;
import com.adrain.llm_middleware.record.api.OpenAiResponse;
import com.adrain.llm_middleware.repository.PromptRepository;
import com.adrain.llm_middleware.service.impl.PromptServiceImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import reactor.core.publisher.Mono;

public class PromptServiceTest {

  @Mock
  private PromptRepository promptRepository;

  @Mock
  private OpenAiClient openAiClient;

  @InjectMocks
  private PromptServiceImpl promptServiceImpl;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testNewPrompt() {
    
    PromptRequest request = new PromptRequest("How do i center a div in html?", "gpt-o1");

    OpenAiResponse.Choice choice = new OpenAiResponse.Choice("You use css LOL", 0, "stop");

    OpenAiResponse openAiResponse = new OpenAiResponse(
      "test-id",
      "gpt-o1",
      List.of(choice),
      null
    );

    Mockito.when(openAiClient.getCompletion(request.prompt()))
      .thenReturn(Mono.just(openAiResponse));

    PromptResponse response = promptServiceImpl.newPrompt(request);
    
    Assertions.assertEquals("You use css LOL", response.response());
    Mockito.verify(openAiClient).getCompletion("How do i center a div in html?");
  }

}

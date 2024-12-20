package com.adrain.llm_middleware;

import java.util.List;

import com.adrain.llm_middleware.api.OpenAiClient;
import com.adrain.llm_middleware.record.api.OpenAiResponse;
import com.adrain.llm_middleware.repository.PromptRepository;
import com.adrain.llm_middleware.service.PromptService;
import com.adrain.llm_middleware.service.impl.PromptServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PromptServiceTest {

  private PromptRepository promptRepository;
  private OpenAiClient openAiClient;
  private PromptService promptServiceImpl;

  @BeforeEach
  void setUp() {
    promptRepository = Mockito.mock(PromptRepository.class);
    openAiClient = Mockito.mock(OpenAiClient.class);
    promptServiceImpl = new PromptServiceImpl(promptRepository, openAiClient);
  }

  @Test
  void givenPromptReturnsApiResponse() {
    String prompt = "How do i center a div in html";

    OpenAiResponse mockResponse = new OpenAiResponse(
      "test-id",
      "test-model",
      List.of(new OpenAiResponse.Choice("You use css LOL", 0, "stop")),
      new OpenAiResponse.Usage(5, 5, 10)
    );
  }
}

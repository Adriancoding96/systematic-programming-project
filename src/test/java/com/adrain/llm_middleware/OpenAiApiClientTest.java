package com.adrain.llm_middleware;

import com.adrain.llm_middleware.api.OpenAiClient;
import com.adrain.llm_middleware.repository.PromptRepository;
import com.adrain.llm_middleware.service.PromptService;
import com.adrain.llm_middleware.service.impl.PromptServiceImpl;
import com.adrain.llm_middleware.util.KeywordSearcher;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class OpenAiApiClientTest {

  private PromptRepository promptRepository;
  private OpenAiClient openAiClient;
  private KeywordSearcher keywordSearcher;
  private PromptService promptServiceImpl;

  @BeforeEach
  void setUp() {
    promptRepository = Mockito.mock(PromptRepository.class);
    openAiClient = Mockito.mock(OpenAiClient.class);
    promptServiceImpl = new PromptServiceImpl(promptRepository, openAiClient, keywordSearcher);
  }

  /*
   * Test evaluates that OpenAiClient returns expected result of a Mono containing a value
   * in PromptService
   * */

  /*
  @Test
  void givenPromptReturnsApiResponse() {
    String prompt = "How do i center a div in html";

    OpenAiResponse mockResponse = new OpenAiResponse(
      "test-id",
      "test-model",
      List.of(new OpenAiResponse.Choice("You use css LOL", 0, "stop")),
      new OpenAiResponse.Usage(5, 5, 10)
    );

    when(openAiClient.getCompletion(prompt)).thenReturn(Mono.just(mockResponse));
  
    OpenAiResponse response = promptServiceImpl.getResponse(prompt); 

    verify(openAiClient).getCompletion(prompt);
    assertNotNull(response);
  }*/

  /*
   * Test checks that openai api response contains a choice contiaining text
   * */
  /*
  @Test
  void assertPromptResponseContainsChoiceWithText() {
    String prompt = "How do i center a div in html";

    OpenAiResponse mockResponse = new OpenAiResponse(
      "test-id",
      "test-model",
      List.of(new OpenAiResponse.Choice("You use css LOL", 0, "stop")),
      new OpenAiResponse.Usage(5, 5, 10)
    );

    when(openAiClient.getCompletion(prompt)).thenReturn(Mono.just(mockResponse));
  
    OpenAiResponse response = promptServiceImpl.getResponse(prompt);
    String text = response.choices().getFirst().text();
    assertNotNull(text);
  }*/
}

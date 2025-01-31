package com.adrain.llm_middleware.api;

import java.util.List;

import com.adrain.llm_middleware.record.api.ChatCompletionRequest;
import com.adrain.llm_middleware.record.api.Message;
import com.adrain.llm_middleware.record.api.OpenAiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

/**
 * OpenAiClient is a service class that interacts with the OpenAI API to retrieve
 * chat completions based on a provided prompt.
 * <p>
 *     This class uses Spring's {@link WebClient} to send HTTP POST requests
 *     to the OpenAI API. The API key is injected through the {@code api.key}
 *     property, and headers are set for authorization and content type.
 * </p>
 */
@Service
public class OpenAiClient {

  private final WebClient webClient;


  /**
   * Constructs an {@link OpenAiClient} instance with the specified API key.
   *
   * @param apiKey The API key used for authenticating requests to the OpenAI API.
   */
  @Autowired
  public OpenAiClient(@Value("${api.key}") final String apiKey) {
    this.webClient = WebClient.builder()
      .baseUrl("https://api.openai.com/v1")
      .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .build();
  }

  /**
   * Sends a prompt to the OpenAI Chat Completion endpoint and retrieves the result.
   * <p>
   *     Creates a {@link ChatCompletionRequest} with the specified parameters
   *     and handles potential errors by returning a {@link Mono} that may emit
   *     an error if the API responds with a non-2xx status code.
   * </p>
   *
   * @param prompt The prompt text to be processed by OpenAI.
   * @return A {@link Mono} emitting the {@link OpenAiResponse} containing the completion.
   */
  public Mono<OpenAiResponse> getCompletion(final String prompt) {
    List<Message> messages = List.of(new Message("user", prompt));
    ChatCompletionRequest request = new ChatCompletionRequest(
      "gpt-3.5-turbo",
      messages,
      100,
      0.7
    );

    return webClient.post()
      .uri("/chat/completions")
      .bodyValue(request)
      .retrieve()
      .onStatus(
        status -> status.is4xxClientError() || status.is5xxServerError(),
        clientResponse ->
          clientResponse.bodyToMono(String.class)
            .flatMap(errorBody -> {
              return Mono.error(new RuntimeException("OpenAi returned error: " + errorBody));
            })
      )
      .bodyToMono(OpenAiResponse.class);
  }
  
}

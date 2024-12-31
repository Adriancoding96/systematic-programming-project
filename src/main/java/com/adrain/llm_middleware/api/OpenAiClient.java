package com.adrain.llm_middleware.api;

import com.adrain.llm_middleware.record.api.OpenAiRequest;
import com.adrain.llm_middleware.record.api.OpenAiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class OpenAiClient {

  private final WebClient webClient;

  /*
   * Constructor creates a new WebClient with api endpoint, authorization header,
   * and content type header
   *
   * @param apiKey: api key for openai from enviroment variable
   * */
  @Autowired
  public OpenAiClient(@Value("${api.key}") final String apiKey) {
    this.webClient = WebClient.builder()
      .baseUrl("https://api.openai.com/v1")
      .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .build();
  }

  /*
   * Method sends request to openai api containing prompt then returns a Mono<OpenApiResponse>
   * Mono can either be a single value or empty, it is used to handle mutiple request asynchrounously
   *
   * @param prompt: Contains prompt to be sent to api
   * @return Mono<OpenApiResponse>: If successfull returns a OpenApiResponse, otherwise returns a empty mono
   *
   * */
  
  public Mono<OpenAiResponse> getCompletion(final String prompt) {
    final OpenAiRequest request = new OpenAiRequest("text-davinci-003", prompt, 100, 0.7);
    return webClient.post()
      .uri("/completions")
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

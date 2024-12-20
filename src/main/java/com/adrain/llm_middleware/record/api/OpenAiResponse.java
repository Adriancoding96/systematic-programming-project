package com.adrain.llm_middleware.record.api;

import java.util.List;


public record OpenAiResponse(
  String id,
  String model,
  List<Choice> choices,
  Usage usage
) {
  
  public record Choice(
    String text,
    Integer index,
    String finish_reason
  ) {}

  public record Usage(
    int prompt_tokens,
    int completion_tokens,
    int total_tokens
  ) {}
}

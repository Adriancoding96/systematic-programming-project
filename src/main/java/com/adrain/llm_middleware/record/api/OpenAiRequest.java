package com.adrain.llm_middleware.record.api;

public record OpenAiRequest(
  String model,
  String prompt,
  Integer max_tokens,
  Double temprature

) {}

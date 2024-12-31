package com.adrain.llm_middleware.record.api;

import java.util.List;


public record ChatCompletionRequest(
  String model,
  List<Message> messages,
  Integer max_tokens,
  Double temperature
) {}

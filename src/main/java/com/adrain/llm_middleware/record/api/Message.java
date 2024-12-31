package com.adrain.llm_middleware.record.api;

public record Message(
  String role,
  String content
) {}
